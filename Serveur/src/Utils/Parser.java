package Utils;


import Apps.ConnectionHandler;
import Models.Game;
import Models.SpeedingContest;
import Models.TourParTour;
import Models.WarFog;

import java.util.Locale;

/**
 * This class is used to parse messages from clients,
 * create java objects of the command
 * and call methods to process them
 */

public class Parser {

    //Attributs

    private final ClientHandler client;
    private final ConnectionHandler mainHandler;


    //Constructeur
    public Parser(ClientHandler client, ConnectionHandler handler) {
        this.client = client;
        this.mainHandler = handler;
    }




    //Méthodes
    private void illegalCommand(){
        client.send("550 ILLEGAL COMMAND. BYE");
        client.closeConnection();
    }





    //Méthode principale qui lit la commande envoyée par l'utilisateur et agit en fonction
    protected void parse(String response_text){
        System.out.println("PROCESSING COMMAND : "+ response_text);
        String[] response = response_text.split(" ");
        switch (response[0]){

            //110 CREATE mode SIZE x y HOLE h TRES n PLAYERS p
            case "110":

                if(response.length == 12 && response[1].equals("CREATE") && response[3].equals("SIZE") &&
                        response[6].equals("HOLE") && response[8].equals("TRES") && response[10].equals("PLAYERS") &&
                        NumberUtils.isNumeric(response[2]) && NumberUtils.isNumeric(response[4]) &&
                        NumberUtils.isNumeric(response[5]) && NumberUtils.isNumeric(response[7]) &&
                        NumberUtils.isNumeric(response[9]) && NumberUtils.isNumeric(response[11])) {

                    int gameMode = Integer.parseInt(response[2]);
                    int sizeX = Integer.parseInt(response[4]);
                    int sizeY = Integer.parseInt(response[5]);
                    int nbHoles = Integer.parseInt(response[7]);
                    int nbTres = Integer.parseInt(response[9]);
                    int nbPlayers = Integer.parseInt(response[11]);
                    int nbWalls = (sizeX * sizeY) / 5;

                    if(gameMode < 1 || gameMode > 3 ) {
                        client.send("556 INVALID GAMEMODE");
                        break;
                    }


                    if((sizeX * sizeY) < 50) {
                        client.send("556 TOO SMALL MAP");
                        break;
                    }

                    if (sizeX < 5 || sizeX > 100){
                        client.send("556 DIMENSION ERROR (X)");
                        break;
                    }

                    if (sizeY < 5 || sizeY > 100){
                        client.send("556 DIMENSION ERROR (Y)");
                        break;
                    }

                    if(nbHoles > (3 * (sizeX * sizeY)) / 25) {
                        client.send("556 TOO MUCH HOLES");
                        break;
                    }


                    if(nbHoles < 0) {
                        client.send("556 HOLES CAN NOT BE NEGATIVE");
                        break;
                    }


                    if(nbTres > 1.5 * ((sizeX * sizeY) - nbWalls - nbHoles - nbTres) / 20) {
                        client.send("556 TOO MUCH TREASURES");
                        break;
                    }


                    if(nbTres < 0) {
                        client.send("556 TREASURES CAN NOT BE NEGATIVE");
                        break;
                    }
                    if(nbPlayers > ((sizeX * sizeY) - nbWalls - nbHoles - nbTres) / 20) {
                        client.send("556 TOO MANY PLAYERS");
                        break;
                    }

                    //Speeding contest
                    if(Integer.parseInt(response[2]) == 1) {
                        SpeedingContest partie = new SpeedingContest(sizeX, sizeY, nbTres, nbHoles, client, mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }

                    //Tour par tour
                    if(Integer.parseInt(response[2]) == 2) {
                        TourParTour partie = new TourParTour(sizeX, sizeY, nbTres, nbHoles, client,  mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }

                    //Brouillard de guerre
                    if(Integer.parseInt(response[2]) == 3) {
                        WarFog partie = new WarFog(sizeX, sizeY, nbTres, nbHoles, client, mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }


                }
                else {
                    illegalCommand();
                }
                break;


            default:
                illegalCommand();
                break;
        }
    }
}
