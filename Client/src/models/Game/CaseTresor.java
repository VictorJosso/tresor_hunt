package models.Game;


public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;

    public CaseTresor(int X, int Y, int value) {
        super(X, Y);
        this.value = value;
        isFree = true;
    }

    public CaseTresor(int X, int Y, int value, boolean secret) {
        super(X, Y);
        this.value = value;
        this.secret = secret;
    }

    public boolean isSecret() {
        return secret;
    }

    public int getValue() {
        return value;
    }
}
