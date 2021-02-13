package models;

import javafx.beans.property.*;

public class Partie {

    private IntegerProperty identifiant;
    private StringProperty createur;
    private StringProperty modeDeJeu;
    private IntegerProperty dimensionX;
    private IntegerProperty dimensionY;
    private IntegerProperty nombreDeTrous;
    private IntegerProperty nombreDeTresors;
    private BooleanProperty robots;

    public Partie(){
        this(-1, null, null, 0, 0, 0, 0, false);
    }

    public Partie(int identifiant, String createur, String modeDeJeu, int dimensionX, int dimensionY, int nombreDeTrous, int nombreDeTresors, boolean robots) {
        this.identifiant = new SimpleIntegerProperty(identifiant);
        this.createur = new SimpleStringProperty(createur);
        this.modeDeJeu = new SimpleStringProperty(modeDeJeu);
        this.dimensionX = new SimpleIntegerProperty(dimensionX);
        this.dimensionY = new SimpleIntegerProperty(dimensionY);
        this.nombreDeTrous = new SimpleIntegerProperty(nombreDeTrous);
        this.nombreDeTresors = new SimpleIntegerProperty(nombreDeTresors);
        this.robots = new SimpleBooleanProperty(robots);
    }

    public int getIdentifiant() {
        return identifiant.get();
    }

    public IntegerProperty identifiantProperty() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant.set(identifiant);
    }

    public String getCreateur() {
        return createur.get();
    }

    public StringProperty createurProperty() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur.set(createur);
    }

    public String getModeDeJeu() {
        return modeDeJeu.get();
    }

    public StringProperty modeDeJeuProperty() {
        return modeDeJeu;
    }

    public void setModeDeJeu(String modeDeJeu) {
        this.modeDeJeu.set(modeDeJeu);
    }

    public int getDimensionX() {
        return dimensionX.get();
    }

    public IntegerProperty dimensionXProperty() {
        return dimensionX;
    }

    public void setDimensionX(int dimensionX) {
        this.dimensionX.set(dimensionX);
    }

    public int getDimensionY() {
        return dimensionY.get();
    }

    public IntegerProperty dimensionYProperty() {
        return dimensionY;
    }

    public void setDimensionY(int dimensionY) {
        this.dimensionY.set(dimensionY);
    }

    public int getNombreDeTrous() {
        return nombreDeTrous.get();
    }

    public IntegerProperty nombreDeTrousProperty() {
        return nombreDeTrous;
    }

    public void setNombreDeTrous(int nombreDeTrous) {
        this.nombreDeTrous.set(nombreDeTrous);
    }

    public int getNombreDeTresors() {
        return nombreDeTresors.get();
    }

    public IntegerProperty nombreDeTresorsProperty() {
        return nombreDeTresors;
    }

    public void setNombreDeTresors(int nombreDeTresors) {
        this.nombreDeTresors.set(nombreDeTresors);
    }

    public boolean isRobots() {
        return robots.get();
    }

    public BooleanProperty robotsProperty() {
        return robots;
    }

    public void setRobots(boolean robots) {
        this.robots.set(robots);
    }
}
