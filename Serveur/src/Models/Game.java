package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Game {

    //Attributs
    private final int x;
    private final int y;
    private final int treasures;
    private final int holes;
    private final int id;
    private final int maxPlayers;
    private ConnectionHandler mainHandler;
    private ClientHandler owner;
    public int mode;
    private CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();


    public Game(int x, int y, int tres, int holes, int maxPlayers, ClientHandler owner,  ConnectionHandler mainHandler) {
        this.x=x;
        this.y=y;
        this.treasures=tres;
        this.holes=holes;
        this.maxPlayers = maxPlayers;
        this.owner= owner;
        this.mainHandler = mainHandler;
        this.id = mainHandler.registerGameId(this);
    }

    public void addPlayer(ClientHandler client){
        players.add(client);
    }

    public void removePlayer(ClientHandler client){
        players.remove(client);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTreasures() {
        return treasures;
    }

    public int getHoles() {
        return holes;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void launch(){
        mainHandler.getAvailableGamesMap().remove(this.id);
    }
}
