package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;
import Utils.Coordinates;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Tour par tour.
 */
public class TourParTour extends Game{

    private int currentPlayerIndex;
    private CopyOnWriteArrayList<ClientHandler> stillAlivePlayers;


    //Attributs


    /**
     * Instantiates a new Tour par tour.
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
//Constructeur
    public TourParTour(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
        this.mode = 2;
    }

    @Override
    protected void startGame() {
        super.startGame();
        this.stillAlivePlayers = new CopyOnWriteArrayList<>(this.players);
        System.err.println("stillAlivePlayers size : "+this.stillAlivePlayers.size());
        Collections.shuffle(this.stillAlivePlayers);
        currentPlayerIndex = (int) (Math.random() * this.stillAlivePlayers.size());
        broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");
    }

    private boolean checkIfBlocked(ClientHandler player){
        Coordinates coordinates = player.getClient().getCoordonnees().copy();
        if (this.plateau.getCase(coordinates.getX()+1, coordinates.getY()).isFree() ||
        this.plateau.getCase(coordinates.getX()-1, coordinates.getY()).isFree() ||
        this.plateau.getCase(coordinates.getX(), coordinates.getY()+1).isFree() ||
        this.plateau.getCase(coordinates.getX(), coordinates.getY()-1).isFree()){
            return false;
        }
        return true;
    }

    private int killBlockedPlayers(){
        int died = 0;
        for (ClientHandler player: this.stillAlivePlayers){
            if (checkIfBlocked(player)){
                if (this.stillAlivePlayers.indexOf(player) < this.currentPlayerIndex) {
                    died++;
                }
                broadcast("520 "+player.getUsername()+" DIED", player);
                player.send("666 MOVE HOLE DEAD");
                this.stillAlivePlayers.remove(player);
                this.playersLeft--;
                isFini();
            }
        }
        System.out.println("On a tu?? "+died+" joueurs");
        return died;
    }

    private void nextIfBlocked(){
        if (checkIfBlocked(this.stillAlivePlayers.get(this.currentPlayerIndex))){
            this.currentPlayerIndex++;
            currentPlayerIndex %= this.stillAlivePlayers.size();
            nextIfBlocked();
        }
    }

    @Override
    public int movePlayer(ClientHandler client, String direction) {
        if (client == this.stillAlivePlayers.get(currentPlayerIndex)){
            int res = super.movePlayer(client, direction);
            //this.currentPlayerIndex -= this.killBlockedPlayers();
            if (res == 1) {
                this.stillAlivePlayers.remove(client);
                this.currentPlayerIndex -= 1;
            }
            if (res != -1) {
                currentPlayerIndex += 1;
                currentPlayerIndex %= this.stillAlivePlayers.size();
                nextIfBlocked();
                broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");
            }
            return res;
        } else {
            return -2;
        }
    }
}
