package Models.Cases;

import Utils.ClientHandler;

/**
 * The type Case tresor.
 */
public class CaseTresor extends Case {

    private int value;
    private boolean secret = false;
    private ClientHandler owner;

    /**
     * Instantiates a new Case tresor.
     *
     * @param X     the x
     * @param Y     the y
     * @param value the value
     */
    public CaseTresor(int X, int Y, int value) {
        super(X, Y);
        this.value = value;
    }

    /**
     * Instantiates a new Case tresor.
     *
     * @param X      the x
     * @param Y      the y
     * @param value  the value
     * @param secret the secret
     */
    public CaseTresor(int X, int Y, int value, boolean secret) {
        super(X, Y);
        this.value = value;
        this.secret = secret;
    }

    /**
     * Instantiates a new Case tresor.
     *
     * @param original the original
     */
    public CaseTresor(CaseTresor original){
        super(original);
        this.value = original.value;
        this.secret = original.secret;
        this.owner = original.owner;
    }

    public CaseTresor copy(){
        return new CaseTresor(this);
    }

    /**
     * Is secret boolean.
     *
     * @return the boolean
     */
    public boolean isSecret() {
        return secret;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public ClientHandler getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(ClientHandler owner) {
        this.owner = owner;
    }

    @Override
    public boolean isFree() {
        return playerOn == null;
    }

    @Override
    public String toString() {
        return "T";
    }
}
