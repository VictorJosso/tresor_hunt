package Utils.Bot;

import Apps.ConnectionHandler;
import Models.Client;
import Utils.ClientHandler;

import java.util.ArrayList;

public class Bot extends ClientHandler {

    private final ConnectionHandler mainApp;
    private Client client = new Client();
    private String username;
    private ArrayList<Integer> joinedGames;
    private final PlateauBot plateauBot;
    private final int gameId;

    /**
     * Instantiates a new Client handler.
     *
     * @param mainApp the main app
     */
    public Bot(ConnectionHandler mainApp, int gameId, int dimensionX, int dimensionY) {
        this.mainApp = mainApp;
        this.gameId = gameId;
        this.plateauBot = new PlateauBot(dimensionX, dimensionY);
    }

    private void illegalCommand(){

    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    public void newClient(){
        this.client = new Client();
    }

    /**
     * Send.
     *
     * @param message the message
     */
    public void send(String message){
        // Process des commandes de la partie ICI
    }



    /**
     * Close connection.
     */
    protected void closeConnection(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return true;
    }

    public void setLoggedIn(boolean loggedIn) {}

    public ArrayList<Integer> getJoinedGames() {
        return joinedGames;
    }

    public boolean isGoodClient(){
        return true;
    }
}
