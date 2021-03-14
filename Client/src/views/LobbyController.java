package views;

import Apps.MainApp;
import com.sun.jdi.PrimitiveValue;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.Partie;
import utils.CallbackInstance;


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

        mainApp.getConnectionHandler().send("130 JOIN "+partie.getIdentifiant());


        // initialiser la partie
        populateScreen();

    }

    @FXML
    private void initialize() {
        // TODO : changer tableau (type) et remplir la colonne avec les joueurs de la partie
        playersInListView.setMouseTransparent( true );
        playersInListView.setFocusTraversable( false );
    }

    private void populateScreen(){
        modeJeuLabel.setText(this.partie.getModeDeJeu().equals("1") ? "Speeding contest" : this.partie.getModeDeJeu().equals("2") ? "Tour par tour" : "Brouillard de guerre");
        dimensionsLabel.setText(this.partie.getDimensionX()+"x"+this.partie.getDimensionY());
        nombreTrousLabel.setText(String.valueOf(this.partie.getNombreDeTrous()));
        nombreTresorsLabel.setText(String.valueOf(this.partie.getNombreDeTresors()));
        playersInListView.setItems(partie.getPlayersNames());
        lancerPartieButton.requestFocus();
        leftAnchorPane.maxWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        leftAnchorPane.minWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        identifantLabel.setText(String.valueOf(this.partie.getIdentifiant()));

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
        this.partie.getPlayersNames().add(s.split(" ")[1]);
    }

    @Override
    public void removePlayer(String s) {
        this.partie.getPlayersNames().remove(s.split(" ")[1]);
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
}