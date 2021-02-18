package Utils;


import Apps.ConnectionHandler;

/**
 * This class is used to parse messages from clients,
 * create java objects of the command
 * and call methods to process them
 */

public class Parser {
    private final ClientHandler client;
    private final ConnectionHandler mainHandler;

    public Parser(ClientHandler client, ConnectionHandler handler) {
        this.client = client;
        this.mainHandler = handler;
    }

    private void illegalCommand(){
        client.send("550 ILLEGAL COMMAND. BYE");
        client.closeConnection();
    }

    protected void parse(String response_text){
        System.out.println("PROCESSING COMMAND : "+ response_text);
        String[] response = response_text.split(" ");
        switch (response[0]){
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
            default:
                illegalCommand();
                break;
        }
    }
}
