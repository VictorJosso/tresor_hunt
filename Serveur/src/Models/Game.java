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
    private ConnectionHandler mainHandler;
    private ClientHandler owner;
    private CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();


    public Game(int x, int y, int tres, int holes, ClientHandler owner,  ConnectionHandler mainHandler) {
        this.x=x;
        this.y=y;
        this.treasures=tres;
        this.holes=holes;
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
}
