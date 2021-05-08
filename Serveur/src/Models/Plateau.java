package Models;

//import java.util.ArrayList;
import Models.Cases.*;
import Models.Games.Game;
import Utils.ClientHandler;
import Utils.Coordinates;
import Utils.Tracker;

import java.util.*;

import static java.lang.Math.*;

/**
 * The type Plateau.
 */
public class Plateau {

    // Variables liées aux paramètres de la grille de jeu
    private int vert;
    private int hor;
    private int nbMurs;
    //private int nbVides; c'est juste le reste des cases
    private int nbTresors;
    private int nbTrous;
    //Case[][] grille;
    //Case[][] grille2;
    private Case [][] grille;
    private ArrayList<Coordinates> coordinatesMurs = new ArrayList<>();
    private ArrayList<Coordinates> coordinatesTrous = new ArrayList<>();
    private ArrayList<Coordinates> coordinatesTresors = new ArrayList<>();
    private Game game;




    //ArrayList<ArrayList<Case>> grille = new ArrayList<>();

    /**
     * Instantiates a new Plateau.
     *
     * @param hor       the hor
     * @param vert      the vert
     * @param nbTrous   the nb trous
     * @param nbTresors the nb tresors
     * @param nbMurs    the nb murs
     * @param game      the game
     */
    public Plateau(int hor, int vert, int nbTrous, int nbTresors, int nbMurs, Game game ) {
        this.vert=vert;
        this.hor=hor;
        this.nbTrous=nbTrous;
        this.nbTresors=nbTresors;
        this.nbMurs=nbMurs;
        grille = new Case[hor][vert];
        this.game=game;

        System.out.println("GENERATION DU PLATEAU");

       /* do {
            coordinatesTrous.clear();
            coordinatesMurs.clear();
            coordinatesTresors.clear();
            generate();
        } while (!parcoursProfondeur());*/
        coordinatesTrous.clear();
        coordinatesMurs.clear();
        coordinatesTresors.clear();
        generate();

        do {
            parcours();
        } while (!parcours());

        for(ClientHandler client : this.game.getPlayers()) {
            placerJoueurs(client);
        }

        System.out.println("PLATEAU GENERE");
    }

