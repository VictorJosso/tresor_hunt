package Utils.Bot;

import Models.Cases.*;
import Utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Plateau bot.
 */
public class PlateauBot {

    private Case[][] grille;
    private ArrayList<String> players = new ArrayList<>();
    private HashMap<String, Coordinates> coordinatesHashMap = new HashMap<>();
    private CopyOnWriteArrayList<Coordinates> treasuresList = new CopyOnWriteArrayList<>();
    private int dim_x;
    private int dim_y;

    /**
     * Instantiates a new Plateau bot.
     *
     * @param dim_x the dim x
     * @param dim_y the dim y
     */
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

    /**
     * Instantiates a new Plateau bot.
     *
     * @param grille             the grille
     * @param players            the players
     * @param coordinatesHashMap the coordinates hash map
     * @param treasuresList      the treasures list
     * @param dim_x              the dim x
     * @param dim_y              the dim y
     */
    public PlateauBot(Case[][] grille, ArrayList<String> players, HashMap<String, Coordinates> coordinatesHashMap, CopyOnWriteArrayList<Coordinates> treasuresList, int dim_x, int dim_y) {
        this.grille = grille;
        this.players = players;
        this.coordinatesHashMap = coordinatesHashMap;
        this.dim_x = dim_x;
        this.dim_y = dim_y;
        this.treasuresList = treasuresList;
    }

    /**
     * Copy plateau bot.
     *
     * @return the plateau bot
     */
    public PlateauBot copy(){
        Case[][] grille_copy = new Case[dim_x][dim_y];
        for (int x = 0; x < dim_x; x ++){
            for (int y = 0; y < dim_y; y++){
                grille_copy[x][y] = grille[x][y].copy();
            }
        }
        HashMap<String, Coordinates> coordinatesHashMap_copy = new HashMap<>();
        coordinatesHashMap.forEach((key, value) -> coordinatesHashMap_copy.put(key, value.copy()));
        //for (String name: coordinatesHashMap.keySet()){
        //    coordinatesHashMap_copy.put(name, coordinatesHashMap.get(name).copy());
        //}
        CopyOnWriteArrayList<Coordinates> treasureList_copy = new CopyOnWriteArrayList<>();
        treasuresList.forEach(value -> treasureList_copy.add(value.copy()));
        //for (Coordinates c: this.treasuresList){
        //    treasureList_copy.add(c.copy());
        //}
        PlateauBot cop = new PlateauBot(grille_copy, this.players, coordinatesHashMap_copy, treasureList_copy, this.dim_x, this.dim_y);
        //System.out.println("\n\n\n\n\n/////////////// COPIE DE GRILLE \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        //affichePlateau();
        //System.out.println("\n");
        //cop.affichePlateau();
        return cop;
    }

    /**
     * Set position.
     *
     * @param player the player
     * @param posX   the pos x
     * @param posY   the pos y
     */
    public void setPosition(String player, int posX, int posY){
        if (coordinatesHashMap.containsKey(player)){
            coordinatesHashMap.get(player).setX(posX);
            coordinatesHashMap.get(player).setY(posY);
        } else {
            coordinatesHashMap.put(player, new Coordinates(posX, posY));
        }
    }

    /**
     * Treasure found.
     *
     * @param player the player
     * @param value  the value
     * @param coordX the coord x
     * @param coordY the coord y
     */
    public void treasureFound(String player, int value, int coordX, int coordY){
        coordinatesHashMap.get(player).addToValue(value);
        this.grille[coordX][coordY] = new CaseVide(coordX, coordY);
        this.treasuresList.removeIf(c -> c.getX() == coordX && c.getY() == coordY);
    }

    /**
     * Notify dead.
     *
     * @param player the player
     */
    public void notifyDead(String player){
        this.coordinatesHashMap.remove(player);
    }

    /**
     * Add player.
     *
     * @param username the username
     */
    public void addPlayer(String username){
        this.players.add(username);
    }

    private void affichePlateau(){
        //System.out.println("\n\n\n");
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

    /**
     * Is there a player there boolean.
     *
     * @param posX the pos x
     * @param posY the pos y
     * @return the boolean
     */
    public boolean isThereAPlayerThere(int posX, int posY){
        for (Coordinates c: this.coordinatesHashMap.values()){
            if (c.getX() == posX && c.getY() == posY){
                return true;
            }
        }
        return false;
    }

    /**
     * Get player position coordinates.
     *
     * @param username the username
     * @return the coordinates
     */
    public Coordinates getPlayerPosition(String username){
        return this.coordinatesHashMap.get(username);
    }

    /**
     * Receive data.
     *
     * @param type the type
     * @param data the data
     */
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
                    this.treasuresList.add(new Coordinates(posX, posY));
                    break;
            }
        }
        affichePlateau();
    }

    /**
     * Gets treasures list.
     *
     * @return the treasures list
     */
    public CopyOnWriteArrayList<Coordinates> getTreasuresList() {
        return treasuresList;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Get grille case [ ] [ ].
     *
     * @return the case [ ] [ ]
     */
    public Case[][] getGrille() {
        return grille;
    }

    /**
     * Gets dim x.
     *
     * @return the dim x
     */
    public int getDim_x() {
        return dim_x;
    }

    /**
     * Gets dim y.
     *
     * @return the dim y
     */
    public int getDim_y() {
        return dim_y;
    }
}
