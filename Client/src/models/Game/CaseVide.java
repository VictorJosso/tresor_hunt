package models.Game;

import javafx.scene.image.Image;

public class CaseVide extends Case {
    public CaseVide(int X, int Y, int COEF_IMAGE) {
        super(X, Y);
        this.isFree = true;
        this.imageCase = new Image("Vide.png", COEF_IMAGE, COEF_IMAGE, false, false);
    }
}
