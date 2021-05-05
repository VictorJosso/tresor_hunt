package Utils.Bot;

import Utils.Coordinates;

public class Solution {
    private Coordinates coordinates;
    private double score;

    public Solution(Coordinates coordinates, double score) {
        this.coordinates = coordinates;
        this.score = score;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public double getScore() {
        return score;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
