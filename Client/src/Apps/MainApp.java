package Apps;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import models.Config;
import utils.CallbackInstance;
import utils.ConnectionHandler;
import utils.PartiesUpdater;
import utils.RecurrentServerRequest;
import views.HomeController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Partie;
import views.LobbyController;
import views.WelcomeController;
import views.LobbyController;

import java.util.ArrayList;
import java.util.Timer;

/**
 * The type Main app.
 */
public class MainApp extends Application {

    // Fenetre principale
    private Stage primaryStage;

    // Fenetre de saisie du nom d'utilisateur
    private Stage configStage;

    private Stage lobbyStage;

    // La liste des parties en cours. On utilise pas une liste classique car celle-ci permet de faire en sorte que l'interface graphique se mette a jour automatiquement.
    private ObservableList<Partie> partiesList = FXCollections.observableArrayList();

    // La configuration du serveur (adresse, port, nom d'utilisateur)
    private Config serverConfig = new Config();

    private ConnectionHandler connectionHandler;

    private Timer fetchPartiesListTimer;

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
        Font.loadFont(getClass().getResource("../Chilanka-Regular.ttf").toExternalForm(), 10);
        this.primaryStage = primaryStage;

        displayConfigStage();

    }

    /**
     * Instantiates a new Main app.
     */
    public MainApp(){
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (this.connectionHandler != null){
            this.connectionHandler.quitter();
        }
    }

    private void fetchPartiesList(){
        connectionHandler.registerCallback("121", new PartiesUpdater(this), CallbackInstance::parse);
        fetchPartiesListTimer = connectionHandler.registerRecurrentServerCall(new RecurrentServerRequest() {
            @Override
            public void run() {
                handler.send("120 GETLIST");
            }
        }, 1000);

        // On simule 3 parties que l'on peut rejoindre


    }

    /**
     * Update parties.
     *
     * @param list the list
     */
    public void updateParties(ArrayList<String> list){
        ArrayList<Integer> identifiants = new ArrayList<>();
        for (Partie partie : this.partiesList){
            identifiants.add(partie.getIdentifiant());
        }
        for (String message : list){
            String[] liste_commandes = message.split(" ");
            int id = Integer.parseInt(liste_commandes[4]);
            if (identifiants.contains(id)){
                identifiants.remove((Integer) id);
            } else {
                Partie nouvelle_partie = new Partie(Integer.parseInt(liste_commandes[4]), liste_commandes[11],
                        liste_commandes[5], Integer.parseInt(liste_commandes[6]), Integer.parseInt(liste_commandes[7]),
                        Integer.parseInt(liste_commandes[8]), Integer.parseInt(liste_commandes[9]), Integer.parseInt(liste_commandes[10]),
                        Boolean.parseBoolean(liste_commandes[12]));
                partiesList.add(nouvelle_partie);
            }
        }
        partiesList.removeIf(partie -> identifiants.contains(partie.getIdentifiant()));
    }

    private void resumeMainStageStartup(){
        try {
            // On nomme le stage.
            this.primaryStage.setTitle("Chasse au tr??sor");
            this.primaryStage.getIcons().add(new Image("logo2.png"));
            // On cr??e un nouveau Loader et on lui donne le fichier fxml qui correspond ?? la fen??tre d'acceuil
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/home.fxml"));

            // On r??cup??re le premier ??l??ment du XML. Il contient tous les autres. Ici c'est une AnchorPane (cf. views/hpme.fxml)
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // On cr??e une sc??ne avec notre ??l??ment racine en param??tre
            Scene scene = new Scene(rootLayout);
            // On affecte la sc??ne au stage principal et on l'affiche
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    connectionHandler.quitter();
                }
            });

            this.fetchPartiesList();

            // On r??cup??re l'instance du controlleur du loader. Il est sp??cifi?? dans le fichier fxml
            HomeController controller = loader.getController();
            // On indique au controlleur quelle est l'application principale (pour le callback)
            controller.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayConfigStage() throws Exception{

        // Meme proc??dure que pour resumeMainStageStartup
        this.configStage = new Stage();
        this.configStage.setTitle("Configuration du jeu");
        this.configStage.getIcons().add(new Image("logo2.png"));

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

    private void displayLobbyStage(Partie p) throws Exception {
        fetchPartiesListTimer.cancel();
        this.lobbyStage = new Stage();
        this.lobbyStage.setTitle("Lobby");
        this.lobbyStage.getIcons().add(new Image("logo2.png"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/lobby.fxml"));

        AnchorPane rootLayout = (AnchorPane) loader.load();
        Scene scene = new Scene (rootLayout);

        this.lobbyStage.setScene(scene);
        this.lobbyStage.show();

        LobbyController controller = loader.getController();
        controller.setMainApp(this, p);

        this.configStage.close();
    }

    /**
     * Start game.
     *
     * @param p the p
     */
    public void startGame(Partie p){
        GameApp game = new GameApp(this, p);
        game.launch();
        this.lobbyStage.close();
    }

    /**
     * Game stage closed.
     */
    public void gameStageClosed(){
        this.resumeMainStageStartup();
    }


    /**
     * Create server connection.
     *
     * @param config the config
     */
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
        // La configuration (serveur + username) est termin??e. On ferme la fenetre de configuration et on affiche la fen??tre principale en sauvegardant la configuration
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
        // G??re l'appui sur le bouton Quitter de l'inteface
        this.connectionHandler.quitter();
        this.primaryStage.close();
        this.partiesList.clear();
        this.displayConfigStage();
    }

    /**
     * Quit lobby.
     *
     * @throws Exception the exception
     */
    public void quitLobby() throws Exception{
        this.lobbyStage.close();
        this.resumeMainStageStartup();
    }

    /**
     * Join game lobby.
     *
     * @param p the p
     * @throws Exception the exception
     */
    public void joinGameLobby(Partie p) throws Exception {
        // G??re l'appui sur le bouton Rejoindre la partie de l'interface
        this.primaryStage.close();
        this.displayLobbyStage(p);
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

    /**
     * Gets connection handler.
     *
     * @return the connection handler
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
