package views;

import Apps.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Partie;

/**
 * The type Home controller.
 */
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

    @FXML
    private Label partiesOuvertesLabel;

    @FXML
    private Label creationPartiesLabel;

    /**
     * Sets main app, and fills UI with parties list and config
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        partiesEnCoursTableView.setItems(mainApp.getPartiesList());
        partiesOuvertesLabel.setText("Parties ouvertes à l'inscription sur "+mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());
        creationPartiesLabel.setText("Créer une partie sur " + mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());
    }

    /**
     * Instantiates a new Home controller.
     */
    public HomeController() {
    }
    
    @FXML
    private void initialize() {
        // Cette methode est appelée automatiquement par JavaFX lors de la création de la fénêtre.
        // Les instructions permettent d'affecter les propriétés de l'élément Parties à chaque colonne du tableView
        identifiantTableColumn.setCellValueFactory(cellData -> cellData.getValue().identifiantProperty().asObject());
        modeTableColumn.setCellValueFactory(cellData -> cellData.getValue().modeDeJeuProperty());
        createurTableColumn.setCellValueFactory(cellData -> cellData.getValue().createurProperty());
        dimensionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDimensionX()+"x"+cellData.getValue().getDimensionY()));
        modeDeJeuChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue){
                if (newValue.equals("Speeding contest")){
                    robotsCheckBox.setSelected(false);
                    robotsCheckBox.setDisable(true);
                } else {
                    robotsCheckBox.setDisable(false);
                }
            }
        });
    }

    @FXML
    private void handleQuitButtonClick(){
        // Cette methode est appelée lorsque l'on clique sur le bouton quitter. Ce comportement est défini dans le fichier FXML
        try {
            this.mainApp.quitMainScreen();
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }

    @FXML
    private void handlejoinButtonClick() {
        // Cette méthode est appelée lorsque l'on clique sur le bouton rejoindre la partie. Comprtement défini dans le fichier XML
        try {
            this.mainApp.joinGameLobby();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
