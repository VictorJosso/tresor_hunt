package Utils.Bot;

import Models.Cases.*;
import Utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;

public class PlateauBot {

    private Case[][] grille;
    private ArrayList<String> players = new ArrayList<>();
    private HashMap<String, Coordinates> coordinatesHashMap = new HashMap<>();
    private int dim_x;
    private int dim_y;

    public PlateauBot(int dim_x, int dim_y){
        this.dim_x = dim_x;
        this.dim_y = dim_y;
        grille = new Case[dim_x][dim_y];
        for (int x = 0; x < dim_x; x ++){
            for (int y = 0; y < dim_y; y++){
                this.grille[x][y] = new CaseVide(x, y);
            }
        }
    }

    public PlateauBot(Case[][] grille){
        this.grille = grille;
    }

    public PlateauBot copy(){
        return new PlateauBot(grille);
    }

    public void setPosition(String player, int posX, int posY){
        if (coordinatesHashMap.containsKey(player)){
            coordinatesHashMap.get(player).setX(posX);
            coordinatesHashMap.get(player).setY(posY);
        } else {
            coordinatesHashMap.put(player, new Coordinates(posX, posY));
        }
    }

    public void treasureFound(String player, int value){
        coordinatesHashMap.get(player).addToValue(value);
    }

    public void notifyDead(String player){
        this.coordinatesHashMap.remove(player);
    }

    public void addPlayer(String username){
        this.players.add(username);
    }

    private void affichePlateau(){
        System.out.println("\n\n\n");
        String sep = "-".repeat(Math.max(0, dim_x));
        System.out.println(sep);
        for (int y = 0; y < dim_y; y++){
            StringBuilder builder = new StringBuilder("|");
            for (int x = 0; x < dim_x; x ++){
                builder.append(grille[x][y]);
            }
            builder.append("|");
            System.out.println(builder);
        }
        System.out.println(sep);
    }

    public void receiveData(String type, ArrayList<String> data){
        for (String coord: data) {
            String[] coordinates = coord.split(" ");
            int posX = Integer.parseInt(coordinates[0]);
            int posY = Integer.parseInt(coordinates[1]);
            switch (type){
                case "401":
                    grille[posX][posY] = new CaseTrou(posX, posY);
                    break;
                case "421":
                    grille[posX][posY] = new CaseMur(posX, posY);
                    break;
                case "411":
                    int value = Integer.parseInt(coordinates[2]);
                    grille[posX][posY] = new CaseTresor(posX, posY, value);
                    break;
            }
        }
        affichePlateau();
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
