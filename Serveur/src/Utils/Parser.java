package Utils;


import Apps.ConnectionHandler;
import Models.Games.Game;
import Models.Games.SpeedingContest;
import Models.Games.TourParTour;
import Models.Games.WarFog;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * This class is used to parse messages from clients,
 * create java objects of the command
 * and call methods to process them
 */
public class Parser {

    //Attributs

    private final ClientHandler client;
    private final ConnectionHandler mainHandler;
    private boolean goodClient = false;


    /**
     * Instantiates a new Parser.
     *
     * @param client  the client
     * @param handler the handler
     */
//Constructeur
    public Parser(ClientHandler client, ConnectionHandler handler) {
        this.client = client;
        this.mainHandler = handler;
    }




    //Méthodes
    private void illegalCommand(){
        client.send("550 ILLEGAL COMMAND. BYE");
        for (Integer id : client.getClient().getJoinedGames()){
            mainHandler.getAvailableGamesMap().get(id).removePlayer(client);
        }
        client.closeConnection();
        if (client.getClient().isLoggedIn()){
            mainHandler.usernamesSet.remove(client.getClient().getUsername());
        }
    }

    public boolean isGoodClient() {
        return goodClient;
    }

    public void setGoodClient(boolean goodClient) {
        this.goodClient = goodClient;
    }

    /**
     * Parse.
     *
     * @param response_text the response text
     */
//Méthode principale qui lit la commande envoyée par l'utilisateur et agit en fonction
    protected void parse(String response_text){
        System.out.println("PROCESSING COMMAND : "+ response_text);
        String[] response = response_text.split(" ");
        switch (response[0]){

            //110 CREATE mode SIZE x y HOLE h TRES n PLAYERS p ROBOTS r
            case "110":

                if(response.length == 14 && response[1].equals("CREATE") && response[3].equals("SIZE") &&
                        response[6].equals("HOLE") && response[8].equals("TRES") && response[10].equals("PLAYERS") &&
                        response[12].equals("ROBOTS") && NumberUtils.isNumeric(response[2]) &&
                        NumberUtils.isNumeric(response[4]) && NumberUtils.isNumeric(response[5]) &&
                        NumberUtils.isNumeric(response[7]) && NumberUtils.isNumeric(response[9]) &&
                        NumberUtils.isNumeric(response[11]) && (response[13].equals("false") || response[13].equals("true"))){

                    int gameMode = Integer.parseInt(response[2]);
                    int sizeX = Integer.parseInt(response[4]);
                    int sizeY = Integer.parseInt(response[5]);
                    int nbHoles = Integer.parseInt(response[7]);
                    int nbTres = Integer.parseInt(response[9]);
                    int nbPlayers = Integer.parseInt(response[11]);
                    boolean robots = Boolean.parseBoolean(response[13]);
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


                    if(nbTres > 1.5 * ((sizeX * sizeY) - nbWalls - nbHoles) / 20) {
                        client.send("556 TOO MUCH TREASURES");
                        break;
                    }


                    if(nbTres < 1) {
                        client.send("556 TREASURES CAN NOT BE LESS THAN 1");
                        break;
                    }
                    if(nbPlayers > ((sizeX * sizeY) - nbWalls - nbHoles - nbTres) / 20) {
                        client.send("556 TOO MANY PLAYERS");
                        break;
                    }

                    //Speeding contest
                    if(Integer.parseInt(response[2]) == 1) {
                        SpeedingContest partie = new SpeedingContest(sizeX, sizeY, nbTres, nbHoles, nbPlayers, robots, client, mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }

                    //Tour par tour
                    if(Integer.parseInt(response[2]) == 2) {
                        TourParTour partie = new TourParTour(sizeX, sizeY, nbTres, nbHoles, nbPlayers, robots, client, mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }

                    //Brouillard de guerre
                    if(Integer.parseInt(response[2]) == 3) {
                        WarFog partie = new WarFog(sizeX, sizeY, nbTres, nbHoles, nbPlayers, robots, client, mainHandler);
                        client.send("111 MAP CREATED " + partie.getId());

                    }


                }
                else {
                    illegalCommand();
                }
                break;

            // 120 GETLIST
            case "120":
                if (response.length == 2 && response[1].equals("GETLIST")){
                    client.send("121 NUMBER " + mainHandler.getAvailableGamesMap().size());
                    int i = 1;
                    for (int gameId : mainHandler.getAvailableGamesMap().keySet()){
                        client.send(String.format("121 MESS %d ID %d %d %d %d %d %d %d %s %b", i++, gameId,
                                mainHandler.getAvailableGamesMap().get(gameId).mode,
                                mainHandler.getAvailableGamesMap().get(gameId).getX(),
                                mainHandler.getAvailableGamesMap().get(gameId).getY(),
                                mainHandler.getAvailableGamesMap().get(gameId).getHoles(),
                                mainHandler.getAvailableGamesMap().get(gameId).getTreasures(),
                                mainHandler.getAvailableGamesMap().get(gameId).getMaxPlayers(),
                                mainHandler.getAvailableGamesMap().get(gameId).getOwner().getClient().getUsername(),
                                mainHandler.getAvailableGamesMap().get(gameId).isRobots()));
                    }
                }
                else {
                    illegalCommand();
                }
                break;

            case "130":
                if (response.length == 3 && response[1].equals("JOIN") && NumberUtils.isNumeric(response[2])){
                    client.getClient().getJoinedGames().add(Integer.parseInt(response[2]));
                    client.send("131 MAP "+ response[2]+ " JOINED");
                    mainHandler.getAvailableGamesMap().get(Integer.parseInt(response[2])).addPlayer(client);
                } else{
                    illegalCommand();
                }
                break;
            case "135":
                if (response.length == 3 && response[1].equals("LEAVE") && NumberUtils.isNumeric(response[2])){
                    this.mainHandler.getAvailableGamesMap().get(Integer.parseInt(response[2])).removePlayer(client);
                    this.client.getClient().getJoinedGames().remove((Integer) Integer.parseInt(response[2]));
                    client.send("136 REMOVED");
                } else {
                    illegalCommand();
                }
                break;
            case "122":
                mainHandler.getAvailableGamesMap().remove(Integer.parseInt(response[1]));
                client.send("123 REMOVED");
                break;

            case "420":
                if (response.length == 2 && response[1].equals("GETWALLS")){
                    // PURE RANDOM
                    client.send("421 NUMBER 8");
                    Game partie = mainHandler.getAvailableGamesMap().get(client.getClient().getJoinedGames().get(0));
                    ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<>();
                    for(int i = 0; i < 40; i++){
                        int x =  (int) (Math.random() * (partie.getX()));
                        int y =  (int) (Math.random() * (partie.getY()));
                        coordinates.add(new Pair<Integer, Integer>(x, y));
                    }
                    for(int i = 0; i < 8; i++){
                        client.send(String.format("421 MESS %d POS %d %d %d %d %d %d %d %d %d %d", i,
                                coordinates.get(5*i).getKey(),
                                coordinates.get(5*i).getValue(),
                                coordinates.get(5*i+1).getKey(),
                                coordinates.get(5*i+1).getValue(),
                                coordinates.get(5*i+2).getKey(),
                                coordinates.get(5*i+2).getValue(),
                                coordinates.get(5*i+3).getKey(),
                                coordinates.get(5*i+3).getValue(),
                                coordinates.get(5*i+4).getKey(),
                                coordinates.get(5*i+4).getValue()));
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
