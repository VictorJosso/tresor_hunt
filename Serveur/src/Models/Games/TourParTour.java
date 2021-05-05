package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

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

    @Override
    public int movePlayer(ClientHandler client, String direction) {
        if (client == this.stillAlivePlayers.get(currentPlayerIndex)){
            int res = super.movePlayer(client, direction);
            if (res == 1) {
                this.stillAlivePlayers.remove(client);
            }
            if (res != -1) {
                currentPlayerIndex += 1;
                currentPlayerIndex %= this.stillAlivePlayers.size();
                broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");
            }
            return res;
        } else {
            return -2;
        }
    }
}
