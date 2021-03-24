package models.Game;


import javafx.scene.image.Image;

public class CaseTrou extends Case {
    public CaseTrou(int X, int Y, int COEF_IMAGE) {
        super(X, Y);
        isFree = true;
        this.imageCase = new Image("Trou.png", COEF_IMAGE, COEF_IMAGE, false, false);

    }
}
