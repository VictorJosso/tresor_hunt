package Utils.Bot;

import java.util.ArrayList;

public class Unpacker {
    private String type;
    private ArrayList<String> resutls = new ArrayList<>();
    private int total;
    private PlateauBot callBack;
    private int n;

    public Unpacker(String message, int n, PlateauBot callBack) {
        String[] commande = message.split(" ");
        this.n = n;
        this.type = commande[0];
        this.total = Integer.parseInt(commande[2]);
        this.callBack = callBack;
    }

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
