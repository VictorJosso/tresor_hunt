package Utils.Bot;

public class Decision implements Runnable {
    // Classe pour décision de mouvement.
    private final PlateauBot plateau;
    private final String targetPlayer;
    private final Bot botInstance;
    private String[] directions = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};

    public Decision(Bot botInstance, PlateauBot plateau, String targetPlayer) {
        this.botInstance = botInstance;
        this.plateau = plateau.copy();
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void run() {
        // TODO : Faire l'aglo de décision
        this.botInstance.iAmDone(directions[(int) (Math.random() * 4)]);
    }
}
