package Apps;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Game.CaseMur;
import models.Partie;
import models.Plateau;
import utils.CallbackInstance;

import java.util.Optional;

public class GameApp {
    private MainApp mainApp;
    private Stage gameStage;
    private Partie partie;
    private Plateau plateau;
    private GraphicsContext gc;

    public GameApp(MainApp mainApp, Partie partie) {
        this.mainApp = mainApp;
        this.partie = partie;
        this.plateau = new Plateau(partie.getDimensionX(), partie.getDimensionY());

        mainApp.getConnectionHandler().registerCallback("421", plateau, CallbackInstance::getWalls);
        mainApp.getConnectionHandler().send("420 GETWALLS");
    }

    public void launch(){
        this.gameStage = new Stage();
        Group root = new Group();
        Scene gameScene = new Scene(root);
        gameStage.setScene(gameScene);

        Canvas canvas = new Canvas(partie.getDimensionX()*20, partie.getDimensionY()*20);
        root.getChildren().add(canvas);

        this.gc = canvas.getGraphicsContext2D();


        new AnimationTimer(){

            @Override
            public void handle(long l) {
                drawGame();
            }
        }.start();


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
                        mainApp.gameStageClosed();
                    }
                }

            }
        });

        this.gameStage.setResizable(false);
        this.gameStage.show();
    }


    protected void drawGame(){
        for(int x = 0; x < partie.getDimensionX(); x++){
            for(int y = 0; y < partie.getDimensionY(); y++){
                    gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x*20, y*20);
            }
        }
    }

}
