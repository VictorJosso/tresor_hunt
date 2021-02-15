package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import models.Config;

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


    public WelcomeController getParentController() {
        return parentController;
    }

    public void setParentController(WelcomeController parentController) {
        this.parentController = parentController;
        this.adresseServeurTextField.setText(parentController.getConfig().getAdresseServeur());
    }

    @FXML
    private void handleOKButtonPressed(){
        // TODO: VERFIFIER QUE L'URL EST VALIDE ET LA TESTER
        Config config = new Config();
        config.setAdresseServeur(adresseServeurTextField.getText());
        config.setPortServeur(portServeurSpinner.getValue());
        this.parentController.setConfig(config);
        this.parentController.fermerParametresServeurFenetre();

    }

    @FXML
    private void handleCancelButtonPressed(){
        this.parentController.fermerParametresServeurFenetre();
    }
}
