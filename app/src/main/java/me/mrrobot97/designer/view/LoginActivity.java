package me.mrrobot97.designer.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.Utils.FileUtils;
import me.mrrobot97.designer.Utils.SharedPreferencesUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

//// TODO: 16/11/1 WebView缓存问题，每次login前手动删除缓存文件，打开APP第一次认证正常，但第二次登录还是会直接跳过，尚未找到原因。 
public class LoginActivity extends AppCompatActivity {
  @BindView(R.id.bt_login)Button mBtLogin;
  @BindView(R.id.bt_non_login)Button mBtNon;
  @BindView(R.id.txt_logo)TextView mTxtLogo;
  @BindView(R.id.web_view)WebView mWebView;
  @BindView(R.id.loading_layout)RelativeLayout loadingLayout;
  @BindView(R.id.back_layout)RelativeLayout backLayout;
  @BindView(R.id.progressbar)ProgressBar mProgressBar;
  @BindView(R.id.webview_layout)RelativeLayout mWebviewLayout;

  private final String AUTH_URL="https://dribbble.com/oauth/authorize";
  private final String TOKEN_URL="https://dribbble.com/oauth/token";
  private final String CLIENT_ID="a655a6b9c5440762a30d30f85142afc6fe34ff3419ee5864130034f613b1791c";
  private final String CLIENT_SECRET="9c5de21b644560851a8823fb47de3c3ce223d9d3f67a27a8c73346ff02caa98a";
  private final String CACHE_DIR="/data/data/me.mrrobot97.designer/app_webview";
  private final String CACHE_FILE="/data/data/me.mrrobot97.designer/cache/org.chromium.android_webview";
  private String code;
  private String token;
  private OkHttpClient client;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    boolean isLogin=
        (boolean) SharedPreferencesUtils.getFromSpfs(getApplicationContext(),"login",false);
    if(isLogin){
      startBrowseActivity();
    }
    init();
  }

  private void startBrowseActivity(){
    Intent intent=new Intent(LoginActivity.this,BrowseActivity.class);
    startActivity(intent);
    finish();
  }

  private void init() {
    Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/New Walt Disney.ttf");
    mTxtLogo.setTypeface(typeface);
    client=new OkHttpClient();
    mBtLogin.setOnClickListener(view -> login());
    mBtNon.setOnClickListener(view -> nonLogin());
    WebSettings settings=mWebView.getSettings();
    settings.setAppCacheEnabled(false);
    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(false);
    mWebView.setWebViewClient(new WebViewClient(){
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.contains("code")){
          //重定向网页，参数中包含了code
          code=url.substring(url.lastIndexOf("=")+1);
          mWebviewLayout.setVisibility(View.GONE);
          getToken();
          return false;
        }
        view.loadUrl(url);
        return true;
      }
    });
    mWebView.setWebChromeClient(new WebChromeClient(){
      @Override public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mProgressBar.setProgress(newProgress);
      }
    });
  }

  private void nonLogin() {
    SharedPreferencesUtils.putToSpfs(getApplicationContext(),"login",false);
    startBrowseActivity();
  }

  private void login() {
    //两个缓存目录下的文件都要手动删除，否则重新登录的时候会因为缓存而直接进入
    //目前每次从新打开APP第一次登录正常，之后的全部直接走缓存了。
    FileUtils.deleteFileOrDir(CACHE_DIR);
    FileUtils.deleteFileOrDir(CACHE_FILE);
    mWebView.clearCache(true);
    Map<String,String> header=new HashMap<>();
    header.put("Cache-Control","no-cache");
    mWebviewLayout.setVisibility(View.VISIBLE);
    backLayout.setVisibility(View.INVISIBLE);
    mWebView.loadUrl(AUTH_URL+"?client_id="+CLIENT_ID,header);
  }

  public void getToken() {
    backLayout.setVisibility(View.VISIBLE);
    loadingLayout.setVisibility(View.VISIBLE);
    RequestBody postParam= new FormBody.Builder().add("client_id",CLIENT_ID).add("client_secret",CLIENT_SECRET).add("code",code).build();
    final Request request=new Request.Builder().url(TOKEN_URL).post(postParam).build();
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        loadingLayout.setVisibility(View.GONE);
        backLayout.setVisibility(View.VISIBLE);
        Toast.makeText(LoginActivity.this, "获取认证信息失败，请重新登录", Toast.LENGTH_SHORT).show();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        try {
          loadingLayout.post(()->{
            loadingLayout.setVisibility(View.GONE);
            backLayout.setVisibility(View.VISIBLE);
          });
          JSONObject object=new JSONObject(response.body().string());
          token=object.getString("access_token");
          SharedPreferencesUtils.putToSpfs(getApplicationContext(),"access_token",token);
          SharedPreferencesUtils.putToSpfs(getApplicationContext(),"login",true);
          startBrowseActivity();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
