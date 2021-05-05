package Models.Cases;

public class CaseVide extends Case{
    public CaseVide(int X, int Y) {
        super(X, Y);
    }

    public CaseVide(CaseVide original){
        super(original);
    }

    public CaseVide copy(){
        return new CaseVide(this);
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
