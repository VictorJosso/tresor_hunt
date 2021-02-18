package Apps;

import Utils.ClientHandler;

import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler {

    public static double SERVER_VERSION = 1.0;
    public CopyOnWriteArrayList<String> usernamesSet = new CopyOnWriteArrayList<>();


    public ConnectionHandler() throws Exception{
        run();
    }

    private void run() throws Exception{
        ServerSocket listener = new ServerSocket(7236);
        System.out.println("Le serveur est actif sur le port " + listener.getLocalPort());
        ExecutorService pool = Executors.newFixedThreadPool(250);
        while (true) {
            pool.execute(new ClientHandler(listener.accept(), this));
        }
    }
}
