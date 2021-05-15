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
import models.Partie;
import models.Plateau;
import utils.CallbackInstance;
import utils.ConnectionHandler;
import utils.ImageCrop;
import utils.LeaderBoardItem;
import views.LeaderBoardController;

import java.io.IOException;
import java.util.*;

/**
 * The type Game app.
 */
public class GameApp {
    /**
     * The Main app.
     */
    public MainApp mainApp;
    private Stage gameStage;
    private Stage leaderBoardStage;
    private final Partie partie;
    private final Plateau plateau;
    /**
     * The Gc.
     */
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

    private int infos_gathered = 0;
    private int total_infos_to_get = 0;
    private int number_of_info_types_requested = 0;

    /**
     * Instantiates a new Game app.
     *
     * @param mainApp the main app
     * @param partie  the partie
     */
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
        mainApp.getConnectionHandler().registerCallback("666", plateau, CallbackInstance::handleMoveDead);
        mainApp.getConnectionHandler().registerCallback("902", plateau, CallbackInstance::handleNotYourTurn);

    }

    public void declareCallBacksMaybe(){
        infos_gathered ++;
        System.err.println("On a recupéré "+infos_gathered+" informations");
        if (infos_gathered == total_infos_to_get && number_of_info_types_requested == 3){
            mainApp.getConnectionHandler().registerCallback("500", plateau, CallbackInstance::handleTurnChanged, true);
            mainApp.getConnectionHandler().registerCallback("510", plateau, CallbackInstance::updatePlayerPosition, true);
            mainApp.getConnectionHandler().registerCallback("511", plateau, CallbackInstance::updatePlayerTresor, true);
            mainApp.getConnectionHandler().registerCallback("520", plateau, CallbackInstance::declareDead, true);
            mainApp.getConnectionHandler().registerCallback("530", plateau, CallbackInstance::partieFinie, true);
        }
    }

    public void tellYouNeedSomeInfos(int nb){
        this.total_infos_to_get += nb;
        this.number_of_info_types_requested ++;
        System.err.println("On a besoin de récupérer "+ total_infos_to_get+ " informations");
    }

    /**
     * Launch.
     */
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

        Canvas canvas = new Canvas(partie.getDimensionX()*this.COEFF_IMAGE, partie.getDimensionY()*this.COEFF_IMAGE);
        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);

        this.gc = canvas.getGraphicsContext2D();


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
        mainApp.getConnectionHandler().send("410 GETTREASURES");
        mainApp.getConnectionHandler().send("400 GETHOLES");
        mainApp.getConnectionHandler().send("420 GETWALLS");
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
        this.mainApp.getConnectionHandler().releaseCallback("530");
        this.mainApp.getConnectionHandler().releaseCallback("666");
        this.mainApp.getConnectionHandler().releaseCallback("902");
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


    private void handleKeyPressed(KeyEvent keyEvent){
        long currentTime = System.currentTimeMillis();
        Long lastCall = this.keyEvents.get(keyEvent.getCode());
        if (lastCall == null || (currentTime - lastCall) > 250){
            this.keyEvents.put(keyEvent.getCode(), currentTime);
            this.processKeyEvent(keyEvent);
        } else {
            keyEvent.consume();
        }

    }


    /**
     * Draw game.
     */
    protected void drawGame(){
        for(int x = 0; x < partie.getDimensionX(); x++){
            for(int y = 0; y < partie.getDimensionY(); y++){
                gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x*this.COEFF_IMAGE, y*this.COEFF_IMAGE);
            }
        }
    }

    /**
     * Draw players.
     */
    protected void drawPlayers() {
        for(String name : plateau.getCoordonneesJoueurs().keySet()) {
            String nameToDraw = name;
            if (!plateau.getCoordonneesJoueurs().get(name).isAlive()){
                if(!name.equals(mainApp.getServerConfig().getUsername()) && plateau.getCoordonneesJoueurs().get(name).getKillDate() + 1000 > System.currentTimeMillis()){
                    gc.drawImage(flammesImage, plateau.getCoordonneesJoueurs().get(name).getX()*COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY()*COEFF_IMAGE);
                } else {
                    continue;
                }
            } else {
                if (this.mainApp.getServerConfig().getUsername().equals(name)) {
                    gc.drawImage(plateau.getListeImages().get(7), plateau.getCoordonneesJoueurs().get(name).getX() * COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY() * COEFF_IMAGE);
                    nameToDraw = "Moi";
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

    /**
     * Register draw on top.
     *
     * @param imageCrop the image crop
     * @param duration  the duration
     */
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

    /**
     * Handle mouse pressed.
     *
     * @param e the e
     */
    protected void handleMousePressed(MouseEvent e)
    {
        this.dragOffsetX = e.getScreenX() - this.leaderBoardStage.getX();
        this.dragOffsetY = e.getScreenY() - this.leaderBoardStage.getY();
    }

    /**
     * Handle mouse dragged.
     *
     * @param e the e
     */
    protected void handleMouseDragged(MouseEvent e)
    {
        this.leaderBoardStage.setX(e.getScreenX() - this.dragOffsetX);
        this.leaderBoardStage.setY(e.getScreenY() - this.dragOffsetY);
    }

    /**
     * Gets directions.
     *
     * @return the directions
     */
    public ArrayList<KeyCode> getDirections() {
        return directions;
    }

    /**
     * Get server config config.
     *
     * @return the config
     */
    public Config getServerConfig(){
        return mainApp.getServerConfig();
    }

    /**
     * Get connection handler connection handler.
     *
     * @return the connection handler
     */
    public ConnectionHandler getConnectionHandler(){
        return mainApp.getConnectionHandler();
    }

    /**
     * Gets leader board items.
     *
     * @return the leader board items
     */
    public ObservableList<LeaderBoardItem> getLeaderBoardItems() {
        return leaderBoardItems;
    }

    /**
     * Gets screen width.
     *
     * @return the screen width
     */
    public int getScreenWidth() {
        return COEFF_IMAGE * partie.getDimensionX();
    }

    /**
     * Gets screen height.
     *
     * @return the screen height
     */
    public int getScreenHeight() {
        return COEFF_IMAGE * partie.getDimensionY();
    }

    /**
     * Sets player turn username.
     *
     * @param playerTurnUsername the player turn username
     */
    public void setPlayerTurnUsername(String playerTurnUsername) {
        this.playerTurnUsername = playerTurnUsername;
    }

    /**
     * Gets timer.
     *
     * @return the timer
     */
    public AnimationTimer getTimer() {
        return timer;
    }

    /**
     * Gets game stage.
     *
     * @return the game stage
     */
    public Stage getGameStage() {
        return gameStage;
    }

    /**
     * End game.
     */
    public void endGame(){
        leaderBoardStage.close();
        gameStage.close();
        releaseAllCallbacks();
        mainApp.gameStageClosed();
    }
}
