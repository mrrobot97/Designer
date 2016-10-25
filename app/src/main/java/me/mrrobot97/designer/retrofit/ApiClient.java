package me.mrrobot97.designer.retrofit;

import java.io.IOException;

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
    public static String baseUrl="https://api.dribbble.com/v1/";
    public static String access_token="a62b88ea291c0d0e5b9295fdb8930936f945027bb84ff747ef6b89f8a9cd4da1";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){
        if(retrofit==null){
            synchronized (Retrofit.class){
                if(retrofit==null){
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(new Interceptor() {
                                                  @Override
                                                  public Response intercept(Interceptor.Chain chain) throws IOException {
                                                      Request original = chain.request();
                                                      Request request = original.newBuilder()
                                                              .addHeader("Authorization", "Bearer "+access_token)
                                                              .addHeader("Cache-control","86400")
                                                              .method(original.method(), original.body())
                                                              .build();

                                                      return chain.proceed(request);
                                                  }
                                              });

                            OkHttpClient client = httpClient.build();
                    retrofit=new Retrofit.Builder()
                            .baseUrl(baseUrl)
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
