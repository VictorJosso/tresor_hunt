package views;

import Apps.MainApp;
import com.sun.jdi.PrimitiveValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Partie;


public class LobbyController {

    private MainApp mainApp;

    private Partie partie;

    @FXML
    private Label modeJeuLabel;

    @FXML
    private Label dimensionsLabel;
    // X et Y ?

    @FXML
    private Label nombreTrousLabel;

    @FXML
    private Label nombreTresorsLabel;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // initialiser la partie
    }

    @FXML
    private void initialize() {
        // TODO : changer tableau (type) et remplir la colonne avec les joueurs de la partie
        modeJeuLabel.setText("Tour par Tour");
        dimensionsLabel.setText("10x10");
        nombreTrousLabel.setText("5");
        nombreTresorsLabel.setText("10");

    }
}
