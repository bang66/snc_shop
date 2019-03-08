package snc.boot.util.common;

/**
 * Created by jac on 18-11-11.
 */
public class BaseString {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        return str.equals("");
    }
}
