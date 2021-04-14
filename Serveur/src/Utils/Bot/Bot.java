package Utils.Bot;

import Apps.ConnectionHandler;
import Models.Client;
import Utils.ClientHandler;
import Utils.Parser;

import java.util.ArrayList;

public class Bot extends ClientHandler {

    private final ConnectionHandler mainApp;
    private Client client = new Client();
    private ArrayList<Integer> joinedGames = new ArrayList<>();
    private final PlateauBot plateauBot;
    private final int gameId;
    private final Parser parser;
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

    /**
     * Send.
     *
     * @param message the message
     */
    public void send(String message){
        // Process des commandes de la partie ICI
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
