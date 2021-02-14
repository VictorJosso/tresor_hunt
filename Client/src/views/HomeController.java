package views;

import Apps.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Partie;

public class HomeController {

    private MainApp mainApp;

    @FXML
    private TableView<Partie> partiesEnCoursTableView;

    @FXML
    private TableColumn<Partie, Integer> identifiantTableColumn;

    @FXML
    private TableColumn<Partie, String> modeTableColumn;

    @FXML
    private TableColumn<Partie, String> createurTableColumn;

    @FXML
    private TableColumn<Partie, String> dimensionTableColumn;

    @FXML
    private Button rejoindrePartieButton;

    @FXML
    private Button quitterButton;

    @FXML
    private Button creationPartieButton;

    @FXML
    private ChoiceBox<String> modeDeJeuChoiceBox;

    @FXML
    private ChoiceBox<String> nombreDeTresorsChoiceBox;

    @FXML
    private Spinner<Integer> dimensionsXSpinner;

    @FXML
    private Spinner<Integer> dimensionsYSpinner;

    @FXML
    private Spinner<Integer> nombreDeTrousSpinner;

    @FXML
    private CheckBox robotsCheckBox;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        partiesEnCoursTableView.setItems(mainApp.getPartiesList());
    }

    public HomeController() {
    }
    
    @FXML
    private void initialize() {
        identifiantTableColumn.setCellValueFactory(cellData -> cellData.getValue().identifiantProperty().asObject());
        modeTableColumn.setCellValueFactory(cellData -> cellData.getValue().modeDeJeuProperty());
        createurTableColumn.setCellValueFactory(cellData -> cellData.getValue().createurProperty());
        dimensionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDimensionX()+"x"+cellData.getValue().getDimensionY()));
    }

    @FXML
    private void handleQuitButtonClick(){
        try {
            this.mainApp.quitMainScreen();
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }
}
