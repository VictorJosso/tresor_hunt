package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Game.
 */
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
    /**
     * The Mode.
     */
    public int mode;
    private CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();
    private final boolean robots;


    /**
     * Instantiates a new Game.
     *
     * @param x           the x
     * @param y           the y
     * @param tres        the tres
     * @param holes       the holes
     * @param maxPlayers  the max players
     * @param robots      the robots
     * @param owner       the owner
     * @param mainHandler the main handler
     */
    public Game(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner,  ConnectionHandler mainHandler) {
        this.x=x;
        this.y=y;
        this.treasures=tres;
        this.holes=holes;
        this.maxPlayers = maxPlayers;
        this.owner= owner;
        this.mainHandler = mainHandler;
        this.id = mainHandler.registerGameId(this);
        this.robots = robots;
    }

    /**
     * Add player.
     *
     * @param client the client
     */
    public void addPlayer(ClientHandler client){
        broadcast("140 "+client.getClient().getUsername()+" JOINED");
        players.add(client);
        for (ClientHandler player : this.players){
            client.send("140 "+player.getClient().getUsername()+" JOINED");
        }
    }

    /**
     * Remove player.
     *
     * @param client the client
     */
    public void removePlayer(ClientHandler client){
        if (players.remove(client)){
            broadcastAmeliore("145 "+client.getClient().getUsername()+" LEFT");
        }
    }

    private void broadcast(String message){
        for (ClientHandler client: this.players){
            client.send(message);
        }
    }

    private void broadcastAmeliore(String message){
        for (ClientHandler player: this.players){
            if (player.isGoodClient()){
                player.send(message);
            }
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets treasures.
     *
     * @return the treasures
     */
    public int getTreasures() {
        return treasures;
    }

    /**
     * Gets holes.
     *
     * @return the holes
     */
    public int getHoles() {
        return holes;
    }

    /**
     * Gets max players.
     *
     * @return the max players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public ClientHandler getOwner() {
        return owner;
    }

    /**
     * Launch.
     */
    public void launch(){
        mainHandler.getAvailableGamesMap().remove(this.id);
    }

    /**
     * Is robots boolean.
     *
     * @return the boolean
     */
    public boolean isRobots() {
        return robots;
    }
}
