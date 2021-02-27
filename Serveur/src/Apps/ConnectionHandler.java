package Apps;

import Models.Game;
import Utils.ClientHandler;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler {

    public static double SERVER_VERSION = 1.0;
    public CopyOnWriteArrayList<String> usernamesSet = new CopyOnWriteArrayList<>();
    private final Map<Integer, Game> gamesMap = new ConcurrentHashMap<>();
    private final Map<Integer, Game> availableGamesMap = new ConcurrentHashMap<>();


    public ConnectionHandler() throws Exception{
        run();
    }

    public int registerGameId(Game game){
        int id = (int) (Math.random() * (9999-1000+1) + 1000);
        if (gamesMap.containsKey(id)){
            return registerGameId(game);
        } else {
            gamesMap.put(id, game);
            availableGamesMap.put(id, game);
            return id;
        }
    }

    private void run() throws Exception{
        ServerSocket listener = new ServerSocket(7236);
        System.out.println("Le serveur est actif sur le port " + listener.getLocalPort());
        ExecutorService pool = Executors.newFixedThreadPool(250);
        while (true) {
            pool.execute(new ClientHandler(listener.accept(), this));
        }
    }

    public Map<Integer, Game> getAvailableGamesMap() {
        return availableGamesMap;
    }
}
