package views;

import Apps.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Config;

public class WelcomeController {

    private MainApp mainApp;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button parametresButton;

    @FXML
    private Button connectionButton;

    public WelcomeController() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private boolean validateUsername(String username){
        // TODO: VERIFIER AUPRES DU SERVEUR SI LE NOM EST DEJA PRIS
        return (!username.equals("Kevin") && username.length() > 3);
    }

    @FXML
    private void handleConnectionButonPressed(){
        String username = usernameTextField.getText();
        boolean isValid = validateUsername(username);
        if (isValid){
            mainApp.warnConfigDone(new Config(username));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getConfigStage());
            alert.setTitle("Erreur");
            alert.setHeaderText("Nom d'utilisateur non disponible");
            alert.setContentText("Soit le nom d'utilisateur est déjà pris, soit il fait moins de 4 caractères.");

            alert.showAndWait();
        }
    }
}
