package utils;

public class Coordinates {
    private int x;
    private int y;
    private final int value;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = -1;
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


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }
}
