package me.mrrobot97.designer.Utils;

import java.io.File;

/**
 * Created by mrrobot on 16/11/4.
 */

public class FileUtils {
  public static void deleteFileOrDir(String path){
    File file=new File(path);
    if(file.exists()){
      if(file.isFile()){
        file.delete();
      }else{
        for (File f:file.listFiles()){
          if(f.isFile()){
            f.delete();
          }else{
            deleteFileOrDir(f.getAbsolutePath());
          }
        }
      }
    }
  }
}
