package models;


public class Config {
    private String adresseServeur = "tresor.josso.fr";
    private int portServeur = 7236;
    private String username;

    public Config(String adresseServeur, int portServeur, String username) {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
        this.username = username;
    }

    public Config(){

    }

    public Config(String username) {
        this.username = username;
    }

    public String getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    public int getPortServeur() {
        return portServeur;
    }

    public void setPortServeur(int portServeur) {
        this.portServeur = portServeur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
