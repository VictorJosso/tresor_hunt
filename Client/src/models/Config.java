package models;

/**
 * The type Config.
 */
public class Config {
    private String adresseServeur = "tresor.josso.fr";
    private int portServeur = 7236;
    private String username;


    /**
     * Instantiates a new Config.
     *
     * @param adresseServeur the server address
     * @param portServeur    the server port
     * @param username       the username
     */
    public Config(String adresseServeur, int portServeur, String username) {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
        this.username = username;
    }

    /**
     * Instantiates a new Config.
     */
    public Config(){

    }

    /**
     * Instantiates a new Config.
     *
     * @param username the username
     */
    public Config(String username) {
        this.username = username;
    }

    /**
     * Gets the server address.
     *
     * @return the server address
     */
    public String getAdresseServeur() {
        return adresseServeur;
    }

    /**
     * Sets the server address.
     *
     * @param adresseServeur the server address
     */
    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    /**
     * Gets server port.
     *
     * @return the server port
     */
    public int getPortServeur() {
        return portServeur;
    }

    /**
     * Sets the server port.
     *
     * @param portServeur the server port
     */
    public void setPortServeur(int portServeur) {
        this.portServeur = portServeur;
    }

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
}
