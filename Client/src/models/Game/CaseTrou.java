package models.Game;


import javafx.scene.image.Image;

public class CaseTrou extends Case {
    public CaseTrou(int X, int Y) {
        super(X, Y);
        isFree = true;
        this.imageCase = new Image("Trou.png", 20, 20, false, false);

    }
}
