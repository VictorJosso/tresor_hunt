package models.Game;


import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * The type Case trou.
 */
public class CaseTrou extends Case {
    /**
     * Instantiates a new Case trou.
     *
     * @param X           the x
     * @param Y           the y
     * @param listeImages the liste images
     */
    public CaseTrou(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        isFree = true;
        this.imageCase = listeImages.get(1);

    }
}
