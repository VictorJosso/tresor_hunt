package Utils;

/**
 * The type Coordinates.
 */
public class Coordinates {
    private int x;
    private int y;
    private int value;

    /**
     * Instantiates a new Coordinates.
     *
     * @param x the x
     * @param y the y
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
    }

    /**
     * Instantiates a new Coordinates.
     *
     * @param x     the x
     * @param y     the y
     * @param value the value
     */
    public Coordinates(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * Instantiates a new Coordinates.
     *
     * @param original the original
     */
    public Coordinates(Coordinates original){
        this.x = original.x;
        this.y = original.y;
        this.value = original.value;
    }

    /**
     * Copy coordinates.
     *
     * @return the coordinates
     */
    public Coordinates copy(){
        return new Coordinates(this);
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Add to value.
     *
     * @param diff the diff
     */
    public void addToValue(int diff){
        this.value += diff;
    }


    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Add to x.
     *
     * @param diff the diff
     */
    public void addToX(int diff){
        this.x += diff;
        //System.out.println("Nouvelles coordonnées : ("+x+", "+y+")");
    }

    /**
     * Add to y.
     *
     * @param diff the diff
     */
    public void addToY(int diff){
        this.y += diff;
        //System.out.println("Nouvelles coordonnées : ("+x+", "+y+")");
    }
}