package Models.Cases;

import Utils.ClientHandler;

public class CaseTrou extends Case{
    public CaseTrou(int X, int Y) {
        super(X, Y);

    }

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
