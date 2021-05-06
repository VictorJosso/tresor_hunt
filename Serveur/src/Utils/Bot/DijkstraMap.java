package Utils.Bot;

import Utils.Coordinates;

import java.util.ArrayList;

public class DijkstraMap {
    private final double[][] distances;
    private final Coordinates[][] predecesseurs;

    public DijkstraMap(double[][] distances, Coordinates[][] predecesseurs) {
        this.distances = distances;
        this.predecesseurs = predecesseurs;
    }

    public double[][] getDistances() {
        return distances;
    }

    public Coordinates[][] getPredecesseurs() {
        return predecesseurs;
    }
}
