package Utils.Bot;

public class Decision implements Runnable {
    // Classe pour décision de mouvement.
    private final PlateauBot plateau;
    private final String targetPlayer;

    public Decision(PlateauBot plateau, String targetPlayer) {
        this.plateau = plateau.copy();
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void run() {
        // TODO : Faire l'aglo de décision
    }
}
