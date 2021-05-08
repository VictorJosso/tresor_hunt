package Utils.Bot;

import java.util.ArrayList;

/**
 * The type Unpacker.
 */
public class Unpacker {
    private String type;
    private ArrayList<String> resutls = new ArrayList<>();
    private int total;
    private PlateauBot callBack;
    private int n;

    /**
     * Instantiates a new Unpacker.
     *
     * @param message  the message
     * @param n        the n
     * @param callBack the call back
     */
    public Unpacker(String message, int n, PlateauBot callBack) {
        String[] commande = message.split(" ");
        this.n = n;
        this.type = commande[0];

        this.total = (int) Math.ceil((double) Integer.parseInt(commande[2]) / 5);
        this.callBack = callBack;
    }

    /**
     * Un pack.
     *
     * @param message the message
     */
    public void unPack(String message){
        String[] commande = message.split(" ");
        for (int i = 4; i < commande.length; i += n){
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < n; j ++){
                builder.append(" ").append(commande[i+j]);
            }
            builder.deleteCharAt(0);
            resutls.add(builder.toString());
        }
        if (Integer.parseInt(commande[2]) == this.total-1){
            callBack.receiveData(this.type, this.resutls);
        }
    }
}
