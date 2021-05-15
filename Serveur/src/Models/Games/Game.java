package Models.Games;

import Apps.ConnectionHandler;
import Models.Cases.Case;
import Models.Cases.CaseTresor;
import Models.Cases.CaseVide;
import Models.Client;
import Models.Plateau;
import Utils.Bot.Bot;
import Utils.ClientHandler;
import Utils.Coordinates;

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
    private int treasuresLeft;
    /**
     * The Players left.
     */
    protected int playersLeft;
    private boolean finie=false;

    /**
     * The Plateau.
     */
    protected Plateau plateau;
    /**
     * The Mode.
     */
    public int mode;
    /**
     * The Players.
     */
    protected CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();
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
        this.treasuresLeft=tres;

        // Pour ajouter un bot à la partie :
        // Bot bot = new Bot(mainHandler, this.id, x, y);
        // bot.register(this);

    }


    /**
     * Ask for name bot boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean askForName_BOT(String name){
        for (ClientHandler client: this.players){
            if (client.getUsername().equals(name)){
                return false;
            }
        }
        return true;
    }

    /**
     * Add player.
     *
     * @param client the client
     */
    public void addPlayer(ClientHandler client){
        broadcast("140 "+client.getUsername()+" JOINED");
        players.add(client);
        if(players.size() == this.maxPlayers){
            mainHandler.hideGame(this.id);
        }
        for (ClientHandler player : this.players){
            client.send("140 "+player.getUsername()+" JOINED");
        }
    }

    /**
     * Remove player.
     *
     * @param client the client
     */
    public void removePlayer(ClientHandler client){
        if (players.remove(client)){
            broadcastAmeliore("145 "+client.getUsername()+" LEFT");
        }
        if (players.size() == this.maxPlayers -1){
            mainHandler.showGame(this.id);
        }
    }

    /**
     * Request start boolean.
     *
     * @param requester the requester
     * @return the boolean
     */
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

    /**
     * Start request status.
     *
     * @param client the client
     * @param status the status
     */
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
                for (ClientHandler clientHandler: players){
                    clientHandler.newClient();
                    clientHandler.getClient().setGameRunning(this);
                }
                if (this.maxPlayers != -1 && this.robots){
                    for (int i = this.players.size(); i < this.maxPlayers; i++){
                        (new Bot(mainHandler, this.id, x, y)).register(this);
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                this.plateau = new Plateau(x, y, holes, this.treasures, (int) (1.5*x*y)/5, this);
                mainHandler.getAvailableGamesMap().remove(this.id);
                broadcast("153 GAME STARTED");
                this.startGame();
            } else {
                int nbMesages =(int) Math.ceil(((double) playersRefusedToStart.size())/5);
                broadcast("154 START ABORTED "+ nbMesages);
                for (int i = 0; i < nbMesages; i++){
                    StringBuilder message = new StringBuilder("154 MESS " + i + " PLAYER");
                    for (int j = 0; 5*i+j<playersRefusedToStart.size() && j < 5; j++){
                        message.append(" ").append(playersRefusedToStart.get(5 * i + j).getUsername());
                    }
                    broadcast(message.toString());
                }
            }
        }
    }

    /**
     * Send positions.
     *
     * @param requester the requester
     */
    public void sendPositions(ClientHandler requester){
        for(ClientHandler c : this.players){
            requester.send(("510 " + c.getUsername() + " POS " + c.getClient().getCoordonnees().getX() + " "+ c.getClient().getCoordonnees().getY()));
        }
    }

    /**
     * Start game.
     */
    protected void startGame(){
        System.out.println("NOMBRE DE JOUEURS : "+players.size());
        this.playersLeft=players.size();
    }

    /**
     * Broadcast.
     *
     * @param message the message
     */
    public void broadcast(String message){
        broadcast(message, null);
    }

    /**
     * Broadcast.
     *
     * @param message the message
     * @param except  the except
     */
    public void broadcast(String message, ClientHandler except){
        System.err.println("ON BROADCAST "+message);
        for (ClientHandler client: this.players){
            if(client != except) {
                client.send(message);
            }
        }
    }

    /**
     * Broadcast ameliore.
     *
     * @param message the message
     */
    public void broadcastAmeliore(String message){
        for (ClientHandler player: this.players){
            if (player.isGoodClient()){
                player.send(message);
            }
        }
    }

    /**
     * Move player int.
     *
     * @param client    the client
     * @param direction the direction
     * @return the int
     */
    public int movePlayer(ClientHandler client, String direction){
        if (!client.getClient().isAlive() || finie){
            return -1;
        }
        Coordinates c = client.getClient().getCoordonnees();
        switch (direction){
            case "GOUP":
                if(!plateau.horsLimite(c.getX(), c.getY()-1) && plateau.getCase(c.getX(),c.getY()-1).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToY(-1);
                } else {
                    return -1;
                }
                break;
            case "GODOWN":
                if(!plateau.horsLimite(c.getX(), c.getY()+1) && plateau.getCase(c.getX(),c.getY()+1).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToY(1);
                } else {
                    return -1;
                }
                break;
            case "GOLEFT":
                if(!plateau.horsLimite(c.getX()-1, c.getY()) && plateau.getCase(c.getX()-1,c.getY()).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToX(-1);
                } else {
                    return -1;
                }
                break;
            case "GORIGHT":
                if(!plateau.horsLimite(c.getX()+1, c.getY()) && plateau.getCase(c.getX()+1,c.getY()).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToX(1);
                } else {
                    return -1;
                }
                break;
        }

        Case currentCase = plateau.getCase(c.getX(), c.getY());
        currentCase.setPlayerOn(client);
        if(currentCase instanceof CaseVide){
            return 0;
        } else if (currentCase instanceof CaseTresor){
            int value = ((CaseTresor) currentCase).getValue();
            client.getClient().addScore(value);
            plateau.setCase(new CaseVide(currentCase.getX(),currentCase.getY()));
            client.getClient().addScore(value);
            treasuresLeft--;
            isFini();
            return value;
        } else{
            playersLeft--;
            System.out.println("Nombre de joueurs : "+playersLeft);
            isFini();
            return 1;
        }
    }

    public void sendTres() {}
    public void sendHoles( ){}
    public void sendWalls() {}
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
     * Gets plateau.
     *
     * @return the plateau
     */
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


    /**
     * Is fini.
     */

    private ClientHandler getFirstPlayerAlive(){
        for (ClientHandler player: this.players){
            if (player.getClient().isAlive()){
                return player;
            }
        }
        return null;
    }
    protected void isFini(){
        if (playersLeft <= 1 || treasuresLeft==0){
            ClientHandler best_player = getFirstPlayerAlive();
            assert best_player != null;
            for (ClientHandler client : this.players){
                if(client.getClient().getScore() > best_player.getClient().getScore() && client.getClient().isAlive()){
                    best_player = client;
                }
            }
            broadcast("530 " +best_player.getUsername()+ " WINS");
            finie=true;
        }
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public CopyOnWriteArrayList<ClientHandler> getPlayers() {
        return players;
    }
}
