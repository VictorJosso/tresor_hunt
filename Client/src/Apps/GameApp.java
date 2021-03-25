package Apps;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import models.Game.CaseMur;
import models.Partie;
import models.Plateau;
import utils.CallbackInstance;

import java.io.IOException;
import java.util.*;

public class GameApp {
    private MainApp mainApp;
    private Stage gameStage;
    private Stage leaderBoardStage;
    private Partie partie;
    private Plateau plateau;
    private GraphicsContext gc;
    private AnimationTimer timer;
    private int screenWidth;
    private int screenHeight;
    private int COEFF_IMAGE;

    private double dragOffsetX;
    private double dragOffsetY;

    public GameApp(MainApp mainApp, Partie partie) {
        this.mainApp = mainApp;
        this.partie = partie;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenHeight = (int) (screenBounds.getHeight()*0.9);
        screenWidth = (int) (screenBounds.getWidth()*0.85);
        int sizeX = screenWidth/ partie.getDimensionX();
        int sizeY = screenHeight/partie.getDimensionY();
        COEFF_IMAGE = Math.min(sizeX, sizeY);
        this.plateau = new Plateau(partie.getDimensionX(), partie.getDimensionY(), COEFF_IMAGE);

        mainApp.getConnectionHandler().registerCallback("421", plateau, CallbackInstance::getWalls);
        mainApp.getConnectionHandler().send("420 GETWALLS");
        mainApp.getConnectionHandler().registerCallback("401", plateau, CallbackInstance::getHoles);
        mainApp.getConnectionHandler().send("400 GETHOLES");
        mainApp.getConnectionHandler().registerCallback("411", plateau, CallbackInstance::getTresors);
        mainApp.getConnectionHandler().send("410 GETTREASURES");




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

        Canvas canvas = new Canvas(partie.getDimensionX()*this.COEFF_IMAGE, partie.getDimensionY()*this.COEFF_IMAGE);
        root.getChildren().add(canvas);

        this.gc = canvas.getGraphicsContext2D();


        timer = new AnimationTimer(){

            @Override
            public void handle(long l) {
                drawGame();
            }
        };
        timer.start();


        this.gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
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
                        mainApp.gameStageClosed();
                    }
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

        this.gameStage.setResizable(false);
        this.gameStage.show();
        this.leaderBoardStage.show();
        int enc = (int) (this.gameStage.getHeight() - partie.getDimensionY()*this.COEFF_IMAGE);
        this.leaderBoardStage.setY(y + enc);
    }


    protected void drawGame(){
        for(int x = 0; x < partie.getDimensionX(); x++){
            for(int y = 0; y < partie.getDimensionY(); y++){
                //gc.save();
                //gc.rotate(Arrays.asList(0, 90, 180, 270).get(new Random().nextInt(4)));
                gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x*this.COEFF_IMAGE, y*this.COEFF_IMAGE);
                //gc.restore();
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

}
