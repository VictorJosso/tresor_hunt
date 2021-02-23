package Utils;


import Apps.ConnectionHandler;
import Models.Game;

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
            //On attend -> 100 HELLO PLAYER username
            case "100":
                if (response.length == 4 && response[1].equals("HELLO") && response[2].equals("PLAYER")){
                    if (mainHandler.usernamesSet.contains(response[3])){
                        client.send("901 THIS NAME IS ALREADY USED");
                        break;
                    }
                    if (response[3].length() > 30){
                        client.send("990 TOO LARGE WORD IN COMMAND");
                        break;
                    }
                    mainHandler.usernamesSet.add(response[3]);
                    client.getClient().setUsername(response[3]);
                    client.send("101 WELCOME "+response[3]);
                    break;
                } else {
                    illegalCommand();
                    break;
                }


            //On attend -> 150 PARTY CREATE taille nb_joueurs bots username
            case "150":

                if(response.length == 7 && response[1].equals("PARTY") && response[2].equals("CREATE")) {

                    boolean exists = false;
                    for(int i=0 ; i<mainHandler.usernamesSet.size() ; i++) {
                        if(response[5].equals(mainHandler.usernamesSet.get(i))) exists = true;
                    }

                    if(!exists) {
                        client.send("151 NON-EXISTENT USERNAME, PLEASE REGISTER FIRST");
                        break;
                    }

                    if(Integer.parseInt(response[3])<3) {
                        client.send("152 TOO SMALL MAP");
                        break;
                    }


                    if(Integer.parseInt(response[3])>15) {
                        client.send("152 TOO BIG MAP");
                        break;
                    }


                    if(Integer.parseInt(response[4])>12) {
                        client.send("153 TOO MANY PLAYERS");
                        break;
                    }


                    boolean bot = response[5].toLowerCase(Locale.ROOT).equals("oui");

                    int size = Integer.parseInt(response[3]);
                    int players = Integer.parseInt(response[4]);



                    Game game = new Game(size, players, bot);


                }

                else {
                    illegalCommand();
                    break;
                }


            default:
                illegalCommand();
                break;
        }
    }
}
