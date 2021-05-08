package models;

import Apps.GameApp;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import models.Game.*;
import utils.CallbackInstance;
import utils.Coordinates;
import utils.ImageCrop;
import utils.LeaderBoardItem;

import java.io.File;
import java.util.*;

/**
 * The type Plateau.
 */
public class Plateau extends CallbackInstance {
    /**
     * The Plateau.
     */
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;
    private int COEFF_IMAGE;
    private ArrayList<Image> listeImages;
    private HashMap<String, Coordinates> coordonneesJoueurs = new HashMap<>();

    private GameApp gameApp;

    /**
     * Instantiates a new Plateau.
     *
     * @param dimX        the dim x
     * @param dimY        the dim y
     * @param COEFF_IMAGE the coeff image
     * @param gameApp     the game app
     */
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
                new Image("player_1_left.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("player_1_right.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("player_2_left.png", COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image("player_2_right.png", COEFF_IMAGE, COEFF_IMAGE, false, false)));


        for(int x = 0; x < dimX; x++){
            plateau.add(new ArrayList<Case>());
            for(int y = 0; y < dimY; y++){
                plateau.get(x).add(new CaseVide(x,y, listeImages));
            }
        }
    }

    /**
     * Gets plateau.
     *
     * @return the plateau
     */
    public ArrayList<ArrayList<Case>> getPlateau() {
        return plateau;
    }

    /**
     * Set case mur.
     *
     * @param x the x
     * @param y the y
     */
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
            gameApp.getLeaderBoardItems().add(new LeaderBoardItem(name, "#1", 0));
        } else {
            c.setX(x);
            c.setY(y);
        }
        gameApp.getConnectionHandler().send("512 "+name+" UPDATED");
    }

    @Override
    public void updatePlayerTresor(String s) {
        coordonneesJoueurs.get(s.split(" ")[1]).addToValue(Integer.parseInt(s.split(" ")[6]));
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> s.split(" ")[1].equals(i.getUsername())).findAny().orElse(null);
        assert item != null;
        Platform.runLater(() -> {
                    item.setScore(item.getScore() + Integer.parseInt(s.split(" ")[6]));
                    trierLeaderBoard();
                });
        updatePlayerPosition(s);
        int x = coordonneesJoueurs.get(s.split(" ")[1]).getX();
        int y = coordonneesJoueurs.get(s.split(" ")[1]).getY();
        plateau.get(x).set(y, new CaseVide(x, y, listeImages));
    }

    @Override
    public void declareDead(String s) {
        coordonneesJoueurs.get(s.split(" ")[1]).kill();
        gameApp.getConnectionHandler().send("521 "+s.split(" ")[1]+" UPDATED");
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> s.split(" ")[1].equals(i.getUsername())).findAny().orElse(null);
        Platform.runLater(() -> {
            assert item != null;
            item.kill();
            trierLeaderBoard();
        });
    }

    @Override
    public void handleMoveAllowed(String s) {
        //TODO: JOUER UN SON DE MARCHE
        System.err.println("LA FONCTION HANDLEMOVEALLOWED A ETE APPELÉE AVEC LA CHAINE "+s+" ET LES DIRECTIONS : "+this.gameApp.getDirections());
        KeyCode code = this.gameApp.getDirections().get(0);
        gameApp.getDirections().remove(0);
        String username = gameApp.getServerConfig().getUsername();
        switch (code) {
            case UP -> this.coordonneesJoueurs.get(username).addToY(-1);
            case DOWN -> this.coordonneesJoueurs.get(username).addToY(1);
            case LEFT -> this.coordonneesJoueurs.get(username).addToX(-1);
            case RIGHT -> this.coordonneesJoueurs.get(username).addToX(1);
            default -> handleMoveAllowed(s);
        }
    }

    @Override
    public void handleMoveBlocked(String s) {
        //TODO: JOUER UN SON POUR DIRE QU'ON A ETE BLOQUE
        KeyCode code = gameApp.getDirections().get(0);
        gameApp.getDirections().remove(0);
        switch(code){
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
                break;
            default:
                handleMoveBlocked(s);
        }
    }

    @Override
    public void handleMoveTresor(String s) {
        handleMoveAllowed(s);
        Coordinates coordinates = this.coordonneesJoueurs.get(this.gameApp.getServerConfig().getUsername());
        coordinates.addToValue(Integer.parseInt(s.split(" ")[4]));
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> this.gameApp.getServerConfig().getUsername().equals(i.getUsername())).findAny().orElse(null);
        assert item != null;
        Platform.runLater(() -> {
                    item.setScore(item.getScore() + Integer.parseInt(s.split(" ")[4]));
                    trierLeaderBoard();
                });
        this.plateau.get(coordinates.getX()).set(coordinates.getY(), new CaseVide(coordinates.getX(), coordinates.getY(), listeImages));
    }

    @Override
    public void handleMoveDead(String s) {
        //TODO: AVERTIR DE SA MORT MAIS LE LAISSER OBSERVER LA PARTIE
        gameApp.getDirections().remove(0);
        this.coordonneesJoueurs.get(this.gameApp.getServerConfig().getUsername()).kill();
        AudioClip sound = new AudioClip(getClass().getResource("../screamer.mp3").toExternalForm());
        double ratio = 1000.0 / 750.0;
        int py = (int) (gameApp.getScreenWidth() / ratio);
        int px = (int) (gameApp.getScreenHeight() * ratio);
        int x, y;
        if (py < gameApp.getScreenHeight()){
            x = px;
            y = gameApp.getScreenHeight();
        } else {
            x = gameApp.getScreenWidth();
            y = py;
        }
        Image screamer = new Image("screamer.jpg", x, y, false, false);
        ImageCrop crop = new ImageCrop(screamer, x, y , (x - gameApp.getScreenWidth()) / 2, (y - gameApp.getScreenHeight()) / 2, gameApp.getScreenWidth(), gameApp.getScreenHeight());
        gameApp.registerDrawOnTop(crop, 3000);
        sound.play();
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> this.gameApp.getServerConfig().getUsername().equals(i.getUsername())).findAny().orElse(null);
        Platform.runLater(() -> {
            assert item != null;
            item.kill();
            trierLeaderBoard();
        });

    }

    private void trierLeaderBoard(){
        Collections.sort(gameApp.getLeaderBoardItems());
        for (int i = 0; i < gameApp.getLeaderBoardItems().size(); i++){
            if (i != 0 && gameApp.getLeaderBoardItems().get(i-1).getScore() == gameApp.getLeaderBoardItems().get(i).getScore()){
                gameApp.getLeaderBoardItems().get(i).setRank(gameApp.getLeaderBoardItems().get(i-1).getRank());
            } else {
                gameApp.getLeaderBoardItems().get(i).setRank("#"+(i+1));
            }
        }
    }

    @Override
    public void handleNotYourTurn(String s) {
        handleMoveBlocked(s);
    }

    @Override
    public void handleTurnChanged(String s) {
        this.gameApp.setPlayerTurnUsername(s.split(" ")[1]);
    }

    @Override
    public void partieFinie(String s) {
        String gagnant = s.split(" ")[1];
        gameApp.getTimer().stop();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NOUS AVONS UN VAINQUEUR");
            alert.setContentText("Et le vainqueur est ... " +gagnant+"!");
            alert.initOwner(gameApp.getGameStage().getOwner());
            alert.setHeaderText(null);
            alert.showAndWait();

            gameApp.endGame();
        });
    }

    /**
     * Gets coeff image.
     *
     * @return the coeff image
     */
    public int getCOEFF_IMAGE() {
        return COEFF_IMAGE;
    }

    /**
     * Sets coeff image.
     *
     * @param COEFF_IMAGE the coeff image
     */
    public void setCOEFF_IMAGE(int COEFF_IMAGE) {
        this.COEFF_IMAGE = COEFF_IMAGE;
    }

    /**
     * Gets coordonnees joueurs.
     *
     * @return the coordonnees joueurs
     */
    public HashMap<String, Coordinates> getCoordonneesJoueurs() {
        return coordonneesJoueurs;
    }

    /**
     * Gets liste images.
     *
     * @return the liste images
     */
    public ArrayList<Image> getListeImages() {
        return listeImages;
    }
}
