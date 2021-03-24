package models.Game;


import javafx.scene.image.Image;

public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;

    public CaseTresor(int X, int Y, int value) {
        super(X, Y);
        this.value = value;
        isFree = true;
        switch (value) {
            case 5:
                this.imageCase = new Image("trésor BRONZE.png", 20, 20, false, false);
                break;
            case 10:
                this.imageCase = new Image("trésor ARGENT.png", 20, 20, false, false);
                break;
            case 15:
                this.imageCase = new Image("trésor OR.png", 20, 20, false, false);
                break;
            case 20:
                this.imageCase = new Image("trésor DIAMANT.png", 20, 20, false, false);
                break;
            default:
                this.imageCase = new Image("trésor MYSTÈRE.png", 20, 20, false, false);
                break;



        }
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
