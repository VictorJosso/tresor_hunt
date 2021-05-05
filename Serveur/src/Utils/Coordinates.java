package Utils;

public class Coordinates {
    private int x;
    private int y;
    private int value;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
    }

    public Coordinates(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Coordinates(Coordinates original){
        this.x = original.x;
        this.y = original.y;
        this.value = original.value;
    }

    public Coordinates copy(){
        return new Coordinates(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public void addToValue(int diff){
        this.value += diff;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }

    public void addToX(int diff){
        this.x += diff;
        //System.out.println("Nouvelles coordonnées : ("+x+", "+y+")");
    }

    public void addToY(int diff){
        this.y += diff;
        //System.out.println("Nouvelles coordonnées : ("+x+", "+y+")");
    }
}