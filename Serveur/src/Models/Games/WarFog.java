package Models.Games;

import Apps.ConnectionHandler;
import Models.Client;
import Utils.ClientHandler;
import Utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type War fog.
 */
public class WarFog extends Game {

    private int currentPlayerIndex;
    private CopyOnWriteArrayList<ClientHandler> stillAlivePlayers;
    //private HashMap<ClientHandler,Integer> seeHoles; // les tours restant pendants lesquels on affiche les trous
    //private HashMap<ClientHandler,>;
    private int resteToursPieges;
    private int resteToursMap;
    //private int revealHoleTour;

    //Attributs

    /**
     * The Mode.
     */
    public int mode = 3;


    /**
     * Instantiates a new War fog.
     *
     * @param x           the x
     * @param y           the y
     * @param tres        the tres
     * @param holes       the holes
     * @param maxPlayers  the max players
     * @param robots      the robots
     * @param owner       the owner
     * @param mainHandler the main handler
     */
//Constructeur
    public WarFog(int x, int y, int tres, int holes, int maxPlayers, boolean robots, ClientHandler owner, ConnectionHandler mainHandler) {
        super(x, y, tres, holes, maxPlayers, robots, owner, mainHandler);
        this.mode = 3;
    }

    @Override
    protected void startGame() {
        // d'abord on broadcast a chaque joueur ce qu'il doit savoir
        this.stillAlivePlayers = new CopyOnWriteArrayList<>(this.players);
        Collections.shuffle(this.stillAlivePlayers);
        currentPlayerIndex = (int) (Math.random() * this.stillAlivePlayers.size());
        broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");

        sendHoles();
        sendWalls();
        sendTres();
        for (ClientHandler c : stillAlivePlayers) {
            sendPositions(c);
        }
    }

    private int getNumberHoles() {return 0;}

    @Override
    public void sendHoles() {
        for (ClientHandler c : stillAlivePlayers) {
            c.send("320 SENDING HOLES");
            int x = c.getClient().getCoordonnees().getX();
            int y = c.getClient().getCoordonnees().getY();
            int nb;
            ArrayList<Coordinates> tmp = new ArrayList<Coordinates>();
            for (int i=x-1; i<x+1;i++) {
                for (int j=y-1;j<y+1;j++) {
                    for (Coordinates coord : plateau.getCoordinatesTrous()) {
                        if (coord.getX()==i && coord.getY()==j) {
                            tmp.add(coord);
                        }
                    }
                }
            }
            c.send("320 NUMBER "+ (int)Math.ceil((double) tmp.size()/5));
            for (int i = 0; i < (int) Math.ceil((double) tmp.size() / 5); i++) {
                StringBuilder message = new StringBuilder("320 MESS " + String.valueOf(i) + " POS");
                for (int j = 0; 5 * i + j < tmp.size() && j < 5; j++) {
                    message.append(" ").append(tmp.get(5 * i + j).getX()).append(" ").append(tmp.get(5 * i + j).getY());
                }
                c.send(message.toString());
            }

        }
    }
    @Override
    public void sendWalls() {
        //broadcast("330 SENDING WALL");
        for (ClientHandler c : stillAlivePlayers) {
            c.send("330 SENDING WALL");
            int x = c.getClient().getCoordonnees().getX();
            int y = c.getClient().getCoordonnees().getY();
            int nb;
            ArrayList<Coordinates> tmp = new ArrayList<Coordinates>();
            for (int i=x-2; i<x+2;i++) {
                for (int j=y-2;j<y+2;j++) {
                    for (Coordinates coord : plateau.getCoordinatesMurs()) {
                        if (coord.getX()==i && coord.getY()==j) {
                            tmp.add(coord);
                        }
                    }
                }
            }
            c.send("330 NUMBER " + (int) Math.ceil((double) tmp.size() / 5));
            for (int i = 0; i < (int) Math.ceil((double) tmp.size() / 5); i++) {
                StringBuilder message = new StringBuilder("330 MESS " + String.valueOf(i) + " POS"); // commencer par 1 ?
                for (int j = 0; 5 * i + j < tmp.size() && j < 5; j++) {
                    message.append(" ").append(tmp.get(5 * i + j).getX()).append(" ").append(tmp.get(5 * i + j).getY());
                }
                c.send(message.toString());
            }
        }
    }

    @Override
    public void sendTres() {
        for (ClientHandler c : stillAlivePlayers) {
            c.send("340 SENDING TRES");
            int x = c.getClient().getCoordonnees().getX();
            int y = c.getClient().getCoordonnees().getY();
            int nb;
            ArrayList<Coordinates> tmp = plateau.getCoordinatesTresors();

            c.send("340 NUMBER "+ (int)Math.ceil((double) tmp.size()/5));
            for (int i = 0; i < (int) Math.ceil((double) tmp.size() / 5); i++) {
                StringBuilder message = new StringBuilder("340 MESS " + String.valueOf(i) + " POS");
                for (int j = 0; 5 * i + j < tmp.size() && j < 5; j++) {
                    message.append(" ").append(tmp.get(5 * i + j).getX()).append(" ").append(tmp.get(5 * i + j).getY()).append(" ").append(tmp.get(5 * i + j).getValue());
                }
                c.send(message.toString());
            }
        }
    }







    @Override
    public int movePlayer(ClientHandler client, String direction) {
        if (client == this.stillAlivePlayers.get(currentPlayerIndex)) {
            int res = super.movePlayer(client, direction);
            if (res == 1) {
                this.stillAlivePlayers.remove(client);
            }
            if (res != -1) {
                currentPlayerIndex += 1;
                currentPlayerIndex %= this.stillAlivePlayers.size();
                broadcast("500 " + this.stillAlivePlayers.get(currentPlayerIndex).getUsername() + " TURN");
            }
            return res;
        } else {
            return -2;
        }
    }

    @Override
    public void sendPositions(ClientHandler requester){
       //TODO: envoyer position des joueurs a rayons 2 max du joueur requester
        System.out.println("appel sendposition de warfog");
        int x = requester.getClient().getCoordonnees().getX();
        int y = requester.getClient().getCoordonnees().getY();
        for (int i=x-2;i<x+2;i++) {
            for (int j=y-2;j<y+2;j++) {
                for (ClientHandler c: stillAlivePlayers) {
                    if (c.getClient().getCoordonnees().getX() ==i && c.getClient().getCoordonnees().getY() ==j) {
                        requester.send("510 " + c.getUsername() + " POS " + c.getClient().getCoordonnees().getX() + " "+ c.getClient().getCoordonnees().getY());
                    }
                }
            }
        }

    }



    public int getResteToursPieges() {
        return resteToursPieges;
    }

    public void setResteToursPieges() {
        resteToursPieges--;
    }

    public void revealHoles(ClientHandler client) {
        // assurer le paiement du client
        // et révéler pièges pendant 5 tours

    }

    public void revealMap(ClientHandler client) {
        // assurer le paiement du client
        // et révéler partie de la map (choisie arbitrairement) pendant 3 tours
    }

}

