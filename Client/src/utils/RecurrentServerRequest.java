package utils;

import java.util.TimerTask;

public abstract class RecurrentServerRequest extends TimerTask {
    public ConnectionHandler handler;

    public RecurrentServerRequest() {
    }

    public void setHandler(ConnectionHandler handler) {
        this.handler = handler;
    }


    public void run(){}
}
