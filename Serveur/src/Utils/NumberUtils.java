package Utils;

/**
 * The type Number utils.
 */
public class NumberUtils {
    /**
     * Is numeric boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isNumeric(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }
}
