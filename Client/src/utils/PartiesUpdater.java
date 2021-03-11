package utils;

import Apps.MainApp;

import java.util.ArrayList;

public class PartiesUpdater extends CallbackInstance {
    MainApp mainApp;
    int totalParties;
    ArrayList<String> partiesListBrute = new ArrayList<>();

    public PartiesUpdater(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void parse(String message){
        String[] commande = message.split(" ");
        if (commande[1].equals("NUMBER")){
            this.totalParties = Integer.parseInt(commande[2]);
            partiesListBrute.clear();
        } else if (commande[1].equals("MESS")){
            partiesListBrute.add(message);
            if (Integer.parseInt(commande[2]) == totalParties){
                mainApp.updateParties(partiesListBrute);
            }
        }
    }


}
