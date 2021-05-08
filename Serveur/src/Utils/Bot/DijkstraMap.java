package Utils.Bot;

import Utils.Coordinates;

import java.util.ArrayList;

/**
 * The type Dijkstra map.
 */
public class DijkstraMap {
    private final double[][] distances;
    private final Coordinates[][] predecesseurs;

    /**
     * Instantiates a new Dijkstra map.
     *
     * @param distances     the distances
     * @param predecesseurs the predecesseurs
     */
    public DijkstraMap(double[][] distances, Coordinates[][] predecesseurs) {
        this.distances = distances;
        this.predecesseurs = predecesseurs;
    }

    /**
     * Get distances double [ ] [ ].
     *
     * @return the double [ ] [ ]
     */
    public double[][] getDistances() {
        return distances;
    }

    /**
     * Get predecesseurs coordinates [ ] [ ].
     *
     * @return the coordinates [ ] [ ]
     */
    public Coordinates[][] getPredecesseurs() {
        return predecesseurs;
    }
}
