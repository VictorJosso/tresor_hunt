package views;

import Apps.MainApp;
import com.sun.jdi.PrimitiveValue;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.Partie;
import utils.CallbackInstance;

import java.util.ArrayList;


/**
 * The type Lobby controller.
 */
public class LobbyController extends CallbackInstance {

    private MainApp mainApp;

    private Partie partie;

    @FXML
    private Label statusLabel;

    @FXML
    private Button quitterButton;

    @FXML
    private Button lancerPartieButton;

    @FXML
    private ListView<String> playersInListView;

    @FXML
    private Label modeJeuLabel;

    @FXML
    private Label dimensionsLabel;
    // X et Y ?

    @FXML
    private Label nombreTrousLabel;

    @FXML
    private Label nombreTresorsLabel;

    @FXML
    private Label identifantLabel;

    @FXML
    private SplitPane lobbySplitPane;

    @FXML
    private AnchorPane leftAnchorPane;

    @FXML
    private CheckBox readyStatusCheckBox;

    private ArrayList<String> playersRefusedToStartGame = new ArrayList<>();
    private boolean hasPressedStartButton = false;

    /**
     * Sets main app.
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp, Partie p) {
        this.mainApp = mainApp;
        this.partie = p;

        this.partie.getPlayersNames().clear();

        mainApp.getConnectionHandler().registerCallback("140", this, CallbackInstance::addPlayer);
        mainApp.getConnectionHandler().registerCallback("131", this, CallbackInstance::updateConnexionStatus);
        mainApp.getConnectionHandler().registerCallback("145", this, CallbackInstance::removePlayer);

        mainApp.getConnectionHandler().registerCallback("152", this, CallbackInstance::updateStartGameStatus);

        mainApp.getConnectionHandler().registerCallback("153", this, CallbackInstance::gameStart);
        mainApp.getConnectionHandler().registerCallback("154", this, CallbackInstance::gameStartAborted);

        mainApp.getConnectionHandler().send("130 JOIN "+partie.getIdentifiant());


        // initialiser la partie
        populateScreen();

    }

    @FXML
    private void initialize() {
        playersInListView.setMouseTransparent( true );
        playersInListView.setFocusTraversable( false );
    }

    private void populateScreen(){
        modeJeuLabel.setText(this.partie.getModeDeJeu().equals("1") ? "Speeding contest" : this.partie.getModeDeJeu().equals("2") ? "Tour par tour" : "Brouillard de guerre");
        dimensionsLabel.setText(this.partie.getDimensionX()+"x"+this.partie.getDimensionY());
        nombreTrousLabel.setText(String.valueOf(this.partie.getNombreDeTrous()));
        nombreTresorsLabel.setText(String.valueOf(this.partie.getNombreDeTresors()));
        playersInListView.setItems(partie.getPlayersNames());
        playersInListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ColoredListCellFormat();
            }
        });
        lancerPartieButton.requestFocus();
        leftAnchorPane.maxWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        leftAnchorPane.minWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        identifantLabel.setText(String.valueOf(this.partie.getIdentifiant()));

        lancerPartieButton.setDisable(!partie.isCreator());
        readyStatusCheckBox.setSelected(partie.isCreator());
        readyStatusCheckBox.setDisable(partie.isCreator());

    }
    @FXML
    private void handleQuitButtonClick(){
        if(this.mainApp.getServerConfig().isServeurAmeliore()){
            this.mainApp.getConnectionHandler().send("135 LEAVE "+this.partie.getIdentifiant());
        }
        // Cette methode est appelée lorsque l'on clique sur le bouton quitter. Ce comportement est défini dans le fichier FXML
        try {
            this.mainApp.quitLobby();
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }

    @Override
    public void addPlayer(String s) {
        Platform.runLater(() -> {
            this.partie.getPlayersNames().add(s.split(" ")[1]);
        });
    }

    @Override
    public void removePlayer(String s) {
        Platform.runLater(() -> {
            this.partie.getPlayersNames().remove(s.split(" ")[1]);
        });
    }

    @Override
    public void updateConnexionStatus(String s) {
        if (s.split(" ")[1].equals("MAP") && s.split(" ")[3].equals("JOINED")){
            Platform.runLater(() -> {
                statusLabel.setText("Connecté");
                statusLabel.setTextFill(Color.web("#008000"));
            });
        }
    }

    @FXML
    private void startGameButtonPressed(){
        hasPressedStartButton = true;
        this.lancerPartieButton.setDisable(true);
        statusLabel.setText("En attente de confirmation des autres joueurs...");
        statusLabel.setTextFill(Color.web("#800000"));

        this.mainApp.getConnectionHandler().send("150 REQUEST START");
    }

    @Override
    public void updateStartGameStatus(String s) {
        this.mainApp.getConnectionHandler().send("152 START "+ ((readyStatusCheckBox.isSelected()) ? "YES" : "NO"));
    }

    @Override
    public void gameStart(String s) {
        Platform.runLater(() -> {
            this.mainApp.startGame(this.partie);
        });
    }

    @Override
    public void gameStartAborted(String s) {
        String[] command = s.split(" ");
        if(!command[2].equals("ABORTED")) {
            for (int i = 4; i < command.length; i += 3) {
                playersRefusedToStartGame.add(command[i]);
                playersInListView.refresh();

            }
        } else {
            playersRefusedToStartGame.clear();
            lancerPartieButton.setDisable(false);
            Platform.runLater(() -> {
                statusLabel.setText("Connecté");
                statusLabel.setTextFill(Color.web("#008000"));
            });
        }

    }

    public class ColoredListCellFormat extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(item);
            if (item == null || !hasPressedStartButton){
                setStyle("");
            } else {
                setStyle("-fx-background-color: "+((playersRefusedToStartGame.contains(item))?"red;":"green;")+" -fx-text-fill: white;");
            }
            //setBackground(new Background( new BackgroundFill(playersRefusedToStartGame.contains(item) ? Color.RED:Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}