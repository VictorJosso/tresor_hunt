package Models;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

public class WarFog extends Game{

    //Attributs

    public int mode = 3;



    //Constructeur
    public WarFog(int x, int y, int tres, int holes, int maxPlayers, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, owner, mainHandler);
    }
}
