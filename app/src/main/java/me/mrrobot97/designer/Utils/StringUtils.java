package me.mrrobot97.designer.Utils;

/**
 * Created by mrrobot on 16/11/5.
 */

public class StringUtils {
  public static boolean checkAvailable(String string){
    if(string==null||string.trim().length()==0) return false;
    return true;
  }
}
