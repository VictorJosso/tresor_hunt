package models.Game;

import javafx.scene.image.Image;

public class CaseVide extends Case {
    public CaseVide(int X, int Y) {
        super(X, Y);
        this.isFree = true;
        this.imageCase = new Image("Vide.png", 20, 20, false, false);
    }
}
