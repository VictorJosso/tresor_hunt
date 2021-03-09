package utils;

import models.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;

public class ConnectionHandler extends Thread{
    private Config config;
    private boolean running = true;
    private Scanner scanner;
    private PrintWriter printWriter;
    private Socket socket;
    private Map<String, CallbackServer> callLinks = new HashMap<>();
    private Map<String, CallbackInstance> callOwners = new HashMap<>();

    public ConnectionHandler(Config config) {
        this.config = config;
    }

    public void quitter(){
        send("102 QUIT");
        running = false;
        if (socket != null){
            try {
                socket.close();
            } catch (IOException ignored){}
        }
    }

    public void send(String message){
        if (printWriter != null) {
            printWriter.println(message);
        } else {
            try {
                Thread.sleep(500);
                send(message);
            } catch (InterruptedException ignored){}
        }
    }

    public void registerCallback(String code, CallbackInstance controller, CallbackServer callback){
        callLinks.put(code, callback);
        callOwners.put(code, controller);
    }

    public void releaseCallback(String code){
        callLinks.remove(code);
        callOwners.remove(code);
    }

    public Timer registerRecurrentServerCall(RecurrentServerRequest recurrentServerRequest, int delay){
        Timer timer = new Timer(true);
        recurrentServerRequest.setHandler(this);
        timer.schedule(recurrentServerRequest, 0, delay);
        return timer;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(config.getAdresseServeur(), config.getPortServeur());
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        while (running && scanner.hasNext()){
            String command = scanner.nextLine();
            String[] response = command.split(" ");
            if (callLinks.containsKey(response[0])) {
                callLinks.get(response[0]).call(callOwners.get(response[0]), command);
            } else {
                //TODO: TRAITER L'INFORMATION DU SERVEUR
            }
        }

    }
}
