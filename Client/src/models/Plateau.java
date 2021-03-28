package models;

import Apps.GameApp;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.Game.*;
import utils.CallbackInstance;
import utils.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Plateau extends CallbackInstance {
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;
    private int COEFF_IMAGE;
    private ArrayList<Image> listeImages;
    private HashMap<String, Coordinates> coordonneesJoueurs = new HashMap<>();
    //private ObservableList<Coordinates>
    private GameApp gameApp;

    public Plateau(int dimX, int dimY, int COEFF_IMAGE, GameApp gameApp) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.COEFF_IMAGE = COEFF_IMAGE;
        this.gameApp = gameApp;

        listeImages = new ArrayList<>(Arrays.asList(new Image("Mur.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("Trou.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("Vide.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor BRONZE.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor ARGENT.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor OR.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor DIAMANT.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("player.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("player2.png", COEFF_IMAGE, COEFF_IMAGE, false, false)));


        for(int x = 0; x < dimX; x++){
            plateau.add(new ArrayList<Case>());
            for(int y = 0; y < dimY; y++){
                plateau.get(x).add(new CaseVide(x,y, listeImages));
            }
        }
    }

    public ArrayList<ArrayList<Case>> getPlateau() {
        return plateau;
    }

    public void setCaseMur(int x, int y){
        this.plateau.get(x).set(y, new CaseMur(x, y, listeImages));
    }

    @Override
    public void getWalls(String s) {
        String[] command = s.split(" ");
        if(!command[1].equals("NUMBER")){
            for(int i = 4; i < command.length; i+= 2){
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseMur(newX,newY, listeImages));
            }
        }
    }

    @Override
    public void getHoles(String s) {
        String[] command = s.split(" ");
        if(!command[1].equals("NUMBER")){
            for(int i = 4; i < command.length; i+= 2){
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseTrou(newX,newY, listeImages));
            }
        }
    }

    @Override
    public void getTresors(String s) {
        String[] command = s.split(" ");
        if(!command[1].equals("NUMBER")){
            for(int i = 4; i < command.length; i+= 3){
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseTresor(newX,newY, Integer.parseInt(command[i+2]), listeImages));
            }
        }
    }

    @Override
    public void updatePlayerPosition(String s) {
        System.out.println("On a recu : "+s);
        String[] command = s.split(" ");
        String name = command[1];
        int x = Integer.parseInt(command[3]);
        int y = Integer.parseInt(command[4]);
        Coordinates c = coordonneesJoueurs.get(name);
        if(c == null) {
            System.out.println("La hashmap ne contenait pas le nom : "+name);
            coordonneesJoueurs.put(name, new Coordinates(x, y));
        } else {
            c.setX(x);
            c.setY(y);
        }
        gameApp.getConnectionHandler().send("411 "+name+" UPDATED");
    }

    @Override
    public void updatePlayerTresor(String s) {
        coordonneesJoueurs.get(s.split(" ")[1]).addToValue(Integer.parseInt(s.split(" ")[6]));
        updatePlayerPosition(s);
        int x = coordonneesJoueurs.get(s.split(" ")[1]).getX();
        int y = coordonneesJoueurs.get(s.split(" ")[1]).getY();
        plateau.get(x).set(y, new CaseVide(x, y, listeImages));
    }

    @Override
    public void declareDead(String s) {
        coordonneesJoueurs.get(s.split(" ")[1]).setAlive(false);
        gameApp.getConnectionHandler().send("520 "+s.split(" ")[1]+" UPDATED");
    }

    @Override
    public void handleMoveAllowed(String s) {
        //TODO: JOUER UN SON DE MARCHE
        KeyCode code = this.gameApp.getDirections().get(0);
        gameApp.getDirections().remove(0);
        String username = gameApp.getServerConfig().getUsername();
        switch (code){
            case UP:
                this.coordonneesJoueurs.get(username).addToY(-1);
                break;
            case DOWN:
                this.coordonneesJoueurs.get(username).addToY(1);
                break;
            case LEFT:
                this.coordonneesJoueurs.get(username).addToX(-1);
                break;
            case RIGHT:
                this.coordonneesJoueurs.get(username).addToX(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMoveBlocked(String s) {
        //TODO: JOUER UN SON POUR DIRE QU'ON A ETE BLOQUE
        gameApp.getDirections().remove(0);
    }

    @Override
    public void handleMoveTresor(String s) {
        handleMoveAllowed(s);
        Coordinates coordinates = this.coordonneesJoueurs.get(this.gameApp.getServerConfig().getUsername());
        coordinates.addToValue(Integer.parseInt(s.split(" ")[4]));
        this.plateau.get(coordinates.getX()).set(coordinates.getY(), new CaseVide(coordinates.getX(), coordinates.getY(), listeImages));
    }

    @Override
    public void handleMoveDead(String s) {
        //TODO: AVERTIR DE SA MORT MAIS LE LAISSER OBSERVER LA PARTIE

    }

    public int getCOEFF_IMAGE() {
        return COEFF_IMAGE;
    }

    public void setCOEFF_IMAGE(int COEFF_IMAGE) {
        this.COEFF_IMAGE = COEFF_IMAGE;
    }

    public HashMap<String, Coordinates> getCoordonneesJoueurs() {
        return coordonneesJoueurs;
    }

    public ArrayList<Image> getListeImages() {
        return listeImages;
    }
}