    /**
     * Hors limite boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean horsLimite (int x, int y) {
        return x < 0 || x >= hor || y < 0 || y >= vert;
    }



    private void reset() {
        for (int i=0; i<hor;i++) {
            for (int j=0; j<vert;j++) {
                grille[i][j].setMarked(false);
            }
        }
    }

    // Génère une grille en plaçant des éléments aléatoirement
    private void generate () {
        int tmpvert;
        int tmphor;
        int k=0;
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert; j++) {
                    grille[i][j] = new CaseVide(i,j);
            }
        }
        // Trous --------------------------------------------------
        for (int i=0; i<nbTrous; i++) {
            do {
                tmpvert = (int)(Math.random() * vert);
                tmphor = (int)(Math.random() * hor);
            } while (grille[tmphor][tmpvert] instanceof CaseTrou ||
                    !(grille[tmphor][tmpvert] instanceof CaseVide));
            grille[tmphor][tmpvert]=new CaseTrou(tmphor, tmpvert);
            coordinatesTrous.add(new Coordinates(tmphor, tmpvert));
            //System.out.println("trou à vert:"+tmpvert+" hor:"+tmphor);

        }
        // Trésors --------------------------------------------------------
        for (int i=0; i<nbTresors;i++) {
            do {
                tmpvert = (int)(Math.random() * vert);
                tmphor = (int)(Math.random() * hor);
            } while (grille[tmphor][tmpvert] instanceof CaseTresor
                    || !(grille[tmphor][tmpvert] instanceof CaseVide));
            int tmp = (int)(Math.random() * 4);
            int val = switch (tmp) {
                case 0 -> 5;
                case 1 -> 10;
                case 2 -> 15;
                case 3 -> 20;
                default -> 5;
            };
            grille[tmphor][tmpvert]=new CaseTresor(tmphor,tmpvert,val);
            coordinatesTresors.add(new Coordinates(tmphor, tmpvert, val));
        }

        for (int i=0; i<nbMurs;i++) {
            do {
                tmpvert = (int)(Math.random() * vert);
                tmphor = (int)(Math.random() * hor);
            } while (grille[tmphor][tmpvert] instanceof CaseMur
                    ||!(grille[tmphor][tmpvert] instanceof CaseVide));
            grille[tmphor][tmpvert]=new CaseMur(tmphor,tmpvert);
            coordinatesMurs.add(new Coordinates(tmphor, tmpvert));
        }
    }

    private boolean connexe(int mI, int mJ) {
        //System.out.println("appel connexe()");
        for (int i=0;i<hor;i++) {
            for (int j=0;j<vert;j++) {
                if (!(grille[i][j].isMarked()) && (grille[i][j] instanceof CaseVide || grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseTrou) ) {
                    System.out.println(i+" "+j+" pas connexe");
                    if (!chercheConnexe(i,j)) {
                        smartSuppr(i, j, mI, mJ);
                        //suppr(i,j,mI,mJ);
                    }
                    return false;
                    //trouve(i,j);
                    //return false;
                }
            }
        }
        //System.out.println("est enfin connexe");
        return true;
    }



    private boolean parcours() {
        reset();
        int tmpvert;
        int tmphor;
        do {
            tmpvert = (int)(Math.random() * vert);
            tmphor = (int)(Math.random() * hor);
        } while (!(grille[tmphor][tmpvert] instanceof CaseVide));
        System.out.println("on teste i="+tmphor+ " j="+tmpvert);
        explore(tmphor, tmpvert);
        return connexe(tmphor,tmpvert);

        //if (!connexe()) parcours();

        //if (!connexe()) parcours();
        /*for (int i=0; i<hor; i++) {
            for (int j=0; j<vert;j++) {
                if (!grille[i][j].isMarked()) {
                    chercheConnexe(i,j);
                    parcours();
                }
            }
        }*/
    }

    private void supprime(int initI, int initJ, int finI, int finJ) {
        int i=initI;
        int j=initJ;
        if(finJ>initJ) {

            while (j<finJ) {

                System.out.println("supprime "+i+" "+j);

                supprime(i, j);

                j++;
            }
            //return;
        }
        if(finJ<initJ) {

            while (j>finJ) {

                System.out.println("supprime "+i+" "+j);

                supprime(i, j);


                j--;
            }
            //return;
        }
        if(finI>initI) {

            while (i<finI) {
                System.out.println("supprime "+i+" "+j);

                supprime(i, j);

                i++;
            }
            ///return;
        }
        if(finI<initI) {

            while (i>finI) {
                System.out.println("supprime "+i+" "+j);
                supprime(i, j);

                i--;
            }
            //return;
        }
    }

    private boolean chercheConnexe(int i, int j) {
       // System.out.println("appel chercheConnexe()");

        int h = j;
        int b = j;
        int d = i;
        int g = i;
        while (d < hor || b < vert || g >= 0 || h >= 0) {
            //System.out.println("boucle");
            d++;
            b++;
            g--;
            h--;
            if (!horsLimite(d,j) && grille[d][j].isMarked() && (grille[d][j] instanceof CaseVide || grille[d][j] instanceof CaseTresor)) {
                System.out.println(i+" "+j+"est marquee");
                supprime(i, j, d, j);
                return true;
            }
            if (!horsLimite(g,j) &&  grille[g][j].isMarked() && (grille[g][j] instanceof CaseVide || grille[g][j] instanceof CaseTresor)) {
                //System.out.println("gauche");
                supprime(i, j, g, j);
                return true ;
            }
            if (!horsLimite(i,h) && grille[i][h].isMarked() && (grille[i][h] instanceof CaseVide || grille[i][h] instanceof CaseTresor)) {
                //System.out.println("haut");

                supprime(i, j, i, h);
                return true;
            }
            if (!horsLimite(i,b) && grille[i][b].isMarked() && (grille[i][b] instanceof CaseVide || grille[i][b] instanceof CaseTresor)) {
                //System.out.println("bas");

                supprime(i, j, i, b);
                return true;
            }

        }
        System.out.println("pas trouvé "+i+ " "+j);
        return false;


    }




    private void supprime(int i, int j) {
        System.out.println("suppr i:"+i+" j:"+j);
        if (grille[i][j] instanceof CaseMur) {
            coordinatesMurs.removeIf(coordinates -> coordinates.getX() == i && coordinates.getY() == j);
            grille[i][j]=new CaseVide(i,j);
        }
        if (grille[i][j] instanceof CaseTrou) {
            coordinatesTrous.removeIf(coordinates -> coordinates.getX() == i && coordinates.getY() == j);
            grille[i][j]=new CaseVide(i,j);
        }

    }

    private void smartSuppr(int initI, int initJ, int finI, int finJ) {
        System.out.println("appel smart de ");
        int i=initI;
        int j=initJ;
        if (initI<=finI && initJ<=finJ) {
            while ( i<finI || j<finJ) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                    System.out.println("finito");
                    return;
                }
                if (i!=finI && j!=finJ) {
                    i++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                }
                if (i==finI) {
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i++;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                            System.out.println("finito");
                            return;
                        }
                    }
                }
            }
            return;
        }
        if (initI>=finI && initJ>=finJ) {
            while ((i>finI || j>finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                    System.out.println("finito");

                    return;
                }
                if (i!=finI && j!=finJ) {

                    i--;
                    supprime(i, j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                    j--;
                    supprime(i, j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                }
                if (i==finI) {
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i--;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                            System.out.println("finito");
                            return;
                        }
                    }
                }

            }
            return;
        }
        if (initI<=finI && initJ>=finJ) {
            while ( (i<finI || j>finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                    System.out.println("finito");

                    return;
                }
                if (i!=finI && j!=finJ) {
                    i++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                }
                if (i==finI) {
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i++;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                            System.out.println("finito");
                            return;
                        }
                    }
                }

            }
            return;
        }
        if (initI>=finI && initJ<=finJ) {
            while ((i>finI || j<finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                    System.out.println("finito");

                    return;
                }
                if (i!=finI && j!=finJ) {
                    i--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                }
                if (i==finI) {
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                        System.out.println("finito");
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i--;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseTresor || grille[i][j] instanceof CaseVide)) {
                            System.out.println("finito");
                            return;
                        }
                    }
                }


            }

        }
    }

    private void suppr (int initI, int initJ, int finI, int finJ) {
        System.out.println("appel suppr");
        int i=initI;
        int j=initJ;
        if (finI < initI) {
            while (!horsLimite(i,initJ) && i>=finI) {
                supprime(i, initJ);
                i--;
            }

        }
        if (finI > initI) {
            while (i<=finI) {
                supprime(i,initJ);
                i++;
            }

        }
        if (finJ < initJ) {
            while ( j>=finJ) {
                supprime(finI, j);
                j--;
            }
        }
        if (finJ > initJ) {
            while (j<=finJ) {
                supprime(finI,j);
                j++;
            }
        }

    }

    private void explore(int i, int j) {
        grille[i][j].setMarked(true);
        if (grille[i][j] instanceof CaseTrou || grille[i][j] instanceof CaseMur) return;

        if (!horsLimite(i,j+1) && !grille[i][j+1].isMarked()) explore(i,j+1);
        if (!horsLimite(i,j-1) && !grille[i][j-1].isMarked()) explore(i,j-1);
        if (!horsLimite(i+1,j) && !grille[i+1][j].isMarked()) explore(i+1,j);
        if (!horsLimite(i-1,j) && !grille[i-1][j].isMarked()) explore(i-1,j);
    }

    private void resetMarked(){
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert;j++) {
                grille[i][j].setMarked(false);
                grille[i][j].setMarkedForDestruction(false);
            }
        }
    }

    /**
     * Get case case.
     *
     * @param x the x
     * @param y the y
     * @return the case
     */
    public Case getCase(int x, int y){
        if (horsLimite(x, y)){
            return new ImaginaryCase();
        }
        return grille[x][y];
    }

    /**
     * Set case.
     *
     * @param c the c
     */
    public void setCase(Case c){
        grille[c.getX()][c.getY()] = c;
    }

    private void destroyCloseWall(int i, int j, Tracker tracker){
        grille[i][j].setMarkedForDestruction(true);
        if (grille[i][j] instanceof CaseMur && !tracker.getStatus()){
            System.out.println("ON DETRUIT LE MUR EN POSITION "+i+":"+j);
            grille[i][j] = new CaseVide(i,j);
            coordinatesMurs.removeIf(coordinates -> coordinates.getX() == i && coordinates.getY() == j);
            tracker.setStatus(true);
            return;
        }
        ArrayList<Integer> etapes = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Collections.shuffle(etapes);
        for (int e: etapes){
            switch (e){
                case 1:
                    if (!horsLimite(i,j+1) && !tracker.getStatus() && !grille[i][j+1].isMarkedForDestruction()) destroyCloseWall(i,j+1, tracker);
                    break;
                case 2:
                    if (!horsLimite(i,j-1) && !tracker.getStatus() && !grille[i][j-1].isMarkedForDestruction()) destroyCloseWall(i,j-1, tracker);
                    break;
                case 3:
                    if (!horsLimite(i+1,j) && !tracker.getStatus() && !grille[i+1][j].isMarkedForDestruction()) destroyCloseWall(i+1,j, tracker);
                    break;
                case 4:
                    if (!horsLimite(i-1,j) && !tracker.getStatus() && !grille[i-1][j].isMarkedForDestruction()) destroyCloseWall(i-1,j, tracker);
                    break;
            }
        }

    }

    private boolean estConnexe(int tmphor, int tmpvert) {
        int destroyed = 0;
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert;j++) {
                if (!(grille[i][j].isMarked()) && (grille[i][j] instanceof CaseVide || grille[i][j] instanceof CaseTresor)) {
                    System.out.println(i+", "+j+" n'est pas marquée");
                    //return false;

                    /*for (int y=j; y<vert; y++){
                        for(int x=0; x<max(i, hor-i); x++){
                            if(!horsLimite(i+x,j) && grille[x+i][j] instanceof CaseMur){
                                grille[x+i][j] = new CaseVide(x+i, j);
                                resetMarked();
                                explorer(tmphor, tmpvert);
                                return estConnexe(tmphor, tmpvert);
                            } else if(!horsLimite(i-x,j) && grille[i-x][j] instanceof CaseMur){
                                grille[i-x][j] = new CaseVide(i-x, j);
                                resetMarked();
                                explorer(tmphor, tmpvert);
                                return estConnexe(tmphor, tmpvert);
                            }
                        }
                    }*/
                    destroyCloseWall(i, j ,new Tracker());
                    destroyed += 1;
                    resetMarked();
                    explorer(tmphor, tmpvert);
                    return estConnexe(tmphor, tmpvert);

                }
            }
        }
        System.out.println("Pour valider le plateau, on a du détruire "+destroyed+" murs");
        return true;
    }

    // On vérifie à partir de chaque position si la grille est connexe
    private boolean parcoursProfondeur() {
        /*for (int i=0; i<vert; i++) {
            for (int j=0; j<hor; j++) {
                if (!grille[i][j].isMarked()) explorer(i,j);
                if (!estConnexe()) {
                    return false;
                }
                //if (i!=vert-1) desac();
            }
        }
        return true;*/
        int tmpvert = (int)(Math.random() * vert);
        int tmphor = (int)(Math.random() * hor);
        if (grille[tmphor][tmpvert] instanceof CaseVide){
            explorer(tmphor, tmpvert);
            return estConnexe(tmphor, tmpvert);
        } else {
            return parcoursProfondeur();
        }
    }

    private void explorer(int i, int j) {
        grille[i][j].setMarked(true);
        if (grille[i][j] instanceof CaseTrou || grille[i][j] instanceof CaseMur) return;
        if (!horsLimite(i,j+1) && !grille[i][j+1].isMarked()) explorer(i,j+1);
        if (!horsLimite(i,j-1) && !grille[i][j-1].isMarked()) explorer(i,j-1);
        if (!horsLimite(i+1,j) && !grille[i+1][j].isMarked()) explorer(i+1,j);
        if (!horsLimite(i-1,j) && !grille[i-1][j].isMarked()) explorer(i-1,j);
    }

    private void placerJoueurs(ClientHandler client) {
        int x = (int) (Math.random()*hor);
        int y = (int) (Math.random()*vert);
        if(this.grille[x][y] instanceof CaseVide && this.grille[x][y].getPlayerOn()==null) {
            this.grille[x][y].setPlayerOn(client);
            System.out.println("ON AFFECTE LES COORDONEES ("+x+", "+y+") AU CLIENT : "+client.getUsername());
            client.getClient().getCoordonnees().setX(x);
            client.getClient().getCoordonnees().setY(y);
        } else {
            placerJoueurs(client);
        }
    }

    /**
     * Gets coordinates murs.
     *
     * @return the coordinates murs
     */
    public ArrayList<Coordinates> getCoordinatesMurs() {
        return coordinatesMurs;
    }

    /**
     * Gets coordinates trous.
     *
     * @return the coordinates trous
     */
    public ArrayList<Coordinates> getCoordinatesTrous() {
        return coordinatesTrous;
    }

    /**
     * Gets coordinates tresors.
     *
     * @return the coordinates tresors
     */
    public ArrayList<Coordinates> getCoordinatesTresors() {
        return coordinatesTresors;
    }

    /*int x;
    int y;
    int nb_murs;
    int nb_vide;
    int nb_trs;
    int nb_trou;
    //Case[][] grille;
    //Case[][] grille2;


    ArrayList<ArrayList<Case>> grille = new ArrayList<>();


    public Plateau(int x, int y, int nbTrous, int nbTresors) {
        this.x=x;
        this.y=y;
        this.nb_trs=nbTrous;
        for (int i = 0; i < x; i++){
            grille.add(new ArrayList<>());
        }
    }*/

    /*
    public int caseTrs() {
        nb_trs=0;
        for (int i=1; i<grille.length-1; i++) {
            for (int j = 1; j < grille[i].length-1; j++) {
                if(grille[i][j].isTrs())
                    nb_trs++;
            }
        }
        return nb_trs;
    }

    public int caseMur() {
        nb_murs=0;
        for (int i=1; i<grille.length-1; i++) {
            for (int j = 1; j < grille[i].length-1; j++) {
                if(grille[i][j].isTrs())
                    nb_murs++;
            }
        }
        return nb_murs;
    }

    public int caseTrou() {
        nb_trou=0;
        for (int i=1; i<grille.length-1; i++) {
            for (int j = 1; j < grille[i].length-1; j++) {
                if(grille[i][j].isTrou())
                    nb_trou++;
            }
        }
        return nb_trou;
    }

    public int caseVide() {
        nb_vide=0;
        for (int i=1; i<grille.length-1; i++) {
            for (int j = 1; j < grille[i].length-1; j++) {
                if(grille[i][j].isVide())
                    nb_vide++;
            }
        }
        return nb_vide;
    }

    public void initialiser() {
        for(int i = 0; i < y + 2; i++) {
            for(int j = 0; j < x + 2; j++) {
                grille[i][j]= new Case(-1, i, j);
                grille2[i][j]= new Case(-1, i, j);
            }
        }
        for(int i=0;i<y+2;i++) {
            this.grille[i][0].type=9;
            this.grille[i][x + 1].type=9;
        }
        for(int j=0;j<x+2;j++) {
            this.grille[0][j].type=9;
            this.grille[y+1][j].type=9;
        }
    }

    public void initialisertst() {
        for(int i = 0; i < y + 2; i++) {
            for(int j = 0; j < x + 2; j++) {
                grille[i][j]= new Case(3, i, j);
                grille2[i][j]= new Case(-1, i, j);
            }
        }
        grille[5][5]= new Case(0, 5, 5);
        grille[6][5]= new Case(1, 5, 5);
        for(int i=0;i<y+2;i++) {
            this.grille[i][0].type=9;
            this.grille[i][x + 1].type=9;
        }
        for(int j=0;j<x+2;j++) {
            this.grille[0][j].type=9;
            this.grille[y+1][j].type=9;
        }
    }

    public boolean fin(){
        boolean bool1=true;
        for (int i = 1; i < grille.length-1; i++) {
            for (int j = 1; j < grille[i].length-1; j++) {
                if(grille[i][j].isTrs()){
                    bool1 = false;
                }
            }
        }
        return bool1;
    }

    public void plateauR(int nb_murs, int nb_trs, int nb_trou) {
        if(nb_murs> this.x*this.y || nb_trs> this.x*this.y || nb_trou> this.x*this.y || nb_vide> this.x*this.y)
            System.out.println("Valeurs non réglementaires");
        else {
            //remplir de vide
            for (int i = 1; i < grille.length - 1; i++) {
                for (int j = 1; j < grille[i].length - 1; j++) {
                    grille[i][j] = new Case (-1, i, j);
                }
            }
            //mettre les murs
            int a = nb_murs;
            while (a > 0) {
                int b = new Random().nextInt(x - 1);
                int c = new Random().nextInt(y - 1);
                if (grille[c][b].isVide()) {
                    grille[c][b].mur();
                    a--;
                }
            }
            //mettre les trous
            int d = nb_murs;
            while (d > 0) {
                int e = new Random().nextInt(x - 1);
                int f = new Random().nextInt(y - 1);
                if (grille[f][e].isVide()) {
                    grille[f][e].trou();
                    d--;
                }
            }
            //mettre les trésors
            int g = nb_trs;
            while (g > 0) {
                int h = new Random().nextInt(x - 1);
                int i = new Random().nextInt(y - 1);
                int z = 1 + new Random().nextInt(4);

                mettre des trésors de valeurs aléatoires (5,10,15,20) car on a dit dans Case.java
                que si .type vaut 1 ou 2 ou 3 ou 4 alors c'est un trésor de valeurs respective :
                5, 10, 15 ou 20.
                On ajoute 1 car on veut que le nombre aléatoire soit compris entre 1 et 4 et pas 0 et 3

                if (grille[i][h].isVide()) {
                    grille[i][h].type = z;
                    g--;
                }
            }
        }
    }

    public void afficheTout() {
        int a = 0;
        int b = 0;
        nb_vide=caseVide();
        nb_murs=caseMur();
        nb_trou=caseTrou();
        nb_trs=caseTrs();
        System.out.println("  Trous  : " + nb_trou);
        System.out.println(" Trésors : " + nb_trs);
        System.out.print("   ");
        while (b < this.x + 1) {
            System.out.print(b + " ");
            b++;
        }
        System.out.print(0 + " ");
        b++;
        System.out.println();
        while (b > -this.x - 1) {
            System.out.print("-");
            b--;
        }
        System.out.println();
        for (int i = 0; i < grille.length-1; i++) {
            for (int j = 0; j < grille[i].length; j++) {

                if (j == 0) {
                    System.out.print(a + "| ");
                    a++;
                    if(grille[i][j].type==-1) {
                        System.out.print("  ");
                    }else if(grille[i][j].type==9) {
                        System.out.print("* ");
                    }else if(grille[i][j].type==0) {
                        System.out.print("X ");
                    }else if(grille[i][j].type==5) {
                        System.out.print("O ");
                    }else {
                        System.out.print(grille[i][j].type+" ");
                    }
                }else {
                    if(grille[i][j].type==-1) {
                        System.out.print("  ");
                    }else if(grille[i][j].type==9) {
                        System.out.print("* ");
                    }else if(grille[i][j].type==0) {
                        System.out.print("X ");
                    }else if(grille[i][j].type==5) {
                        System.out.print("O ");
                    }else {
                        System.out.print(grille[i][j].type+" ");
                    }
                }

            }
            System.out.println();
        }
        int i=grille.length-1;
        for (int j = 0; j < grille[i].length; j++) {
            if (j == 0) {
                System.out.print(0 + "| ");
                a++;
                if(grille[i][j].type==-1) {
                    System.out.print("  ");
                }else if(grille[i][j].type==9) {
                    System.out.print("* ");
                }else if(grille[i][j].type==0) {
                    System.out.print("X ");
                }else if(grille[i][j].type==5) {
                    System.out.print("O ");
                }else {
                    System.out.print(grille[i][j].type+" ");
                }
            }else {
                if(grille[i][j].type==-1) {
                    System.out.print("  ");
                }else if(grille[i][j].type==9) {
                    System.out.print("* ");
                }else if(grille[i][j].type==0) {
                    System.out.print("X ");
                }else if(grille[i][j].type==5) {
                    System.out.print("O ");
                }else {
                    System.out.print(grille[i][j].type+" ");
                }
            }
        }
        System.out.println();
    }
*/
}