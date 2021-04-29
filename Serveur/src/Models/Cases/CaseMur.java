package Models.Cases;

public class CaseMur extends Case{

    private int durabilite = 100;

    public CaseMur(int X, int Y) {
        super(X, Y);
    }

    @Override
    public void free() {}

    @Override
    public boolean isFree() {
        return false;
    }

    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }

    @Override
    public String toString() {
        return "M";
    }
}
