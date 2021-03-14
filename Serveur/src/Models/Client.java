package Models;

import java.util.ArrayList;

/**
 * The type Client.
 */
public class Client {
    private String username;
    private boolean loggedIn;

    private ArrayList<Integer> joinedGames = new ArrayList<>();

    /**
     * Instantiates a new Client.
     */
    public Client(){}

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets logged in.
     *
     * @param loggedIn the logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ArrayList<Integer> getJoinedGames() {
        return joinedGames;
    }
}
