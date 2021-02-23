package Models;

public class Game {

    //Attributs
    private final int size;
    private final int nb_players;
    private final boolean bots;


    //Constructeur
    public Game(int size, int players, boolean bots) {
        this.size = size;
        this.nb_players = players;
        this.bots = bots;
    }


}
