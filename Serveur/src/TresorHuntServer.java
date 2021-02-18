import Apps.ConnectionHandler;
import Utils.ClientHandler;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Point d'entrée du serveur

public class TresorHuntServer {
    public static void main(String[] args) throws Exception {
        ConnectionHandler handler = new ConnectionHandler();
    }
}
