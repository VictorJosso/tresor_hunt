package Utils.Bot;

import Apps.ConnectionHandler;
import Models.Client;
import Models.Games.Game;
import Utils.ClientHandler;
import Utils.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot extends ClientHandler {

    private final ConnectionHandler mainApp;
    private Client client = new Client();
    private ArrayList<Integer> joinedGames = new ArrayList<>();
    private final PlateauBot plateauBot;
    private final int gameId;
    private final Parser parser;
    private final HashMap<String, Unpacker> unpackers = new HashMap<>();
    private boolean lastMoveSaved = true;
    private boolean someonehasMoved = false;
    private String[] availableUsernames = {"thosekayak", "referencesham", "ongoingshivering", "harmlessalluring", "indonesianyawning", "dodgeballislands", "starepolenta", "geologistarches", "danonecourage", "drinkgrudging", "tunainform", "japanmade", "volarylectern", "fanaticalextralarge", "jabberlovesick", "cubstorage", "hoarsegrind", "disneyaddition", "protestminority", "dewmandible", "idioticchubby", "personarm", "flintgelatinous", "lichenignore", "ludicroushire", "doughmusty", "filldisguising", "heavilykoala", "wearyscreeching", "remaincocksfoot", "clogsfink", "floweryclipclop", "dugcletch", "bucketthorns", "pleasecow", "boughtprint", "argentpad", "relievedclimb", "scutesaveloy", "medicalbent", "rowdypeaceful", "governessforked", "amiableopisthenar", "penitentbird", "desertcrash", "relaxedravioli", "trustingshut", "portraytweed", "shelifestyle", "vealdrab", "gyruslepe", "denpossibly", "automaple", "openbowline", "brainycheat", "todayincreasing", "tensionidentity", "chutneyresolution", "foolishcrummy", "buckbounce", "poursweaty", "therecope", "althoughrecap", "dentistpancake", "shuffleblowfish", "rejoiceliterary", "asternquack", "martenalarming", "shouldersscull", "boafroggery", "photographbed", "gormlesssamoan", "jowlbreed", "cerebrumexcuse", "exhortvex", "citizenscare", "tophatraspberry", "importantartificial", "rhumbastarboard", "advancevenezuelan", "absorbingbox", "kettleninny", "killmarvelous", "rewardingwatery", "groanlimes", "santanderessex", "loganberriesmetacarpal", "murkystorm", "stradbouquet", "unlessobligation", "fibberbesides", "hostbreakdown", "rodmuscle", "sheconvolvulus", "difficultywalty", "coursetouch", "chanelimpossible", "pertheorize", "petitedisplay", "processionarylonely", "wasevidence", "differsauce", "cautionpies", "naturallyeland", "rowcheese", "grapnelsquawk", "solarreserve", "funnyafterdeck", "exchangedoves", "knownoutpost", "itchquaint", "boinksilk", "drawinglemony", "awfultailed", "santaabsent", "singerduffer", "mealystudent", "guaranteeretire", "covenkimono", "sexualcrying", "actioncompanion", "vagabondpowerful", "growlchant", "pepperyfour", "concretetrunnel", "possiblearmpit", "ankleradish", "announcefrontal", "afternoonbell", "pigpress", "secretivefavor", "obsessedhorrible", "pinionairline", "pearspill", "ethicsmodern", "rangepharmacist", "draincitizen", "observermainly", "ringedbeef", "worseblare", "spikespager", "chivedeter", "likesome", "layerfind", "pillockwrap", "sternumtoast", "haggisframework", "occasionalcowardice", "thinkingconsistent", "educationcase", "abandonedcocktail", "dingdongmarvelous", "scorchseemly", "headscarfvestments", "backsupporter", "noisegreens", "lightpink", "asideshut", "dungareesfumbling", "needymagnet", "teacherwind", "hippielongjohns", "extensionslipway", "diligencedaily", "enormousore", "raceragonizing", "ferociousbesides", "dyeproblem", "industrypleasant", "ribbitrecall", "aggravatedstack", "ontobreezy", "untimelyhunter", "stunningpuck", "descentelated", "spotseem", "likeablewornout", "somersaultmessenger", "gamehaworth", "wrestleremain", "grininterrupt", "holelevel", "scrapclay", "anorakaround", "darelepe", "payproclaim", "propertyhefty", "sheeppumps", "garrulousoperator", "norscore", "readingfrequent", "matekhakis", "fundingparliament", "welshwrote", "wadersrotten", "ultramarketing", "corpsmoor", "cavalcadejovial", "penitentsecondary", "birchoccupy"};
    private String username = "[BOT]"+availableUsernames[(int) (Math.random() * availableUsernames.length)];


    /**
     * Instantiates a new Client handler.
     *
     * @param mainApp the main app
     */
    public Bot(ConnectionHandler mainApp, int gameId, int dimensionX, int dimensionY) {
        this.mainApp = mainApp;
        this.gameId = gameId;
        this.plateauBot = new PlateauBot(dimensionX, dimensionY);
        this.parser = new Parser(this, mainApp);
        this.joinedGames.add(gameId);
    }

    public void register(Game game){
        if (!game.askForName_BOT(this.username)){
            this.username = "[BOT]"+availableUsernames[(int) (Math.random() * availableUsernames.length)];
            register(game);
        }
        else {
            this.client.setGameRunning(game);
            game.addPlayer(this);
        }
    }

    private void illegalCommand(){

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

    private void fillPlateau(){
        this.parser.parse("400 GETHOLES");
        this.parser.parse("410 GETTREASURES");
        this.parser.parse("420 GETWALLS");
    }

    private String selectPlayerForFocus(){
        String p = this.plateauBot.getPlayers().get((int) (Math.random() * this.plateauBot.getPlayers().size()));
        if (p.equals(this.username)){
            return selectPlayerForFocus();
        } else {
            return p;
        }
    }

    private void think(){
        String player = selectPlayerForFocus();
        Thread t = new Thread(new Decision(this, plateauBot, this.username, player));
        t.start();
    }

    public void iAmDone(String result){
        System.out.println("|| [BOT] || : PENSE = "+result);
        switch (result){
            case "UP" -> this.plateauBot.getPlayerPosition(this.username).addToY(-1);
            case "DOWN" -> this.plateauBot.getPlayerPosition(this.username).addToY(1);
            case "LEFT" -> this.plateauBot.getPlayerPosition(this.username).addToX(-1);
            case "RIGHT" -> this.plateauBot.getPlayerPosition(this.username).addToX(1);
        }
        this.parser.parse("200 GO"+result);
    }

    /**
     * Send.
     *
     * @param message the message
     */
    public void send(String message){
        // Process des commandes de la partie ICI

        System.out.println("|| [BOT] || : MESSAGE RECU = "+message);

        String[] commande = message.split(" ");
        switch (commande[0]){
            case "140":
                this.plateauBot.addPlayer(commande[1]);
                break;
            case "153":
                this.fillPlateau();
                break;
            case "401":
            case "421":
                if(unpackers.containsKey(commande[0])){
                    unpackers.get(commande[0]).unPack(message);
                } else {
                    unpackers.put(commande[0], new Unpacker(message, 2, plateauBot));
                }
                break;
            case "411":
                if(unpackers.containsKey(commande[0])){
                    unpackers.get(commande[0]).unPack(message);
                } else {
                    unpackers.put(commande[0], new Unpacker(message, 3, plateauBot));
                }
                break;
            case "510":
                plateauBot.setPosition(commande[1], Integer.parseInt(commande[3]), Integer.parseInt(commande[4]));
                if (!this.lastMoveSaved){
                    this.lastMoveSaved = true;
                    this.think();
                }
                break;
            case "500":
                if (commande[1].equals(this.username)){
                    if (!someonehasMoved){
                        someonehasMoved = true;
                        this.think();
                        break;
                    }
                    this.lastMoveSaved = false;
                }
                break;
            case "203":
                this.plateauBot.treasureFound(this.username, Integer.parseInt(commande[4]), this.plateauBot.getPlayerPosition(this.username).getX(), this.plateauBot.getPlayerPosition(this.username).getY());
                this.client.addScore(Integer.parseInt(commande[4]));
                break;
            case "511":
                plateauBot.setPosition(commande[1], Integer.parseInt(commande[3]), Integer.parseInt(commande[4]));
                this.plateauBot.treasureFound(commande[1], Integer.parseInt(commande[6]), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]));
                this.client.addScore(Integer.parseInt(commande[6]));
                if (!this.lastMoveSaved){
                    lastMoveSaved = true;
                    this.think();
                }
                break;
            case "520":
                this.plateauBot.notifyDead(commande[1]);
            default:
                break;
        }
    }



    /**
     * Close connection.
     */
    protected void closeConnection(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return true;
    }

    public void setLoggedIn(boolean loggedIn) {}

    public ArrayList<Integer> getJoinedGames() {
        return joinedGames;
    }

    public boolean isGoodClient(){
        return true;
    }
}
