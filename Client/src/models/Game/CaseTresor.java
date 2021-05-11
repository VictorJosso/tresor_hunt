package models.Game;


import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * The type Case tresor.
 */
public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;

    /**
     * Instantiates a new Case tresor.
     *
     * @param X           the x
     * @param Y           the y
     * @param value       the value
     * @param listeImages the liste images
     */
    public CaseTresor(int X, int Y, int value, ArrayList<Image> listeImages) {
        super(X, Y);
        this.value = value;
        isFree = true;
        switch (value) {
            case 0:
                this.imageCase=listeImages.get(11);
                break;
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

    /**
     * Instantiates a new Case tresor.
     *
     * @param X      the x
     * @param Y      the y
     * @param value  the value
     * @param secret the secret
     */
    public CaseTresor(int X, int Y, int value, boolean secret) {
        super(X, Y);
        this.value = value;
        this.secret = secret;
    }

    /**
     * Is secret boolean.
     *
     * @return the boolean
     */
    public boolean isSecret() {
        return secret;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
