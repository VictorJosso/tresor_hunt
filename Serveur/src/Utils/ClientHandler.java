package Utils;

import Apps.ConnectionHandler;
import Models.Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Client handler.
 */
public class ClientHandler implements Runnable{

    private ConnectionHandler mainApp;
    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;
    private Parser parser;
    private Client client;
    private String username;
    private boolean loggedIn;
    private final ArrayList<Integer> joinedGames = new ArrayList<>();



    private final CopyOnWriteArrayList<Integer> sendingQueue = new CopyOnWriteArrayList<>();

    /**
     * Instantiates a new Client handler.
     *
     * @param socket  the socket
     * @param mainApp the main app
     * @throws Exception the exception
     */
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
        if (loggedIn){
            mainApp.usernamesSet.remove(username);
        }
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
                    case "55":
                        if (command.split(" ")[1].equals("UPGRADE")){
                            parser.setGoodClient(true);
                            send("55 UPGRADED");
                            System.out.println("Le client "+socket+" est un bon client.");
                            break;
                        }
                    //On attend -> 100 HELLO PLAYER username
                    case "100":
                        if (command.split(" ").length == 4 && command.split(" ")[1].equals("HELLO") && command.split(" ")[2].equals("PLAYER")){
                            if (mainApp.usernamesSet.contains(command.split(" ")[3]) || command.split(" ")[3].matches(".*[victorVICTOR].*")){
                                send("901 THIS NAME IS ALREADY USED");
                                break;
                            }
                            if (command.split(" ")[3].length() > 30){
                                send("990 TOO LARGE WORD IN COMMAND");
                                break;
                            }
                            mainApp.usernamesSet.add(command.split(" ")[3]);
                            username = (command.split(" ")[3]);
                            loggedIn = true;
                            send("101 WELCOME "+command.split(" ")[3]);
                            break;
                        } else {
                            illegalCommand();
                            break;
                        }
                    case "102":
                        if (command.split(" ")[1].equals("QUIT")){
                            if (loggedIn){
                                mainApp.usernamesSet.remove(username);
                            }
                            for (Integer id: joinedGames){
                                this.mainApp.getGamesMap().get(id).removePlayer(this);
                            }
                            send("103 BYE");
                            socket.close();
                        } else {
                            illegalCommand();
                        }
                        break;
                    default:
                        if (loggedIn) {
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

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    public void newClient(){
        this.client = new Client();
    }

    /**
     * Send.
     *
     * @param message the message
     */
    public void send(String message){
        int code = (int) (Math.random() * (9999-1000+1) + 1000);
        this.sendingQueue.add(code);
        while (!(this.sendingQueue.get(0) == code)){}
        this.printWriter.println(message);
        this.sendingQueue.remove((Integer) code);
    }



    /**
     * Close connection.
     */
    protected void closeConnection(){
        try{
            socket.close();
            if (username != null) {
                mainApp.usernamesSet.remove(username);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ArrayList<Integer> getJoinedGames() {
        return joinedGames;
    }

    public boolean isGoodClient(){
        return parser.isGoodClient();
    }

}
