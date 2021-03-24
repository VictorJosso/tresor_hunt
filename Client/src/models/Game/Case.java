package models.Game;


import javafx.scene.image.Image;

public abstract class Case {

    protected boolean isFree;
    protected int posVert;
    protected int posHor;
    protected Image imageCase;
    private int COEF_IMAGE;

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
    }

}

    /*
    public int type;

    int posVert;
    int posHor;

    public Case(int type, int posVert, int posHor) {
        this.type = type;
        this.posVert = posVert;
        this.posHor = posHor;
    }
    public Case(int posVert, int posHor) {
        this.posVert = posVert;
        this.posHor = posHor;
    }
    public Case(int type) {
        this.type = type;
    }
    public Case() {
        this.type = -1;
    }

    public void vide() {
        this.type=-1;
    }

    public void mur() {
        this.type=0;
    }

    public void trs5() {
        this.type=1;
    }

    public void trs10() {
        this.type=2;
    }

    public void trs15() {
        this.type=3;
    }

    public void trs20() {
        this.type=4;
    }

    public void trou() {
        this.type=5;
    }

    public void trsX() {
        this.type=6;
    }

    public boolean isVide() {
        if(type==-1) return true;
        else return false;
    }

    public boolean isMur() {
        if(type==0) return true;
        else return false;
    }

    public boolean isTrou() {
        if(type==5) return true;
        else return false;
    }

    public boolean isTrs() {
        if(type==1 || type==2 || type==3 || type==4) return true;
        else return false;
    }

    public boolean isTrs5() {
        if(type==1) return true;
        else return false;
    }

    public boolean isTrs10() {
        if(type==2) return true;
        else return false;
    }

    public boolean isTrs15() {
        if(type==3) return true;
        else return false;
    }

    public boolean isTrs20() {
        if(type==4) return true;
        else return false;
    }

    public void pillage() {
        this.type = -1;
    }
}
*/
