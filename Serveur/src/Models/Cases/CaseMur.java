package Models.Cases;

public class CaseMur extends Case{

    private int durabilite = 100;

    public CaseMur(int X, int Y) {
        super(X, Y);
        isFree = false;
    }

    @Override
    public void free() {}


    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }
}
