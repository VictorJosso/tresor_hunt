package Apps;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Config;
import models.Game.CaseMur;
import models.Game.CaseTresor;
import models.Game.CaseTrou;
import models.Game.CaseVide;
import models.Partie;
import models.Plateau;
import utils.CallbackInstance;
import utils.ConnectionHandler;
import utils.ImageCrop;
import utils.LeaderBoardItem;
import views.LeaderBoardController;

import java.io.IOException;
import java.util.*;

public class GameApp {
    public MainApp mainApp;
    private Stage gameStage;
    private Stage leaderBoardStage;
    private final Partie partie;
    private final Plateau plateau;
    public GraphicsContext gc;
    private AnimationTimer timer;
    private final int screenWidth;
    private final int screenHeight;
    private final int COEFF_IMAGE;

    private final HashMap<KeyCode, Long> keyEvents = new HashMap<>();
    private final ArrayList<KeyCode> directions = new ArrayList<>();

    private double dragOffsetX;
    private double dragOffsetY;

    private final ObservableList<LeaderBoardItem> leaderBoardItems = FXCollections.observableArrayList(LeaderBoardItem.extractor());

    private LeaderBoardController leaderBoardController;

    private final Map<ImageCrop, Long> haveToDrawOnTop = new HashMap<>();

    private final Image flammesImage;
    private String playerTurnUsername;
    // ajouter méthode d'initialisation du compte tours et ajouter update dans drawgame suivant la valeur du comptetour

    public GameApp(MainApp mainApp, Partie partie) {
        this.mainApp = mainApp;
        this.partie = partie;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenHeight = (int) (screenBounds.getHeight()*0.9);
        screenWidth = (int) (screenBounds.getWidth()*0.85);
        int sizeX = screenWidth/ partie.getDimensionX();
        int sizeY = screenHeight/partie.getDimensionY();
        COEFF_IMAGE = Math.min(sizeX, sizeY);
        flammesImage = new Image("flammes-scaled.png", COEFF_IMAGE, COEFF_IMAGE, false, false);
        this.plateau = new Plateau(partie.getDimensionX(), partie.getDimensionY(), COEFF_IMAGE, this);
        mainApp.getConnectionHandler().registerCallback("201", plateau, CallbackInstance::handleMoveAllowed);
        mainApp.getConnectionHandler().registerCallback("202", plateau, CallbackInstance::handleMoveBlocked);
        mainApp.getConnectionHandler().registerCallback("203", plateau, CallbackInstance::handleMoveTresor);
        mainApp.getConnectionHandler().registerCallback("401", plateau, CallbackInstance::getHoles);
        mainApp.getConnectionHandler().registerCallback("411", plateau, CallbackInstance::getTresors);
        mainApp.getConnectionHandler().registerCallback("421", plateau, CallbackInstance::getWalls);
        mainApp.getConnectionHandler().registerCallback("500", plateau, CallbackInstance::handleTurnChanged, true);
        mainApp.getConnectionHandler().registerCallback("510", plateau, CallbackInstance::updatePlayerPosition, true);
        mainApp.getConnectionHandler().registerCallback("511", plateau, CallbackInstance::updatePlayerTresor);
        mainApp.getConnectionHandler().registerCallback("520", plateau, CallbackInstance::declareDead);
        mainApp.getConnectionHandler().registerCallback("666", plateau, CallbackInstance::handleMoveDead);
        mainApp.getConnectionHandler().registerCallback("902", plateau, CallbackInstance::handleNotYourTurn);

        mainApp.getConnectionHandler().registerCallback("301", plateau, CallbackInstance::updateRevealHole);
        mainApp.getConnectionHandler().registerCallback("320", plateau, CallbackInstance::getNearHoles, true);
        mainApp.getConnectionHandler().registerCallback("330", plateau, CallbackInstance::getNearWall,true);
        mainApp.getConnectionHandler().registerCallback("340", plateau, CallbackInstance::getTresors, true);






        if (partie.getModeDeJeu().equals("3")) {
            //mainApp.getConnectionHandler().send("410 GETTREASURES");
            //mainApp.getConnectionHandler().send("420 GETWALLS");
            //mainApp.getConnectionHandler().send("400 GETHOLES");


            // pb: broadcast des joueurs : utiliser 400,410,420 ?

        } else {
            mainApp.getConnectionHandler().send("410 GETTREASURES");
            mainApp.getConnectionHandler().send("400 GETHOLES");
            mainApp.getConnectionHandler().send("420 GETWALLS");
        }




    }

