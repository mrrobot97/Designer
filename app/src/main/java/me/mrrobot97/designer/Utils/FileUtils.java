package me.mrrobot97.designer.Utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import me.mrrobot97.designer.retrofit.ApiClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

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

  public static boolean isExternalStorageWritable() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      return true;
    }
    return false;
  }

  public static void saveImage(Context context,String url) {
    OkHttpClient client= ApiClient.getClient();
    if (!isExternalStorageWritable()) {
      Toast.makeText(context, "SD card not available", Toast.LENGTH_SHORT).show();
      return;
    }
    File dir = new File(Environment.getExternalStorageDirectory(), "Designer");
    if (!dir.exists()) dir.mkdir();
    final File file = new File(dir,
        url.substring(url.lastIndexOf(File.separator), url.length()));
    Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        BufferedOutputStream bos = null;
        try {
          response = client.newCall(request).execute();
          bos = new BufferedOutputStream(new FileOutputStream(file));
          byte[] buffer = response.body().bytes();
          bos.write(buffer, 0, buffer.length);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (bos != null) {
            try {
              bos.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        return Observable.just(null);
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Void>() {

          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
          }

          @Override public void onNext(Void aVoid) {
            Toast.makeText(context, "Save image to " + file.getAbsolutePath(),
                Toast.LENGTH_SHORT).show();
          }
        });
  }
}
