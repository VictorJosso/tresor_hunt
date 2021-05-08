package Models.Cases;

import Utils.ClientHandler;

/**
 * The type Case trou.
 */
public class CaseTrou extends Case{
    /**
     * Instantiates a new Case trou.
     *
     * @param X the x
     * @param Y the y
     */
    public CaseTrou(int X, int Y) {
        super(X, Y);

    }

    /**
     * Instantiates a new Case trou.
     *
     * @param original the original
     */
    public CaseTrou(CaseTrou original){
        super(original);
    }

    public CaseTrou copy(){
        return new CaseTrou(this);
    }

    @Override
    public void setPlayerOn(ClientHandler player) {
        player.getClient().kill();
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public String toString() {
        return "H";
    }
}
