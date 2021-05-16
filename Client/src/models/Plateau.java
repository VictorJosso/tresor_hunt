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

public class Plateau extends CallbackInstance {
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;
    private int COEFF_IMAGE;
    private ArrayList<Image> listeImages;
    private HashMap<String, Coordinates> coordonneesJoueurs = new HashMap<>();
    private int compteToursRevealHole;
    private int trousRayon1;
    private int compteToursRevealMap;
    private int revealedX;
    private int revealedY;


    private GameApp gameApp;

    public Plateau(int dimX, int dimY, int COEFF_IMAGE, GameApp gameApp) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.COEFF_IMAGE = COEFF_IMAGE;
        this.gameApp = gameApp;

        listeImages = new ArrayList<>(Arrays.asList(new Image(Objects.requireNonNull(getClass().getResource("../Mur.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../Trou.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../Vide.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../tresor_BRONZE.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../tresor_ARGENT.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../tresor_OR.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../tresor_DIAMANT.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_1_left.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_1_right.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_2_left.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_2_right.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../tresor_MYSTERE.png")).toExternalForm(),COEFF_IMAGE,COEFF_IMAGE,false,false),
                new Image(Objects.requireNonNull(getClass().getResource("../war.png")).toExternalForm(), gameApp.getScreenHeight(), gameApp.getScreenHeight(), false, false)));




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
            gameApp.declareCallBacksMaybe();
        } else {
            gameApp.tellYouNeedSomeInfos((int) Math.ceil((double) Integer.parseInt(command[2]) / 5));
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
            gameApp.declareCallBacksMaybe();
        } else {
            gameApp.tellYouNeedSomeInfos((int) Math.ceil((double) Integer.parseInt(command[2]) / 5));
        }
    }


    @Override
    public void getTresors(String s) {
        System.out.println("appel gettresor");
        String[] command = s.split(" ");
        System.out.println(command[1]);

        if(!command[1].equals("NUMBER") && !command[1].equals("SENDING")){

            for(int i = 4; i < command.length; i+= 3){
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseTresor(newX,newY, Integer.parseInt(command[i+2]), listeImages));
            }
            gameApp.declareCallBacksMaybe();
        } else {
            if (!(gameApp.getPartie().getModeDeJeu().equals("3"))) {
                gameApp.tellYouNeedSomeInfos((int) Math.ceil((double) Integer.parseInt(command[2]) / 5));
            }
        }
        System.out.println("plateau : "+plateau);
    }

    @Override
    public void getNearWall(String s) {
        String[] command = s.split(" ");
        if (!command[1].equals("NUMBER") && !command[1].equals("SENDING")) {
            for (int i=4; i< command.length; i+= 2) {
                int newX = Integer.parseInt(command[i]);
                int newY = Integer.parseInt(command[i+1]);
                plateau.get(newX).set(newY, new CaseMur(newX,newY, listeImages));
            }
        }
    }

    @Override
    public void getNearHoles(String s) { // ou alors: ajouter un map avec int,coord qui indique le chiffre
        String[] command = s.split(" ");
        System.out.println("appel getnearHoles");
        if (command[1].equals("NUMBER")) {
            System.out.println("set trous : "+Integer.parseInt(command[2]));
            setTrousRayon1(Integer.parseInt(command[2]));
        }
        System.out.println("plateau avant : "+plateau);
        //if (compteToursRevealHole > 0) { // même pas besoin de cette condition (il faut quand même qu'ils existe,t pour pouvoir mourir !!!
            System.out.println("ajout de trou");
            if(!command[1].equals("NUMBER") && !command[1].equals ("SENDING")){
                for(int i = 4; i < command.length; i+= 2){
                    int newX = Integer.parseInt(command[i]);
                    int newY = Integer.parseInt(command[i+1]);
                    plateau.get(newX).set(newY, new CaseTrou(newX,newY, listeImages));
                }
            }
        //}
        System.out.println("plateau apres trou"+plateau);



       /*if (!command[1].equals("SENDING") && !command[1].equals("NUMBER")) {
           for (int i=4; i<command.length;i+=2) {
               int newX = Integer.parseInt(command[i]);
               int newY = Integer.parseInt(command[i+1]);
               plateau.get(newX).set(newY, new CaseTrou(newX,newY, listeImages));
           }
       }*/
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
            plateau.get(x).get(y).setVisitee();
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
        gameApp.getConnectionHandler().send("512 "+s.split(" ")[1]+" UPDATED");
    }

    @Override
    public void updateRevealHole(String s) {

        compteToursRevealHole=5;
        System.out.println("appel update RevealHole");
        // nombre de tours
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> gameApp.getPlayerTurnUsername().equals(i.getUsername())).findAny().orElse(null);
        assert item!=null;
        if (item.getScore()-20>=0) {
            Platform.runLater(() -> {
                item.setScore(item.getScore() - 20);
                trierLeaderBoard();
            });
        } else {
            gameApp.mainApp.getConnectionHandler().send("905 Not enough point");
        }

        // révèle les trous dans un rayon 1 autour du joueur pendant 5 tours
    }




    public boolean updateCompteToursRevealHole() {
        System.out.println("appel updateCompteToursRevealHole, compteToursRevealHole="+compteToursRevealHole);
        if (/*coordonneesJoueurs.keySet().equals(gameApp.getPlayerTurnUsername()) &&*/ compteToursRevealHole > 0) {
            System.out.println("tour : "+compteToursRevealHole);
            compteToursRevealHole--;
            return true;
        } else {
            for (int i=0; i<plateau.size();i++) {
                for (int j=0; j<plateau.get(i).size();j++) {
                    if (plateau.get(i).get(j) instanceof CaseTrou) {
                        System.out.println("SUPPRESSION DE TROU");
                        // problème : avec remove on a ensuite une erreur
                        //plateau.get(i).remove(j);
                        plateau.get(i).set(j, new CaseVide(i, j, listeImages));

                    }
                }
            }
        }
        return false;
    }


    public void updateRevealMap(String s) {
        String[] command = s.split(" ");
        int x = Integer.parseInt(command[3]);
        int y = Integer.parseInt(command[4]);
        this.revealedX=x;
        this.revealedY=y;
        compteToursRevealHole=5;
        System.out.println("Je vais réveler la map");
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

        if (gameApp.getPartie().getModeDeJeu().equals("3")) {
            plateau.get(coordonneesJoueurs.get(username).getX()).get(coordonneesJoueurs.get(username).getY()).setVisitee();
            updateCompteToursRevealHole();
        }
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




    public void setTrousRayon1(int trousRayon1) {
        this.trousRayon1=trousRayon1;
    }

    public int getTrousRayon1() {
        return trousRayon1;
    }

    public boolean horsLimite (int x, int y) {
        return x < 0 || x >= dimX || y < 0 || y >= dimY;
    }


    public void setCompteToursRevealHole(int i) {
        compteToursRevealHole=i;
    }

    public int getCompteToursRevealHole() {
        return compteToursRevealHole;
    }

    public int getCompteToursRevealMap() { return compteToursRevealMap;}

    public int getRevealedX() {return revealedX;}

    public int getRevealedY() {return revealedY;}

    public void setCompteToursRevealMap(int compteToursRevealMap) {
        this.compteToursRevealMap = compteToursRevealMap;
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }
}
