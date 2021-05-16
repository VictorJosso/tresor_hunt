package models.Game;


import javafx.scene.image.Image;

public abstract class Case {

    protected boolean isFree;
    protected int posVert;
    protected int posHor;
    protected Image imageCase;
    private int COEF_IMAGE;

    // warfog
    protected boolean visitee;

    public boolean isFree() {
        return isFree;
    }

    public void free(){
        isFree = true;
    }

    public Image getImageCase() {
        return imageCase;
    }

    public int getX(){
        return posHor;
    }

    public int getY(){
        return posVert;
    }

    public Case(int X, int Y) {
        this.posVert = Y;
        this.posHor = X;
        visitee=false;
    }

    public void setVisitee() {
        visitee=true;
    }

    public boolean isVisitee() { return visitee; }

}