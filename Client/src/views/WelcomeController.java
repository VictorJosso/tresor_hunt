package views;

import Apps.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Config;

/**
 * The type Welcome controller.
 */
public class WelcomeController {

    private MainApp mainApp;
    private Config config = new Config();
    private Stage paramStage;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button parametresButton;

    @FXML
    private Button connectionButton;

    /**
     * Instantiates a new Welcome controller.
     */
    public WelcomeController() {
    }

    /**
     * Sets main app.
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private boolean validateUsername(String username){
        // TODO: VERIFIER AUPRES DU SERVEUR SI LE NOM EST DEJA PRIS
        return (!username.equals("Kevin") && username.length() > 3);
    }

    @FXML
    private void handleConnectionButtonPressed(){
        // Cette méthode est appelée lorsque le bouton Connexion est pressé. Ce comportement est défini dans le fichier FXML
        String username = usernameTextField.getText();
        boolean isValid = validateUsername(username);
        if (isValid){
            config.setUsername(username);
            mainApp.warnConfigDone(this.config);
        }
        else{
            // On crée un dialogue pour avertir l'utilisateur de son erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getConfigStage());
            alert.setTitle("Erreur");
            alert.setHeaderText("Nom d'utilisateur non disponible");
            alert.setContentText("Soit le nom d'utilisateur est déjà pris, soit il fait moins de 4 caractères.");

            alert.showAndWait();
        }
    }

//    @FXML
//    private void onKeyPressed(KeyEvent keyEvent){
//        if (keyEvent.getCode() == KeyCode.ENTER){
//            handleConnectionButtonPressed();
//        }
//    }

    @FXML
    private void handleParametresButtonPressed() throws Exception{
        // Cette méthode est appelée lorsque le bouton Paramètres est pressé. Ce comportement est défini dans le fichier FXML.
        // On crée une nouvelle fenêtre, pour la procédure voir le fichier Apps.ainApp
        this.paramStage = new Stage();
        this.paramStage.setTitle("Paramètres du serveur");

        FXMLLoader parametresServeurLoader = new FXMLLoader();
        parametresServeurLoader.setLocation(getClass().getResource("/views/paramServeur.fxml"));

        BorderPane parametresServeurLayout = (BorderPane) parametresServeurLoader.load();
        Scene parametresServeurScene = new Scene(parametresServeurLayout);

        this.paramStage.setScene(parametresServeurScene);
        this.paramStage.setResizable(false);

        ParamServeurController parametresServeurController = parametresServeurLoader.getController();
        parametresServeurController.setParentController(this);

        // Ces paramètres permettent de ne pas pouvoir interagir avec la fenêtre de configuration princpale (nom d'utilisateur) tant que celle du serveur est ouverte.
        this.paramStage.initModality(Modality.WINDOW_MODAL);
        this.paramStage.initOwner(mainApp.getConfigStage());
        this.paramStage.show();


    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Close server settings window.
     */
    public void fermerParametresServeurFenetre(){
        this.paramStage.close();
    }
}
