package Models.Games;

import Apps.ConnectionHandler;
import Models.Cases.Case;
import Models.Cases.CaseTresor;
import Models.Cases.CaseTrou;
import Models.Cases.CaseVide;
import Utils.ClientHandler;
import Utils.Coordinates;

/**
 * The type Speeding contest.
 */
public class SpeedingContest extends Game{

    //Attributs

    /**
     * Instantiates a new Speeding contest.
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
    public SpeedingContest(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
        this.mode = 1;
    }

    @Override
    public int movePlayer(ClientHandler client, String direction) {
        return super.movePlayer(client, direction);
    }
}
