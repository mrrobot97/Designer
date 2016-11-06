package me.mrrobot97.designer.retrofit;

import java.io.File;
import me.mrrobot97.designer.SwipeActivity.MyApplication;
import me.mrrobot97.designer.Utils.NetUtils;
import me.mrrobot97.designer.Utils.SharedPreferencesUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrrobot on 16/10/21.
 */

public class ApiClient {
  public static final String CACHE_DIR="CacheFile";
  public static final String baseUrl = "https://api.dribbble.com/v1/";
  public static final String access_token =
      "a62b88ea291c0d0e5b9295fdb8930936f945027bb84ff747ef6b89f8a9cd4da1";
  public static String user_access_token;

  private static Retrofit retrofit;
  private static OkHttpClient client;

  public static OkHttpClient getClient(){
    return client;
  }

  public static Retrofit getRetrofit() {
    user_access_token=
        (String) SharedPreferencesUtils.getFromSpfs(MyApplication.getContext(),"access_token",null);
    String token=user_access_token;
    if(token==null) token=access_token;
    if (retrofit == null) {
      synchronized (Retrofit.class) {
        if (retrofit == null) {
          String finalToken = token;
          Interceptor interceptor = chain ->  {
              Request original = chain.request();
              Request request = null;
              if (NetUtils.isNetworkOnline(MyApplication.getContext())) {
                request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + finalToken)
                    .build();
              } else {
                request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + finalToken)
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
              }
              Response response = chain.proceed(request);
              if (NetUtils.isNetworkOnline(MyApplication.getContext())) {
                int maxAge = 60 * 60 * 1; // read from cache for 1 hour
                return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
              } else {
                int maxStale = 60 * 60 * 24 * 7; // tolerate one week
                return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
              }
          };
          //设置OKHttpClient的缓存目录
          File cacheDir = MyApplication.getContext().getCacheDir();
          File cacheFile = new File(cacheDir, CACHE_DIR);

          client =
              new OkHttpClient.Builder()
                  .cache(new Cache(cacheFile, 1024 * 1024 * 50))
                  .addNetworkInterceptor(interceptor)
                  .build();

          retrofit = new Retrofit.Builder().baseUrl(baseUrl)
              .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create())
              .client(client)
              .build();
        }
      }
    }
    return retrofit;
  }
}
