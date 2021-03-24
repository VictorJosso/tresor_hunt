package Models.Cases;

import Utils.ClientHandler;

public class CaseTrou extends Case{
    public CaseTrou(int X, int Y) {
        super(X, Y);
        isFree = true;

    }

    @Override
    public void setPlayerOn(ClientHandler player) {
        player.kill();
    }
}
