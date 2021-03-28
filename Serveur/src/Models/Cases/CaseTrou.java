package Models.Cases;

import Utils.ClientHandler;

public class CaseTrou extends Case{
    public CaseTrou(int X, int Y) {
        super(X, Y);

    }

    @Override
    public void setPlayerOn(ClientHandler player) {
        player.getClient().kill();
    }

    @Override
    public boolean isFree() {
        return true;
    }
}
