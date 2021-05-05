package Models.Cases;

import Utils.ClientHandler;

public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;
    private ClientHandler owner;

    public CaseTresor(int X, int Y, int value) {
        super(X, Y);
        this.value = value;
    }

    public CaseTresor(int X, int Y, int value, boolean secret) {
        super(X, Y);
        this.value = value;
        this.secret = secret;
    }

    public CaseTresor(CaseTresor original){
        super(original);
        this.value = original.value;
        this.secret = original.secret;
        this.owner = original.owner;
    }

    public CaseTresor copy(){
        return new CaseTresor(this);
    }

    public boolean isSecret() {
        return secret;
    }

    public int getValue() {
        return value;
    }

    public ClientHandler getOwner() {
        return owner;
    }

    public void setOwner(ClientHandler owner) {
        this.owner = owner;
    }

    @Override
    public boolean isFree() {
        return playerOn == null;
    }

    @Override
    public String toString() {
        return "T";
    }
}
