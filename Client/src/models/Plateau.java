package models;

import models.Game.Case;
import models.Game.CaseMur;
import models.Game.CaseVide;
import utils.CallbackInstance;

import java.util.ArrayList;

public class Plateau extends CallbackInstance {
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;

    public Plateau(int dimX, int dimY) {
        this.dimX = dimX;
        this.dimY = dimY;

        for(int x = 0; x < dimX; x++){
            plateau.add(new ArrayList<Case>());
            for(int y = 0; y < dimY; y++){
                plateau.get(x).add(new CaseVide(x,y));
            }
        }
    }

    public ArrayList<ArrayList<Case>> getPlateau() {
        return plateau;
    }

    public void setCaseMur(int x, int y){
        this.plateau.get(x).set(y, new CaseMur(x, y));
    }

    @Override
    public void getWalls(String s) {
        String[] command = s.split(" ");
        if(!command[1].equals("NUMBER")){
            for(int i = 4; i < command.length; i+= 2){
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseMur(newX,newY));
            }
        }
    }
}