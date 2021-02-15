package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import models.Config;

/**
 * The type Param serveur controller.
 */
public class ParamServeurController {

    @FXML
    private TextField adresseServeurTextField;

    @FXML
    private Spinner<Integer> portServeurSpinner;

    @FXML
    private Button OKButton;

    @FXML
    private Button CancelButton;

    private WelcomeController parentController;


    /**
     * Gets parent controller.
     *
     * @return the parent controller
     */
    public WelcomeController getParentController() {
        return parentController;
    }

    /**
     * Sets parent controller.
     *
     * @param parentController the parent controller
     */
    public void setParentController(WelcomeController parentController) {
        this.parentController = parentController;
        this.adresseServeurTextField.setText(parentController.getConfig().getAdresseServeur());
    }

    @FXML
    private void handleOKButtonPressed(){
        // Cette méthode est appelée lorsque le bouton OK est pressé. Ce comportement est défini dans le fichier FXML
        // TODO: VERFIFIER QUE L'URL EST VALIDE ET LA TESTER
        Config config = new Config();
        config.setAdresseServeur(adresseServeurTextField.getText());
        config.setPortServeur(portServeurSpinner.getValue());
        this.parentController.setConfig(config);
        this.parentController.fermerParametresServeurFenetre();

    }

    @FXML
    private void handleCancelButtonPressed(){
        // Cette méthode est appelée lorsque le bouton Annuler est pressé. Ce comportement est défini dans le fichier FXML
        this.parentController.fermerParametresServeurFenetre();
    }
}
