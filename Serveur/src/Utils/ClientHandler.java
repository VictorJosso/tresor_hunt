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
                    default:
                        this.parser.parse(command);
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
