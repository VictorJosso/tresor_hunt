package models;

import javafx.scene.image.Image;
import models.Game.*;
import utils.CallbackInstance;

import java.util.ArrayList;
import java.util.Arrays;

public class Plateau extends CallbackInstance {
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;
    private int COEFF_IMAGE;
    private ArrayList<Image> listeImages;

    public Plateau(int dimX, int dimY, int COEFF_IMAGE) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.COEFF_IMAGE = COEFF_IMAGE;

        listeImages = new ArrayList<>(Arrays.asList(new Image("Mur.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("Trou.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("Vide.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor BRONZE.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor ARGENT.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor OR.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("trésor DIAMANT.png", COEFF_IMAGE, COEFF_IMAGE, false, false)));

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

    public int getCOEFF_IMAGE() {
        return COEFF_IMAGE;
    }

    public void setCOEFF_IMAGE(int COEFF_IMAGE) {
        this.COEFF_IMAGE = COEFF_IMAGE;
    }
}
