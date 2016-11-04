package me.mrrobot97.designer.retrofit;

import java.io.File;
import java.io.IOException;
import me.mrrobot97.designer.SwipeActivity.MyApplication;
import me.mrrobot97.designer.Utils.NetUtils;
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
  public static String baseUrl = "https://api.dribbble.com/v1/";
  public static String access_token =
      "a62b88ea291c0d0e5b9295fdb8930936f945027bb84ff747ef6b89f8a9cd4da1";

  private static Retrofit retrofit;

  public static Retrofit getRetrofit() {
    if (retrofit == null) {
      synchronized (Retrofit.class) {
        if (retrofit == null) {
          Interceptor interceptor = new Interceptor() {
            @Override public Response intercept(Interceptor.Chain chain) throws IOException {
              Request original = chain.request();
              Request request = null;
              if (NetUtils.isNetworkOnline(MyApplication.getContext())) {
                request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + access_token)
                    .build();
              } else {
                request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + access_token)
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
              }
              Response response = chain.proceed(request);
              if (NetUtils.isNetworkOnline(MyApplication.getContext())) {
                int maxAge = 60 * 60 * 4; // read from cache for 4 hour
                return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
              } else {
                int maxStale = 60 * 60 * 24 * 3; // tolerate 3 day
                return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
              }
            }
          };
          //设置OKHttpClient的缓存目录
          File cacheDir = MyApplication.getContext().getCacheDir();
          File cacheFile = new File(cacheDir, "CacheFile");

          OkHttpClient client =
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
