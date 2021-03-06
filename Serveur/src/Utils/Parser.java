package Utils;


import Apps.ConnectionHandler;
import Models.Cases.Case;
import Models.Cases.CaseTresor;
import Models.Cases.CaseTrou;
import Models.Games.Game;
import Models.Games.SpeedingContest;
import Models.Games.TourParTour;
import Models.Games.WarFog;

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
        for (Integer id : client.getJoinedGames()){
            mainHandler.getAvailableGamesMap().get(id).removePlayer(client);
        }
        client.closeConnection();
        if (client.isLoggedIn()){
            mainHandler.usernamesSet.remove(client.getUsername());
        }
    }

    /**
     * Is good client boolean.
     *
     * @return the boolean
     */
    public boolean isGoodClient() {
        return goodClient;
    }

    /**
     * Sets good client.
     *
     * @param goodClient the good client
     */
    public void setGoodClient(boolean goodClient) {
        this.goodClient = goodClient;
    }

    /**
     * Parse.
     *
     * @param response_text the response text
     */
//Méthode principale qui lit la commande envoyée par l'utilisateur et agit en fonction
    public void parse(String response_text){
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
                        System.out.println("creat de warfog");
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
                    //System.out.println("jeux disponibles : "+mainHandler.getAvailableGamesMap());
                    for (int gameId : mainHandler.getAvailableGamesMap().keySet()){
                        StringBuilder builder = new StringBuilder("121 MESS ");
                        builder.append(i++);
                        builder.append(" ID ");
                        builder.append(gameId);
                        builder.append(" ");
                        builder.append(mainHandler.getAvailableGamesMap().get(gameId).mode);
                        builder.append(" ");
                        builder.append(mainHandler.getAvailableGamesMap().get(gameId).getX());
                        builder.append(" ");
                        builder.append(mainHandler.getAvailableGamesMap().get(gameId).getY());
                        builder.append(" ");
                        builder.append(mainHandler.getAvailableGamesMap().get(gameId).getHoles());
                        builder.append(" ");
                        builder.append(mainHandler.getAvailableGamesMap().get(gameId).getTreasures());
                        if(client.isGoodClient()){
                            builder.append(" ");
                            builder.append(mainHandler.getAvailableGamesMap().get(gameId).getMaxPlayers());
                            builder.append(" ");
                            builder.append(mainHandler.getAvailableGamesMap().get(gameId).getOwner().getUsername());
                            builder.append(" ");
                            builder.append(mainHandler.getAvailableGamesMap().get(gameId).isRobots());

                        }

                        /*client.send(String.format("121 MESS %d ID %d %d %d %d %d %d %d %s %b", i++, gameId,
                                mainHandler.getAvailableGamesMap().get(gameId).mode,
                                mainHandler.getAvailableGamesMap().get(gameId).getX(),
                                mainHandler.getAvailableGamesMap().get(gameId).getY(),
                                mainHandler.getAvailableGamesMap().get(gameId).getHoles(),
                                mainHandler.getAvailableGamesMap().get(gameId).getTreasures(),
                                mainHandler.getAvailableGamesMap().get(gameId).getMaxPlayers(),
                                mainHandler.getAvailableGamesMap().get(gameId).getOwner().getUsername(),
                                mainHandler.getAvailableGamesMap().get(gameId).isRobots()));*/
                        client.send(builder.toString());
                    }

                }
                else {
                    illegalCommand();
                }
                break;

            case "130":
                if (response.length == 3 && response[1].equals("JOIN") && NumberUtils.isNumeric(response[2])){
                    client.getJoinedGames().clear();
                    if(client.getClient().getGameRunning() != null){
                        client.getClient().getGameRunning().removePlayer(client);
                    }
                    client.getJoinedGames().add(Integer.parseInt(response[2]));
                    client.send("131 MAP "+ response[2]+ " JOINED");
                    mainHandler.getAvailableGamesMap().get(Integer.parseInt(response[2])).addPlayer(client);
                } else{
                    illegalCommand();
                }
                break;
            case "135":
                if (response.length == 3 && response[1].equals("LEAVE") && NumberUtils.isNumeric(response[2])){
                    this.mainHandler.getAvailableGamesMap().get(Integer.parseInt(response[2])).removePlayer(client);
                    this.client.getJoinedGames().remove((Integer) Integer.parseInt(response[2]));
                    client.send("136 REMOVED");
                } else {
                    illegalCommand();
                }
                break;
            case "141":
            case "512":

            case "521":
            case "321":
            case "331":
            case "341":
                break;
            case "122":
                mainHandler.getAvailableGamesMap().remove(Integer.parseInt(response[1]));
                client.send("123 REMOVED");
                break;

            case "200":
                if(response.length == 2 && (response[1].equals("GOUP") || response[1].equals("GODOWN") || response[1].equals("GOLEFT") || response[1].equals("GORIGHT")) && client.getClient().getGameRunning() != null){
                    int status = client.getClient().getGameRunning().movePlayer(client, response[1]);
                    switch (status) {
                        case -2 -> client.send("902 NOT YOUR TURN");
                        case -1 -> client.send("202 MOVE BLOCKED");
                        case 0 -> {
                            client.send("201 MOVE OK");
                            client.getClient().getGameRunning().broadcast("510 " + client.getUsername() + " POS " + client.getClient().getCoordonnees().getX() + " " + client.getClient().getCoordonnees().getY(), client);
                            client.getClient().getGameRunning().sendHoles();
                            client.getClient().getGameRunning().sendWalls();

                        }
                        case 1 -> {
                            client.send("666 MOVE HOLE DEAD");
                            client.getClient().getGameRunning().broadcast("510 " + client.getUsername() + " POS " + client.getClient().getCoordonnees().getX() + " " + client.getClient().getCoordonnees().getY(), client);
                            client.getClient().getGameRunning().broadcast("520 " + client.getUsername() + " DIED");
                        }
                        default -> {
                            client.send("203 MOVE OK TRES " + status);
                            client.getClient().getGameRunning().broadcast("511 " + client.getUsername() + " POS " + client.getClient().getCoordonnees().getX() + " " + client.getClient().getCoordonnees().getY() + " TRES " + status, client);
                            client.getClient().getGameRunning().getPlateau();

                            client.getClient().getGameRunning().sendHoles();
                            client.getClient().getGameRunning().sendWalls();
                        }
                    }
                } else {
                    illegalCommand();
                }
                break;

            case "400":
                if (response.length == 2 && response[1].equals("GETHOLES")) {
                    // PURE RANDOM
                    Game partie = client.getClient().getGameRunning();
                    ArrayList<Coordinates> coordinates = partie.getPlateau().getCoordinatesTrous();
                    client.send("401 NUMBER " + (int) Math.ceil((double) coordinates.size()));
                    for (int i = 0; i < (int) Math.ceil((double) coordinates.size() / 5); i++) {
                        StringBuilder message = new StringBuilder("401 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < coordinates.size() && j < 5; j++) {
                                message.append(" ").append(coordinates.get(5 * i + j).getX()).append(" ").append(coordinates.get(5 * i + j).getY());
                        }
                        client.send(message.toString());

                    }
                    partie.sendPositions(client);

                }
                else {
                    illegalCommand();
                }
                break;

            case "410":
                if (response.length == 2 && response[1].equals("GETTREASURES")) {
                    // PURE RANDOM
                    Game partie = client.getClient().getGameRunning();
                    ArrayList<Coordinates> coordinates = partie.getPlateau().getCoordinatesTresors();
                    client.send("411 NUMBER " + (int) Math.ceil((double) coordinates.size()));
                    for (int i = 0; i < (int) Math.ceil((double) coordinates.size() / 5); i++) {
                        StringBuilder message = new StringBuilder("411 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < coordinates.size() && j < 5; j++) {
                            Case tres = partie.getPlateau().getCase(coordinates.get(5*i+j).getX(),coordinates.get(i*5+j).getY());
                            if (tres instanceof CaseTrou && ((CaseTresor) tres).isSecret()) {
                                message.append(" ").append(coordinates.get(5 * i + j).getX()).append(" ").append(coordinates.get(5 * i + j).getY()).append(" ").append(0);
                            } else {
                                message.append(" ").append(coordinates.get(5 * i + j).getX()).append(" ").append(coordinates.get(5 * i + j).getY()).append(" ").append(coordinates.get(5 * i + j).getValue());
                            }
                        }
                        client.send(message.toString());
                    }
                }
                else {
                    illegalCommand();
                }
                break;

            case "420":
                if (response.length == 2 && response[1].equals("GETWALLS")) {
                    // PURE RANDOM
                    Game partie = client.getClient().getGameRunning();
                    ArrayList<Coordinates> coordinates = partie.getPlateau().getCoordinatesMurs();
                    client.send("421 NUMBER " + (int) Math.ceil((double) coordinates.size()));
                    for (int i = 0; i < (int) Math.ceil((double) coordinates.size() / 5); i++) {
                        StringBuilder message = new StringBuilder("421 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < coordinates.size() && j < 5; j++) {
                            message.append(" ").append(coordinates.get(5 * i + j).getX()).append(" ").append(coordinates.get(5 * i + j).getY());
                        }
                        client.send(message.toString());
                    }
                }
                else {
                    illegalCommand();
                }
                break;

            case "150":
                if (response.length == 3 && response[1].equals("REQUEST") && response[2].equals("START")){
                    boolean result = mainHandler.getAvailableGamesMap().get(client.getJoinedGames().get(0)).requestStart(client);
                    if (!result){
                        client.send("151 REFUSED");
                    }
                } else {
                    illegalCommand();
                }
                break;
            case "152":
                if (response.length == 3 && response[1].equals("START") && (response[2].equals("YES") || response[2].equals("NO"))){
                    mainHandler.getAvailableGamesMap().get(client.getJoinedGames().get(0)).startRequestStatus(client, response[2].equals("YES"));
                } else {
                    illegalCommand();
                }
                break;
            case "300":
                if (response.length == 3 && response[1].equals("REVEAL") && response[2].equals("HOLE")) {
                    // définir prix : fixé à 20
                    if (client.getClient().getScore()>=20) {
                        client.getClient().lowScore(20);
                        client.send("301 PAYMENT VALIDATED");
                    } else {
                        client.send("905 Not enough point");
                        break;
                    }
                    int x = client.getClient().getCoordonnees().getX();
                    int y = client.getClient().getCoordonnees().getY();
                    // envoyer les trous :
                    Game partie = client.getClient().getGameRunning();
                    ArrayList<Coordinates> coordinates = partie.getPlateau().getCoordinatesTrous();
                    ArrayList<Coordinates> holes = new ArrayList<>();
                    client.send("320 SENDING HOLES");

                    for (Coordinates c: coordinates) {
                        for (int i=x-1;i<x+1;i++) {
                            for (int j=y-1;j<y+1;j++) {
                                if (c.getX()==i && c.getY()==j) {
                                    holes.add(c);
                                }
                            }
                        }
                    }
                    client.send("320 NUMBER " + (int) Math.ceil((double) holes.size()));
                    for (int i = 0; i < (int) Math.ceil((double) holes.size() /5); i++) {
                        StringBuilder message = new StringBuilder("320 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < holes.size() && j < 5; j++) {
                            message.append(" ").append(holes.get(5 * i + j).getX()).append(" ").append(holes.get(5 * i + j).getY());
                        }
                        client.send(message.toString());
                    }

                } else {
                    illegalCommand();
                }

                break;



            case "310":
                if (response.length == 5 && response[1].equals("REVEAL") && response[2].equals("MAP")) {

                    int x = Integer.parseInt(response[3]);
                    int y = Integer.parseInt(response[4]);

                    if (client.getClient().getScore()>=50) {
                        client.getClient().lowScore(50);
                        client.send("311 PAYMENT VALIDATED"+x+y);
                    } else {
                        client.send("905 Not enough point");
                        break;
                    }






                    // On envoie tout d'abord les trous
                    Game partie = client.getClient().getGameRunning();
                    ArrayList<Coordinates> coordinates = partie.getPlateau().getCoordinatesTrous();
                    ArrayList<Coordinates> holes = new ArrayList<>();

                    client.send("320 SENDING HOLES");

                    for (Coordinates c: coordinates) {
                        for (int i=x-1;i<x+1;i++) {
                            for (int j=y-1;j<y+1;j++) {
                                if (c.getX()==i && c.getY()==j) {
                                    holes.add(c);
                                }
                            }
                        }
                    }

                    client.send("320 NUMBER " + (int) Math.ceil((double) holes.size()));
                    for (int i = 0; i < (int) Math.ceil((double) holes.size() /5); i++) {
                        StringBuilder message = new StringBuilder("320 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < holes.size() && j < 5; j++) {
                            message.append(" ").append(holes.get(5 * i + j).getX()).append(" ").append(holes.get(5 * i + j).getY());
                        }
                        client.send(message.toString());
                    }




                    // on envoie les murs ensuite
                    coordinates = partie.getPlateau().getCoordinatesMurs();
                    ArrayList<Coordinates> walls = new ArrayList<>();
                    client.send("330 SENDING WALLS");

                    //On ajoute dans l'arraylist walls toutes les cases de la zone qui nous intéresse
                    for (Coordinates c: coordinates) {
                        for (int i=x;i<x+4;i++) {
                            for (int j=y;j<y+4;j++) {
                                if (c.getX()==i && c.getY()==j) {
                                    walls.add(c);
                                }
                            }
                        }
                    }

                    client.send("330 NUMBER " + (int) Math.ceil((double) walls.size()));
                    for (int i = 0; i < (int) Math.ceil((double) walls.size() /5); i++) {
                        StringBuilder message = new StringBuilder("320 MESS " + String.valueOf(i) + " POS");
                        for (int j = 0; 5 * i + j < walls.size() && j < 5; j++) {
                            message.append(" ").append(walls.get(5 * i + j).getX()).append(" ").append(walls.get(5 * i + j).getY());
                        }
                        client.send(message.toString());
                    }




                } else {
                    client.send("c pas bon ?");
                    illegalCommand();
                }
                break;


            default:
                illegalCommand();
                break;
        }
    }
}
