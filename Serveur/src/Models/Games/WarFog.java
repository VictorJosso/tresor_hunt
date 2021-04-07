package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;
import java.util.HashMap;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type War fog.
 */
public class WarFog extends Game {

    private int currentPlayerIndex;
    private CopyOnWriteArrayList<ClientHandler> stillAlivePlayers;
    //private HashMap<ClientHandler,Integer> seeHoles; // les tours restant pendants lesquels on affiche les trous
    //private HashMap<ClientHandler,>;
    private int resteToursPieges;
    private int resteToursMap;

    //Attributs

    /**
     * The Mode.
     */
    public int mode = 3;


    /**
     * Instantiates a new War fog.
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
    public WarFog(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
        this.mode = 3;
    }

    @Override
    protected void startGame() {
        this.stillAlivePlayers = new CopyOnWriteArrayList<>(this.players);
        Collections.shuffle(this.stillAlivePlayers);
        currentPlayerIndex = (int) (Math.random() * this.stillAlivePlayers.size());
        broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");
    }


    @Override
    public int movePlayer(ClientHandler client, String direction) {
        if (client == this.stillAlivePlayers.get(currentPlayerIndex)) {
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

    public void revealHoles(ClientHandler client) {
        // assurer le paiement du client
        // et révéler pièges pendant 5 tours

    }

    public void revealMap(ClientHandler client) {
        // assurer le paiement du client
        // et révéler partie de la map (choisie arbitrairement) pendant 3 tours
    }

}

