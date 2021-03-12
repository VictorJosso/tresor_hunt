package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

public class TourParTour extends Game{

    //Attributs


    //Constructeur
    public TourParTour(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
        this.mode = 2;
    }
}
