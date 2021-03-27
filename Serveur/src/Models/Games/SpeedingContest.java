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
        Coordinates c = client.getCoordonnees();
        switch (direction){
            case "GOUP":
                if(!plateau.horsLimite(c.getX(), c.getY()-1) && plateau.getCase(c.getX(),c.getY()-1).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToY(-1);
                } else {
                    return -1;
                }
                break;
            case "GODOWN":
                if(!plateau.horsLimite(c.getX(), c.getY()+1) && plateau.getCase(c.getX(),c.getY()+1).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToY(1);
                } else {
                    return -1;
                }
                break;
            case "GOLEFT":
                if(!plateau.horsLimite(c.getX()-1, c.getY()) && plateau.getCase(c.getX()-1,c.getY()).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToX(-1);
                } else {
                    return -1;
                }
                break;
            case "GORIGHT":
                if(!plateau.horsLimite(c.getX()+1, c.getY()) && plateau.getCase(c.getX()+1,c.getY()).isFree()){
                    plateau.getCase(c.getX(), c.getY()).free();
                    c.addToX(1);
                } else {
                    return -1;
                }
                break;
        }
        Case currentCase = plateau.getCase(c.getX(), c.getY());
        currentCase.setPlayerOn(client);
        if(currentCase instanceof CaseVide){
            return 0;
        } else if (currentCase instanceof CaseTresor){
            int value = ((CaseTresor) currentCase).getValue();
            plateau.setCase(new CaseVide(currentCase.getX(),currentCase.getY()));
            return value;
        } else{
            return 1;
        }
    }
}
