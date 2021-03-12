package Models;

//import java.util.ArrayList;
import java.util.Random;

public class Plateau {
    int x;
    int y;
    int nb_murs;
    int nb_vide;
    int nb_trs;
    int nb_trou;
    Case[][] grille;
    Case[][] grille2;


    //ArrayList<Case> L = new ArrayList<>();


    public Plateau(int x, int y, int nb_trs) {
        this.x=x;
        this.y=y;
        this.nb_trs=nb_trs;
        this.grille = new Case[y+2][x+2];
        this.grille2 = new Case[y+2][x+2];
    }

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

    /*public void initialisertst() {
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
    }*/

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
                /*
                mettre des trésors de valeurs aléatoires (5,10,15,20) car on a dit dans Case.java
                que si .type vaut 1 ou 2 ou 3 ou 4 alors c'est un trésor de valeurs respective :
                5, 10, 15 ou 20.
                On ajoute 1 car on veut que le nombre aléatoire soit compris entre 1 et 4 et pas 0 et 3
                 */
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

}
