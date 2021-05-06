package Utils.Bot;

import Models.Cases.CaseMur;
import Models.Cases.CaseTresor;
import Models.Cases.CaseTrou;
import Models.Cases.CaseVide;
import Utils.Coordinates;
import com.sun.jdi.DoubleValue;

import java.util.ArrayList;

public class Decision implements Runnable {
    // Classe pour décision de mouvement.
    private final PlateauBot plateau_off;
    private final String targetPlayer;
    private final String me;
    private final Bot botInstance;
    private String[] directions = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};

    public Decision(Bot botInstance, PlateauBot plateau_off, String me, String targetPlayer) {
        this.botInstance = botInstance;
        this.plateau_off = plateau_off;
        this.targetPlayer = targetPlayer;
        this.me = me;
    }

    private double evaluate(PlateauBot plateau, String playerName, int coeff){
        Coordinates c = plateau.getPlayerPosition(playerName);
        if (plateau.getGrille()[c.getX()][c.getY()] instanceof CaseTresor) {
            double val = ((CaseTresor) plateau.getGrille()[c.getX()][c.getY()]).getValue();
            plateau.getGrille()[c.getX()][c.getY()] = new CaseVide(c.getX(), c.getY());
            return coeff * val;
        } else if (plateau.getGrille()[c.getX()][c.getY()] instanceof CaseTrou || plateau.getGrille()[c.getX()][c.getY()] instanceof CaseMur){
            return coeff * Double.NEGATIVE_INFINITY;
        }
        return 0;
    }

    private Solution miniMax(PlateauBot plateau, int depth, boolean maximizingPlayer, double previousEvaluationMax, double previousEvaluationMin){
        if (depth == 0){
            if (maximizingPlayer){
                double eval = evaluate(plateau, this.me, 1) + previousEvaluationMax;
                return new Solution(plateau.getPlayerPosition(this.me), eval);
            } else {
                double eval = previousEvaluationMin + evaluate(plateau, this.targetPlayer, -1);
                return new Solution(plateau.getPlayerPosition(this.targetPlayer), eval);
            }
        }

        if (maximizingPlayer){
            Solution maxEval = new Solution(plateau.getPlayerPosition(this.me), Double.NEGATIVE_INFINITY);
            for (String direction: directions){
                PlateauBot copie = plateau.copy();
                switch (direction) {
                    case "UP" -> copie.getPlayerPosition(this.me).addToY(-1);
                    case "DOWN" -> copie.getPlayerPosition(this.me).addToY(1);
                    case "LEFT" -> copie.getPlayerPosition(this.me).addToX(-1);
                    case "RIGHT" -> copie.getPlayerPosition(this.me).addToX(1);
                }
                Coordinates save = copie.getPlayerPosition(this.me);
                if(copie.getPlayerPosition(this.me).getX() < 0 ||
                copie.getPlayerPosition(this.me).getX() >= copie.getDim_x() ||
                copie.getPlayerPosition(this.me).getY() < 0 ||
                copie.getPlayerPosition(this.me).getY() >= copie.getDim_y()){
                    continue;
                }
                double eval = evaluate(copie, this.me, 1);
                if (eval == Double.NEGATIVE_INFINITY){
                    continue;
                }
                Solution nextSolution = miniMax(copie, depth-1, false, previousEvaluationMax+eval, previousEvaluationMin);
                //System.out.println("Evaluation en ("+nextSolution.getCoordinates().getX()+", "+nextSolution.getCoordinates().getY()+") : "+nextSolution.getScore());
                if (nextSolution.getScore() > maxEval.getScore()){
                    maxEval.setScore(nextSolution.getScore());
                    maxEval.setCoordinates(save);
                }
            }
            return maxEval;
        } else {
            Solution minEval = new Solution(plateau.getPlayerPosition(this.me), Double.POSITIVE_INFINITY);
            for (String direction: directions){
                PlateauBot copie = plateau.copy();
                switch (direction){
                    case "UP" -> copie.getPlayerPosition(this.targetPlayer).addToY(-1);
                    case "DOWN" -> copie.getPlayerPosition(this.targetPlayer).addToY(1);
                    case "LEFT" -> copie.getPlayerPosition(this.targetPlayer).addToX(-1);
                    case "RIGHT" -> copie.getPlayerPosition(this.targetPlayer).addToX(1);
                }
                Coordinates save = copie.getPlayerPosition(this.targetPlayer);
                if(copie.getPlayerPosition(this.targetPlayer).getX() < 0 ||
                        copie.getPlayerPosition(this.targetPlayer).getX() >= copie.getDim_x() ||
                        copie.getPlayerPosition(this.targetPlayer).getY() < 0 ||
                        copie.getPlayerPosition(this.targetPlayer).getY() >= copie.getDim_y()){
                    continue;
                }
                double eval = evaluate(copie, this.targetPlayer, -1);
                if (eval == Double.POSITIVE_INFINITY){
                    continue;
                }
                Solution nextSolution = miniMax(copie, depth-1, true, previousEvaluationMax, previousEvaluationMin+eval);
                if (nextSolution.getScore() < minEval.getScore()){
                    minEval.setScore(nextSolution.getScore());
                    minEval.setCoordinates(save);
                }
            }
            return minEval;
        }
    }




    private String chooseRandom(PlateauBot plateau){
        String choice = directions[(int) (Math.random() * 4)];
        int x_d = choice.equals("LEFT") ? -1 : choice.equals("RIGHT") ? 1 : 0;
        int y_d = choice.equals("UP") ? -1 : choice.equals("DOWN") ? 1 : 0;
        Coordinates playerPosition = plateau.getPlayerPosition(this.me);
        if (playerPosition.getX()+x_d >= plateau.getDim_x() || playerPosition.getX()+x_d < 0 || playerPosition.getY()+y_d < 0 || playerPosition.getY()+y_d >= plateau.getDim_y() || plateau.getGrille()[playerPosition.getX()+x_d][playerPosition.getY()+y_d] instanceof CaseMur ||
        plateau.getGrille()[playerPosition.getX()+x_d][playerPosition.getY()+y_d] instanceof CaseTrou){
            return chooseRandom(plateau);
        } else {
            return choice;
        }
    }

    private double[][] initDistances(PlateauBot plateau){
        double[][] d = new double[plateau.getDim_x()][plateau.getDim_y()];
        for (int x = 0; x < plateau.getDim_x(); x++){
            for (int y = 0; y < plateau.getDim_y(); y++){
                d[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        d[plateau.getPlayerPosition(this.me).getX()][plateau.getPlayerPosition(this.me).getY()] = 0;
        return d;
    }

    private ArrayList<Coordinates> initComplementaire(PlateauBot plateau){
        ArrayList<Coordinates> complementaire = new ArrayList<>();
        for (int x = 0; x < plateau.getDim_x(); x++){
            for (int y = 0; y < plateau.getDim_y(); y++){
                if ((plateau.getGrille()[x][y] instanceof CaseVide || plateau.getGrille()[x][y] instanceof CaseTresor) && (!plateau.isThereAPlayerThere(x, y) || (plateau.getPlayerPosition(this.me).getX() == x && plateau.getPlayerPosition(this.me).getY() == y))){
                    complementaire.add(new Coordinates(x, y));
                }
            }
        }
        return complementaire;
    }

    private Coordinates[][] initPredecesseurs(PlateauBot plateau){
        Coordinates[][] predecesseurs = new Coordinates[plateau.getDim_x()][plateau.getDim_y()];
        for (int x = 0; x < plateau.getDim_x(); x++){
            for (int y = 0; y < plateau.getDim_y(); y++){
                predecesseurs[x][y] = null;
            }
        }
        return predecesseurs;
    }

    private double poids(PlateauBot plateau, Coordinates s1, Coordinates s2){
        if ((Math.abs(s1.getX()-s2.getX()) == 1 && s1.getY() - s2.getY() == 0) || (Math.abs(s1.getY()-s2.getY()) == 1 && s1.getX() - s2.getX() == 0)){
            if (plateau.getGrille()[s1.getX()][s1.getY()] instanceof CaseMur || plateau.getGrille()[s2.getX()][s2.getY()] instanceof CaseMur ||
                    plateau.getGrille()[s1.getX()][s1.getY()] instanceof CaseTrou || plateau.getGrille()[s2.getX()][s2.getY()] instanceof CaseTrou ||
                    (plateau.isThereAPlayerThere(s2.getX(), s2.getY()) && Math.abs(s1.getY() + s1.getX() - s2.getY() - s2.getX()) == 1)){
                return Double.POSITIVE_INFINITY;
            } else {
                return 1;
            }
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    private Coordinates findClosest(ArrayList<Coordinates> complementaire, double[][] distances){
        double mini = Double.POSITIVE_INFINITY;
        Coordinates sommet = null;
        for (Coordinates c: complementaire){
            if (distances[c.getX()][c.getY()] < mini){
                mini = distances[c.getX()][c.getY()];
                sommet = c;
            }
        }
        return sommet;
    }

    private void updateDistances(PlateauBot plateau, double[][] distances, Coordinates[][] predecesseurs, Coordinates s1, Coordinates s2){
        if (distances[s2.getX()][s2.getY()] > (distances[s1.getX()][s1.getY()] + poids(plateau, s1, s2))){
            distances[s2.getX()][s2.getY()] = (distances[s1.getX()][s1.getY()] + poids(plateau, s1, s2));
            predecesseurs[s2.getX()][s2.getY()] = s1;
        }
    }

    private DijkstraMap dijkstra(PlateauBot plateau) {
        double[][] distances = initDistances(plateau);
        ArrayList<Coordinates> complementaire = initComplementaire(plateau);
        Coordinates[][] predecesseurs = initPredecesseurs(plateau);
        while (!complementaire.isEmpty()) {
            Coordinates s1 = findClosest(complementaire, distances);
            if (s1 == null) {
                complementaire.clear();
                continue;
            }
            complementaire.remove(s1);
            for (String direction : directions) {
                Coordinates s2 = s1.copy();
                switch (direction) {
                    case "UP" -> s2.addToY(-1);
                    case "DOWN" -> s2.addToY(1);
                    case "LEFT" -> s2.addToX(-1);
                    case "RIGHT" -> s2.addToX(1);
                }
                if (s2.getX() < 0 || s2.getX() >= plateau.getDim_x() || s2.getY() < 0 || s2.getY() >= plateau.getDim_y()) {
                    continue;
                }
                updateDistances(plateau, distances, predecesseurs, s1, s2);
            }
        }
        return new DijkstraMap(distances, predecesseurs);
    }

    private Solution findPath(PlateauBot plateau, DijkstraMap map, int targetX, int targetY){
        double[][] distances = map.getDistances();
        Coordinates[][] predecesseurs = map.getPredecesseurs();
        ArrayList<Coordinates> chemin = new ArrayList<>();
        Coordinates s = new Coordinates(targetX, targetY);
        while (s.getX() != plateau.getPlayerPosition(this.me).getX() || s.getY() != plateau.getPlayerPosition(this.me).getY()){
            chemin.add(s);
            s = predecesseurs[s.getX()][s.getY()];
            if (s == null){
                System.out.println("Erreur dans la matrice #2");
                return new Solution(new Coordinates(-1, -1), Double.POSITIVE_INFINITY);
            }
        }
        return new Solution(chemin.get(chemin.size() - 1), distances[targetX][targetY]);
    }

    private Solution randomSol(PlateauBot plateau, int dir){
        int[][] movs = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Coordinates s2 = plateau.getPlayerPosition(this.me);
        s2.addToX(movs[dir][0]);
        s2.addToY(movs[dir][1]);
        if (poids(plateau, plateau.getPlayerPosition(this.me), s2) == 1){
            return new Solution(s2, 1);
        } else {
            return randomSol(plateau, (dir+1)%4);
        }
    }

    private Solution pasOuf(PlateauBot plateau){
        Solution bestSolution = new Solution(null, Double.POSITIVE_INFINITY);
        DijkstraMap map = dijkstra(plateau);
        for (Coordinates c: plateau.getTreasuresList()){
            Solution s = findPath(plateau, map, c.getX(), c.getY());
            if (s.getScore() < bestSolution.getScore()){
                bestSolution = s;
            }
        }
        if (bestSolution.getCoordinates() == null){
            return randomSol(plateau, 0);
        }
        return bestSolution;
    }

    @Override
    public void run() {
        // TODO : Faire l'aglo de décision
        System.out.println("|| [BOT] || : START THINKING AT POS = ("+plateau_off.getPlayerPosition(this.me).getX()+", "+this.plateau_off.getPlayerPosition(this.me).getY()+")");
        //Solution sol = miniMax(plateau_off, 12, true, 0,0);
        Solution sol = pasOuf(plateau_off.copy());
        System.out.println("|| [BOT] || : BEST SOLUTION = ("+sol.getCoordinates().getX()+", "+sol.getCoordinates().getY()+") with score "+sol.getScore());
        if (sol.getCoordinates().getX() - plateau_off.getPlayerPosition(this.me).getX() == 1){
            this.botInstance.iAmDone("RIGHT");
        } else if (sol.getCoordinates().getX() - plateau_off.getPlayerPosition(this.me).getX() == -1){
            this.botInstance.iAmDone("LEFT");
        } else if (sol.getCoordinates().getY() - plateau_off.getPlayerPosition(this.me).getY() == 1){
            this.botInstance.iAmDone("DOWN");
        } else if (sol.getCoordinates().getY() - plateau_off.getPlayerPosition(this.me).getY() == -1){
            this.botInstance.iAmDone("UP");
        } else {
            System.err.println("ERREUR DE SOLUTION ! X="+sol.getCoordinates().getX()+" & Y="+sol.getCoordinates().getY());
            this.botInstance.iAmDone(this.chooseRandom(this.plateau_off));
        }
        //this.botInstance.iAmDone(directions[(int) (Math.random() * 4)]);
    }
}
