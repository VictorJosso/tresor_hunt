package utils;

public class Coordinates {
    private int x;
    private int y;
    private int value;
    private boolean alive = true;

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
    }

    public void addToY(int diff){
        this.y += diff;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }
}
