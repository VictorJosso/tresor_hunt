package utils;

public class Coordinates {
    enum Orientation {
        NORD,
        SUD,
        EST,
        OUEST
        }
    private int x;
    private int y;
    private int value;
    private boolean alive = true;
    private long killDate;

    private Orientation orientation;

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
        if(x - this.x > 0){
            orientation = Orientation.EST;
        } else {
            orientation = Orientation.OUEST;
        }
        this.x = x;
    }


    public void setY(int y) {
        if (y - this.y > 0){
            orientation = Orientation.SUD;
        } else {
            orientation = Orientation.NORD;
        }
        this.y = y;
    }

    public void addToX(int diff){
        if (diff > 0){
            orientation = Orientation.EST;
        } else {
            orientation = Orientation.OUEST;
        }
        this.x += diff;
    }

    public void addToY(int diff){
        if (diff > 0) {
            orientation = Orientation.SUD;
        } else {
            orientation = Orientation.NORD;
        }
        this.y += diff;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.killDate = System.currentTimeMillis();
        this.alive = false;
    }

    public long getKillDate(){
        return killDate;
    }
}
