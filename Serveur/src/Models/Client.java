package Models;

import Models.Games.Game;
import Utils.Coordinates;

import java.util.ArrayList;

/**
 * The type Client.
 */
public class Client {

    private Game gameRunning;
    private Coordinates coordonnees = new Coordinates(0,0);
    private boolean alive = true;
    private int score = 0;



    /**
     * Instantiates a new Client.
     */
    public Client(){}

    public Coordinates getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordinates coordonnees) {
        this.coordonnees = coordonnees;
    }

    public Game getGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(Game gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int diff) {
        this.score += diff;
    }
}