    public void launch(){
        this.gameStage = new Stage();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + 5;
        double y = bounds.getMinY() + 5;
        System.out.println("X POS : "+x);
        System.out.println("Y POS : "+y);
        this.gameStage.setX(x);
        this.gameStage.setY(y);
        Group root = new Group();
        Scene gameScene = new Scene(root);
        gameStage.setScene(gameScene);

        if (partie.getModeDeJeu().equals("3")) {
            //gameScene.setFill(plateau.getListeImages().get(11));
        }

        Canvas canvas = new Canvas(partie.getDimensionX()*this.COEFF_IMAGE, partie.getDimensionY()*this.COEFF_IMAGE);
        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);

        this.gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image ("war.png", screenHeight, screenWidth, false, false), 0, 0);



        timer = new AnimationTimer(){
            @Override
            public void handle(long l) {
                drawGame();
                drawPlayers();
                drawOnTop();
            }
        };
        timer.start();

        if(this.partie.getModeDeJeu().equals("Tour par tour")){
            root.setOnKeyReleased(this::processKeyEvent);
        } else {
            root.setOnKeyPressed(this::handleKeyPressed);
        }

        this.gameStage.setOnCloseRequest(windowEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quitter la partie ?");
            alert.setContentText("Une partie est en cours. Es-tu sûr de vouloir abandonner ?");
            alert.initOwner(gameStage.getOwner());
            alert.setHeaderText(null);
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent()){
                if (res.get().equals(ButtonType.CANCEL)){
                    windowEvent.consume();
                }
                else {
                    timer.stop();
                    leaderBoardStage.close();
                    this.releaseAllCallbacks();
                    mainApp.gameStageClosed();
                }
            }

        });


        this.leaderBoardStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/leaderBoard.fxml"));
        AnchorPane rootPane = null;
        try {
            rootPane = loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }
        assert rootPane != null;
        Scene scene = new Scene(rootPane, bounds.getWidth() * 0.15 - 5, partie.getDimensionY() * COEFF_IMAGE);

        this.leaderBoardStage.setScene(scene);
        this.leaderBoardStage.initStyle(StageStyle.UNDECORATED);
        this.leaderBoardStage.setX(x + partie.getDimensionX() * COEFF_IMAGE + 5);
        System.out.println("LEADERBOARD X POS : "+ (x + partie.getDimensionX()*this.COEFF_IMAGE + 5));
        //this.leaderBoardStage.setX(y);
        this.leaderBoardStage.setTitle("Leader Board");
        this.leaderBoardStage.setResizable(false);

        scene.setOnMousePressed(this::handleMousePressed);
        scene.setOnMouseDragged(this::handleMouseDragged);

        leaderBoardController = loader.getController();
        leaderBoardController.setGameApp(this);

        this.gameStage.setResizable(false);
        this.gameStage.show();
        this.leaderBoardStage.show();
        int enc = (int) (this.gameStage.getHeight() - partie.getDimensionY()*this.COEFF_IMAGE);
        this.leaderBoardStage.setY(y + enc);
    }

    private void releaseAllCallbacks(){
        this.mainApp.getConnectionHandler().releaseCallback("201");
        this.mainApp.getConnectionHandler().releaseCallback("202");
        this.mainApp.getConnectionHandler().releaseCallback("203");
        this.mainApp.getConnectionHandler().releaseCallback("401");
        this.mainApp.getConnectionHandler().releaseCallback("411");
        this.mainApp.getConnectionHandler().releaseCallback("421");
        this.mainApp.getConnectionHandler().releaseCallback("500");
        this.mainApp.getConnectionHandler().releaseCallback("510");
        this.mainApp.getConnectionHandler().releaseCallback("511");
        this.mainApp.getConnectionHandler().releaseCallback("520");
        this.mainApp.getConnectionHandler().releaseCallback("666");
        this.mainApp.getConnectionHandler().releaseCallback("902");

        this.mainApp.getConnectionHandler().releaseCallback("301");
        this.mainApp.getConnectionHandler().releaseCallback("320");
        this.mainApp.getConnectionHandler().releaseCallback("330");
        this.mainApp.getConnectionHandler().releaseCallback("340");
    }

    private void processKeyEvent(KeyEvent keyEvent){
        System.out.println("TOUCHE PRESSEE : " + keyEvent.getCode().getName());
        this.directions.add(keyEvent.getCode());
        switch(keyEvent.getCode()){
            case UP:
                this.mainApp.getConnectionHandler().send("200 GOUP");
                break;
            case DOWN:
                this.mainApp.getConnectionHandler().send("200 GODOWN");
                break;
            case LEFT:
                this.mainApp.getConnectionHandler().send("200 GOLEFT");
                break;
            case RIGHT:
                this.mainApp.getConnectionHandler().send("200 GORIGHT");
                break;
            default:
                break;
        }
    }


    private void handleKeyPressed(KeyEvent keyEvent) {
        long currentTime = System.currentTimeMillis();
        Long lastCall = this.keyEvents.get(keyEvent.getCode());
        if (lastCall == null || (currentTime - lastCall) > 250){
            this.keyEvents.put(keyEvent.getCode(), currentTime);
            this.processKeyEvent(keyEvent);
        } else {
            keyEvent.consume();
        }
    }








    protected void drawGame() {
        if(this.partie.getModeDeJeu().equals("3")) {
            gc.drawImage(plateau.getListeImages().get(11), 0, 0);
            for (int x = 0; x < partie.getDimensionX(); x++) {
                for (int y = 0; y < partie.getDimensionY(); y++) {
                    if (!(plateau.getPlateau().get(x).get(y) instanceof CaseVide)) {
                        //System.out.println("draw !");
                        gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x * this.COEFF_IMAGE, y * this.COEFF_IMAGE);
                    } else {
                        if( plateau.getPlateau().get(x).get(y).isVisitee()) {
                            //System.out.println("visiteeeee");
                            gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x * this.COEFF_IMAGE, y * this.COEFF_IMAGE);
                        }
                    }
                }
            }

           /* for (int x = 0; x < partie.getDimensionX(); x++) {
                for (int y = 0; y < partie.getDimensionY(); y++) {
                    if (plateau.getPlateau().get(x).get(x) instanceof  CaseTresor) {
                        gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x * this.COEFF_IMAGE, y * this.COEFF_IMAGE);
                    }
                }
            }*/
            if (plateau.getCompteToursRevealHole()>0) {
                System.out.println("reveal hole----------------------------------------");
                plateau.updateCompteToursRevealHole();
            }

         /*   int x = plateau.getCoordonneesJoueurs().get(playerTurnUsername).getX(); // non : on veut pas ça, mais le client seul...
            int y = plateau.getCoordonneesJoueurs().get(playerTurnUsername).getY();

            for (int i = x-2; i <= x+2; i++) {
                for (int j = y-2; j <= y+2; j++) {
                    if (!plateau.horsLimite(i,j)) {
                        if (plateau.getPlateau().get(i).get(j) instanceof CaseMur || (i==x && j==y)) {
                            gc.drawImage(plateau.getPlateau().get(i).get(j).getImageCase(), i * this.COEFF_IMAGE, j * this.COEFF_IMAGE);
                        }
                    }
                }
            }*/




            /*for(String name : plateau.getCoordonneesJoueurs().keySet()) {
                if (name.equals(playerTurnUsername) && plateau.getCompteToursRevealHole()>0) {
                    System.out.println("tour : "+plateau.getCompteToursRevealHole());
                    for (int i=x-1;i<=x+1;i++) {
                        for (int j=y-1;j<=y+1;j++) {
                            if  (plateau.getPlateau().get(i).get(j) instanceof CaseTrou) {
                                gc.drawImage(plateau.getPlateau().get(i).get(j).getImageCase(), i * this.COEFF_IMAGE, j * this.COEFF_IMAGE);
                            }
                        }
                    }
                    plateau.updateCompteToursRevealHole();
                }
            }*/

        } else {
            for (int x = 0; x < partie.getDimensionX(); x++) {
                for (int y = 0; y < partie.getDimensionY(); y++) {
                        gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x * this.COEFF_IMAGE, y * this.COEFF_IMAGE);
                }
            }
        }
    }

    protected void drawPlayers() {
        for(String name : plateau.getCoordonneesJoueurs().keySet()) {
            String nameToDraw;
            if (!(this.partie.getModeDeJeu().equals("3"))) {
                nameToDraw = name;
            } else {
                nameToDraw= name +" | "+plateau.getTrousRayon1()+ " trou(s)";
            }
            if (!plateau.getCoordonneesJoueurs().get(name).isAlive()){
                if(!name.equals(mainApp.getServerConfig().getUsername()) && plateau.getCoordonneesJoueurs().get(name).getKillDate() + 1000 > System.currentTimeMillis()){
                    gc.drawImage(flammesImage, plateau.getCoordonneesJoueurs().get(name).getX()*COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY()*COEFF_IMAGE);
                } else {
                    continue;
                }
            } else {
                if (this.mainApp.getServerConfig().getUsername().equals(name)) {
                    gc.drawImage(plateau.getListeImages().get(7), plateau.getCoordonneesJoueurs().get(name).getX() * COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY() * COEFF_IMAGE);
                    if (this.partie.getModeDeJeu().equals("3")) {
                        nameToDraw = "Moi"+" | "+plateau.getTrousRayon1()+ " trous";
                    } else {
                        nameToDraw = "Moi";
                    }
                } else {
                    gc.drawImage(plateau.getListeImages().get(9), plateau.getCoordonneesJoueurs().get(name).getX() * COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY() * COEFF_IMAGE);
                }
                if (name.equals(this.playerTurnUsername)){
                    gc.setStroke(Color.GREEN);
                    gc.setLineWidth(2);
                    gc.strokeOval((plateau.getCoordonneesJoueurs().get(name).getX() + 0.5 - Math.sqrt(2)/2) * COEFF_IMAGE, (plateau.getCoordonneesJoueurs().get(name).getY() + 0.5 - Math.sqrt(2)/2) * COEFF_IMAGE, COEFF_IMAGE*Math.sqrt(2), COEFF_IMAGE*Math.sqrt(2));
                }
            }
            gc.setFill(new Color(0,0,0,0.3));
            Text t = new Text();
            t.setText(nameToDraw);
            t.setFont(Font.font("Chilanka Regular", 20));
            int sizeX = (int) (t.getLayoutBounds().getWidth());
            int sizeY = (int) (t.getLayoutBounds().getHeight());
            gc.fillRect((int) ((plateau.getCoordonneesJoueurs().get(name).getX()+0.5)*COEFF_IMAGE - sizeX/2 - 4) , (int) ((plateau.getCoordonneesJoueurs().get(name).getY())*COEFF_IMAGE - (sizeY/2.) - 22), sizeX+8, 4+sizeY);
            gc.setFont(Font.font("Chilanka Regular", 20));
            gc.setFill(new Color(1,1,1,1));
            gc.fillText(nameToDraw, (int) ((plateau.getCoordonneesJoueurs().get(name).getX()+0.5)*COEFF_IMAGE - sizeX/2), ((plateau.getCoordonneesJoueurs().get(name).getY())*COEFF_IMAGE) - 18 + 2);
        }
    }

    public void registerDrawOnTop(ImageCrop imageCrop, long duration){
        System.out.println("On enregistre une demande de draw on top jusqua " + (System.currentTimeMillis() + duration) +" actuellement "+System.currentTimeMillis());
        this.haveToDrawOnTop.put(imageCrop, System.currentTimeMillis() + duration);
    }

    private void drawOnTop(){
        for (ImageCrop i : this.haveToDrawOnTop.keySet()){
            if (this.haveToDrawOnTop.get(i) > System.currentTimeMillis()){
                gc.drawImage(i.getImage(), i.getCropStartX(), i.getCropStartY(), i.getCropWidth(), i.getCropHeight(), 0, 0, getScreenWidth(), getScreenHeight());
            } else {
                this.haveToDrawOnTop.remove(i);
            }
        }
    }

    protected void handleMousePressed(MouseEvent e)
    {
        this.dragOffsetX = e.getScreenX() - this.leaderBoardStage.getX();
        this.dragOffsetY = e.getScreenY() - this.leaderBoardStage.getY();
    }

    protected void handleMouseDragged(MouseEvent e)
    {
        this.leaderBoardStage.setX(e.getScreenX() - this.dragOffsetX);
        this.leaderBoardStage.setY(e.getScreenY() - this.dragOffsetY);
    }

    public ArrayList<KeyCode> getDirections() {
        return directions;
    }

    public Config getServerConfig(){
        return mainApp.getServerConfig();
    }

    public ConnectionHandler getConnectionHandler(){
        return mainApp.getConnectionHandler();
    }

    public ObservableList<LeaderBoardItem> getLeaderBoardItems() {
        return leaderBoardItems;
    }

    public int getScreenWidth() {
        return COEFF_IMAGE * partie.getDimensionX();
    }

    public int getScreenHeight() {
        return COEFF_IMAGE * partie.getDimensionY();
    }

    public void setPlayerTurnUsername(String playerTurnUsername) {
        this.playerTurnUsername = playerTurnUsername;
    }

    public Partie getPartie() {
        return partie;
    }

    public String getPlayerTurnUsername() {
        return playerTurnUsername;
    }

    public Plateau getPlateau() {
        return plateau;
    }
}
