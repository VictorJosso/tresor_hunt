package models.Game;


import javafx.scene.image.Image;

import java.util.ArrayList;

public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;

    public CaseTresor(int X, int Y, int value, ArrayList<Image> listeImages) {
        super(X, Y);
        this.value = value;
        isFree = true;
        switch (value) {
            // trésor inconnu : valeur 0 ?
            case 5:
                this.imageCase = listeImages.get(3);
                break;
            case 10:
                this.imageCase = listeImages.get(4);
                break;
            case 15:
                this.imageCase = listeImages.get(5);
                break;
            case 20:
                this.imageCase = listeImages.get(6);
                break;
            default:
                this.imageCase = listeImages.get(6);
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
