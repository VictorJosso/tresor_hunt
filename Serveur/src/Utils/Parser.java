package Utils;


import Apps.ConnectionHandler;

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


            //On attend -> 110 CREATE mode SIZE x y HOLE h TRES n
            case "110":

                if(response.length == 10 && response[1].equals("CREATE") && response[3].equals("SIZE") &&
                        response[6].equals("HOLE") && response[8].equals("TRES")
                ) {

                    boolean exists = false;
                    for(int i=0 ; i<mainHandler.usernamesSet.size() ; i++) {
                        if(this.client.equals(mainHandler.usernamesSet.get(i))) exists = true;
                    }

                    if(!exists) {
                        client.send("ERROR : NON-EXISTENT USERNAME, PLEASE REGISTER FIRST");
                        break;
                    }


                    if(Integer.parseInt(response[4])>12) {
                        client.send("ERROR : TOO MANY PLAYERS");
                        break;
                    }


                    if(Integer.parseInt(response[2]) < 1 || Integer.parseInt(response[2])>3 ) {
                        client.send("ERROR : INVALID GAMEMODE");
                        break;
                    }


                    if(Integer.parseInt(response[4])<5 || Integer.parseInt(response[5])<5) {
                        client.send("ERROR : TOO SMALL MAP");
                        break;
                    }


                    if(Integer.parseInt(response[4])>30 || Integer.parseInt(response[5])>30 ) {
                        client.send("ERROR : TOO BIG MAP");
                        break;
                    }


                    if(Integer.parseInt(response[7])>  Integer.parseInt(response[4])*Integer.parseInt(response[5])/2   ) {
                        client.send("ERROR : TOO MUCH HOLES");
                        break;
                    }


                    if(Integer.parseInt(response[7])<0) {
                        client.send("ERROR : NOT ENOUGH HOLES");
                        break;
                    }


                    if(Integer.parseInt(response[9])>  Integer.parseInt(response[4])*Integer.parseInt(response[5])/4   ) {
                        client.send("ERROR : TOO MUCH TREASURES");
                        break;
                    }


                    if(Integer.parseInt(response[9])<0) {
                        client.send("ERROR : NOT ENOUGH TREASURES");
                        break;
                    }

                    //Speeding contest
                    if(Integer.parseInt(response[2]) == 1) {

                    }

                    //Tour par tour
                    if(Integer.parseInt(response[2]) == 2) {

                    }

                    //Brouillard de guerre
                    if(Integer.parseInt(response[2]) == 3) {

                    }



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
