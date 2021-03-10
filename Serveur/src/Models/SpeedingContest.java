package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

public class SpeedingContest extends Game{

    //Attributs

    public int mode = 1;


    //Constructeur
    public SpeedingContest(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
    }
}
