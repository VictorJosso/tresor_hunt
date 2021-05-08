package Utils.Bot;

import Utils.Coordinates;

/**
 * The type Solution.
 */
public class Solution {
    private Coordinates coordinates;
    private double score;

    /**
     * Instantiates a new Solution.
     *
     * @param coordinates the coordinates
     * @param score       the score
     */
    public Solution(Coordinates coordinates, double score) {
        this.coordinates = coordinates;
        this.score = score;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets coordinates.
     *
     * @param coordinates the coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(double score) {
        this.score = score;
    }
}
