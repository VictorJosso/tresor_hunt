package models.Game;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class CaseVide extends Case {
    public CaseVide(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        this.isFree = true;
        this.imageCase = listeImages.get(2);
    }
}
