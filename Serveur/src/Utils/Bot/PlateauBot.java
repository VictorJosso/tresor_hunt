package Utils.Bot;

import Models.Cases.Case;

public class PlateauBot {

    private Case[][] grille;

    public PlateauBot(int x, int y){
        grille = new Case[x][y];
    }

    public PlateauBot(Case[][] grille){
        this.grille = grille;
    }

    public PlateauBot copy(){
        return new PlateauBot(grille);
    }
}
