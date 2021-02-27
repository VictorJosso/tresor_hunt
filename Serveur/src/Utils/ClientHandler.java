package Utils;

import Apps.ConnectionHandler;
import Models.Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private ConnectionHandler mainApp;
    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;
    private Parser parser;
    private Client client;

    public ClientHandler(Socket socket, ConnectionHandler mainApp) throws Exception{
        this.socket = socket;
        this.mainApp = mainApp;
        this.scanner = new Scanner(socket.getInputStream());
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.parser = new Parser(this, mainApp);
        this.client = new Client();

    }

    private void illegalCommand(){
        send("550 ILLEGAL COMMAND. BYE");
        closeConnection();
    }

    @Override
    public void run(){
        try {
            System.out.println("Connected from " + socket);
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                switch (command.split(" ")[0]) {
                    case "50":
                        printWriter.println("50 HELLO " + ConnectionHandler.SERVER_VERSION);
                        socket.close();
                        break;
                    //On attend -> 100 HELLO PLAYER username
                    case "100":
                        if (command.split(" ").length == 4 && command.split(" ")[1].equals("HELLO") && command.split(" ")[2].equals("PLAYER")){
                            if (mainApp.usernamesSet.contains(command.split(" ")[3])){
                                send("901 THIS NAME IS ALREADY USED");
                                break;
                            }
                            if (command.split(" ")[3].length() > 30){
                                send("990 TOO LARGE WORD IN COMMAND");
                                break;
                            }
                            mainApp.usernamesSet.add(command.split(" ")[3]);
                            client.setUsername(command.split(" ")[3]);
                            client.setLoggedIn(true);
                            send("101 WELCOME "+command.split(" ")[3]);
                            break;
                        } else {
                            illegalCommand();
                            break;
                        }
                    default:
                        if (client.isLoggedIn()) {
                            this.parser.parse(command);
                        } else {
                            send("555 UNAUTHORIZED. PLEASE LOG IN");
                        }
                        break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }

    protected void send(String message){
        this.printWriter.println(message);
    }

    protected void closeConnection(){
        try{
            socket.close();
            if (client.getUsername() != null) {
                mainApp.usernamesSet.remove(client.getUsername());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        }
}
