package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

/**
 * The type Tour par tour.
 */
public class TourParTour extends Game{

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
}
