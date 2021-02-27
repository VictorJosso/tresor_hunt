package Utils;

public class NumberUtils {
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
