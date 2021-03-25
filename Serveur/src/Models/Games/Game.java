package Models.Games;

import Apps.ConnectionHandler;
import Models.Client;
import Models.Plateau;
import Utils.ClientHandler;

import java.util.ArrayList;
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
    private CopyOnWriteArrayList<ClientHandler> playersRefusedToStart = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<ClientHandler> playersOkToStart = new CopyOnWriteArrayList<>();
    private final ConnectionHandler mainHandler;
    private final ClientHandler owner;

    private Plateau plateau;
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

    public boolean requestStart(ClientHandler requester){
        if (requester == owner){
            this.playersOkToStart.clear();
            this.playersRefusedToStart.clear();
            broadcast("152 START REQUESTED");
            return true;
        } else {
            return false;
        }
    }

    public void startRequestStatus(ClientHandler client, boolean status){
        if (playersOkToStart.contains(client) || playersRefusedToStart.contains(client)){
            return;
        }
        if (status){
            playersOkToStart.add(client);
        } else {
            playersRefusedToStart.add(client);
        }
        if (playersOkToStart.size() + playersRefusedToStart.size() == players.size()){
            if (playersRefusedToStart.size() == 0){
                this.plateau = new Plateau(x, y, holes, this.treasures, (int) (1.5*x*y)/5, this);
                broadcast("153 GAME STARTED");
            } else {
                int nbMesages =(int) Math.ceil(((double) playersRefusedToStart.size())/5);
                broadcast("154 START ABORTED "+ nbMesages);
                for (int i = 0; i < nbMesages; i++){
                    StringBuilder message = new StringBuilder("154 MESS " + i + " PLAYER");
                    for (int j = 0; 5*i+j<playersRefusedToStart.size() && j < 5; j++){
                        message.append(" ").append(playersRefusedToStart.get(5 * i + j).getClient().getUsername());
                    }
                    broadcast(message.toString());
                }
            }
        }
    }

    public void sendPositions(ClientHandler requester){
        for(ClientHandler c : this.players){
            requester.send(("510 " + c.getClient().getUsername() + " POS " + c.getCoordonnees().getX() + " "+ c.getCoordonnees().getY()));
        }
    }

    public void broadcast(String message){
        System.err.println("ON BROADCAST "+message);
        for (ClientHandler client: this.players){
            client.send(message);
        }
    }

    public void broadcastAmeliore(String message){
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

    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Is robots boolean.
     *
     * @return the boolean
     */
    public boolean isRobots() {
        return robots;


    }


    public CopyOnWriteArrayList<ClientHandler> getPlayers() {
        return players;
    }
}
