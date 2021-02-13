package Apps;

import views.HomeController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Partie;

public class MainApp extends Application {

    private Stage primaryStage;
    private ObservableList<Partie> partiesList = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chasse au trésor");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/home.fxml"));

        AnchorPane rootLayout = (AnchorPane) loader.load();
        Scene scene = new Scene(rootLayout);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        HomeController controller = loader.getController();
        controller.setMainApp(this);
    }

    public MainApp(){
        fetchPartiesList();

    }

    private void fetchPartiesList(){
        // TODO: RECUPERER LES PARTIES AUPRES DU SERVEUR
        partiesList.add(new Partie(1, "Victor", "Tour par tour", 40, 40, 10, 15, true));
        partiesList.add(new Partie(2, "Basile", "Speeding Contest", 30, 60, 3, 5, false));
        partiesList.add(new Partie(3, "Leah", "Brouillard de guerre", 60, 80, 30, 25, true));

    }

    public ObservableList<Partie> getPartiesList() {
        return partiesList;
    }
}
