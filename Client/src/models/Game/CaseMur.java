package models.Game;

import javafx.scene.image.Image;

public class CaseMur extends Case{

    private int durabilite = 100;

    public CaseMur(int X, int Y, int COEF_IMAGE) {
        super(X, Y);
        isFree = false;
        this.imageCase = new Image("Mur.png", COEF_IMAGE, COEF_IMAGE, false, false);
    }

    @Override
    public void free() {}


    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }
}
