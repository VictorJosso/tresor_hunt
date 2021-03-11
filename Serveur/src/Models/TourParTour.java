package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

public class TourParTour extends Game{

    //Attributs

    public int mode = 2;



    //Constructeur
    public TourParTour(int x, int y, int tres, int holes, int maxPlayers, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, owner, mainHandler);
    }
}
