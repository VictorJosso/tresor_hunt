package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

/**
 * The type War fog.
 */
public class WarFog extends Game{

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
}
