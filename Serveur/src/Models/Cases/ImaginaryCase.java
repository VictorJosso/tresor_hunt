package Models.Cases;

public class ImaginaryCase extends Case{
    public ImaginaryCase() {
        super(-1, -1);
    }

    @Override
    public Case copy() {
        return new ImaginaryCase();
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
