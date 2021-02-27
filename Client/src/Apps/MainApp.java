package Apps;

import javafx.scene.layout.BorderPane;
import models.Config;
import utils.ConnectionHandler;
import views.HomeController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Partie;
import views.WelcomeController;

/**
 * The type Main app.
 */
public class MainApp extends Application {

    // Fenetre principale
    private Stage primaryStage;

    // Fenetre de saisie du nom d'utilisateur
    private Stage configStage;

    // La liste des parties en cours. On utilise pas une liste classique car celle-ci permet de faire en sorte que l'interface graphique se mette a jour automatiquement.
    private ObservableList<Partie> partiesList = FXCollections.observableArrayList();

    // La configuration du serveur (adresse, port, nom d'utilisateur)
    private Config serverConfig = new Config();

    private ConnectionHandler connectionHandler;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        displayConfigStage();

    }

    /**
     * Instantiates a new Main app.
     */
    public MainApp(){
        // On récupère les parties disponibles. A terme, cette fonction doit être appelée après que la configuration soit terminée
        fetchPartiesList();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (this.connectionHandler != null){
            this.connectionHandler.quitter();
        }
    }

    private void fetchPartiesList(){
        // TODO: RECUPERER LES PARTIES AUPRES DU SERVEUR

        // On simule 3 parties que l'on peut rejoindre
        partiesList.add(new Partie(1, "Victor", "Tour par tour", 40, 40, 10, 15, true));
        partiesList.add(new Partie(2, "Basile", "Speeding Contest", 30, 60, 3, 5, false));
        partiesList.add(new Partie(3, "Leah", "Brouillard de guerre", 60, 80, 30, 25, true));

    }

    private void resumeMainStageStartup(){
        try {
            // On nomme le stage.
            this.primaryStage.setTitle("Chasse au trésor");

            // On crée un nouveau Loader et on lui donne le fichier fxml qui correspond à la fenêtre d'acceuil
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/home.fxml"));

            // On récupère le premier élément du XML. Il contient tous les autres. Ici c'est une AnchorPane (cf. views/hpme.fxml)
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // On crée une scène avec notre élément racine en paramètre
            Scene scene = new Scene(rootLayout);
            // On affecte la scène au stage principal et on l'affiche
            this.primaryStage.setScene(scene);
            this.primaryStage.show();

            // On récupère l'instance du controlleur du loader. Il est spécifié dans le fichier fxml
            HomeController controller = loader.getController();
            // On indique au controlleur quelle est l'application principale (pour le callback)
            controller.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayConfigStage() throws Exception{

        // Meme procédure que pour resumeMainStageStartup
        this.configStage = new Stage();
        this.configStage.setTitle("Configuration du jeu");

        FXMLLoader configLoader = new FXMLLoader();
        configLoader.setLocation(getClass().getResource("/views/welcome.fxml"));

        BorderPane configLayout = (BorderPane) configLoader.load();
        Scene configScene = new Scene(configLayout);

        this.configStage.setScene(configScene);
        this.configStage.setResizable(false);
        this.configStage.show();

        WelcomeController configController = configLoader.getController();
        configController.setMainApp(this);
        configController.setConfig(serverConfig);

    }

    public void createServerConnection(Config config){
        this.connectionHandler = new ConnectionHandler(config);
        this.connectionHandler.start();
    }

    /**
     * Warn config done. Close config window
     *
     * @param config the config
     */
    public void warnConfigDone(Config config){
        // La configuration (serveur + username) est terminée. On ferme la fenetre de configuration et on affiche la fenêtre principale en sauvegardant la configuration
        this.serverConfig = config;
        this.configStage.close();
        this.resumeMainStageStartup();

    }

    /**
     * Quit main screen and reopen configuration screen.
     *
     * @throws Exception an exception if displayConfigStage fails
     */
    public void quitMainScreen() throws Exception{
        // Gère l'appui sur le bouton Quitter de l'inteface
        // TODO: AVERTIR LE SERVEUR DE LA DECONNEXION (LIBERATION DU USERNAME)
        this.primaryStage.close();
        this.displayConfigStage();
    }

    // Getters & setters

    /**
     * Gets parties list.
     *
     * @return the parties list
     */
    public ObservableList<Partie> getPartiesList() {
        return partiesList;
    }

    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets config stage.
     *
     * @return the config stage
     */
    public Stage getConfigStage() {
        return configStage;
    }

    /**
     * Gets server config.
     *
     * @return the server config
     */
    public Config getServerConfig() {
        return serverConfig;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
