package models.Game;


import javafx.scene.image.Image;

import java.util.ArrayList;

public class CaseTrou extends Case {
    public CaseTrou(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        isFree = true;
        this.imageCase = listeImages.get(1);

    }
}
