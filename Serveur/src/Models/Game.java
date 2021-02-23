package Models;

public abstract class Game {

    //Attributs
    private final int x;
    private final int y;
    private final int treasures;
    private final int holes;
    private final int mod;
    private static int id=0;


    public Game(int x, int y, int tres, int holes, int mode) {
        this.x=x;
        this.y=y;
        this.treasures=tres;
        this.holes=holes;
        this.mod=mode;
        this.id = id++;
    }

}
