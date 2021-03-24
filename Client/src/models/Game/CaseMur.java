package models.Game;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class CaseMur extends Case{

    private int durabilite = 100;

    public CaseMur(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        isFree = false;
        this.imageCase = listeImages.get(0);
    }

    @Override
    public void free() {}


    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }
}
