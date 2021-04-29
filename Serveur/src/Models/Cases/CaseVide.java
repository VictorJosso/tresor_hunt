package Models.Cases;

public class CaseVide extends Case{
    public CaseVide(int X, int Y) {
        super(X, Y);
    }

    @Override
    public boolean isFree() {
        return playerOn == null;
    }

    @Override
    public String toString() {
        return " ";
    }
}
