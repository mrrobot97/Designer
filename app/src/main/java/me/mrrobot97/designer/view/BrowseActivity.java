package me.mrrobot97.designer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.Utils.NetUtils;
import me.mrrobot97.designer.Utils.ScreenUtils;
import me.mrrobot97.designer.Utils.SharedPreferencesUtils;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;
import me.mrrobot97.designer.presenter.BrowsePresenterImpl;
import me.mrrobot97.designer.presenter.IBrowsePresenter;

public class BrowseActivity extends AppCompatActivity implements IBrowseView{
    @BindView(R.id.activity_browse)RelativeLayout mContainer;
    @BindView(R.id.tool_bar)Toolbar mToolbar;
    @BindView(R.id.swipe_refresh_layout)SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tab_layout)TabLayout mTabLayout;
    @BindView(R.id.view_pager)ViewPager mViewPager;

    private Snackbar mSnackBar;
    private FragmentPagerAdapter mAdapter;
    private String[] mTitles=new String[]{"Popular","Debuts","Recent"};
    private static final int REQUEST_CODE=0;
    private boolean isSnackBarShown=false;
    private boolean isLogin=false;
    private Runnable runnable=new Runnable() {
        @Override public void run() {
            if(mRefreshLayout!=null){
                mRefreshLayout.setRefreshing(false);
            }
        }
    };


    private BaseFragment[] mFragments=new BaseFragment[3];

    private IBrowsePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.log_out:
                        SharedPreferencesUtils.putToSpfs(getApplicationContext(),"login",false);
                        Intent intent=new Intent(BrowseActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.user:
                        mPresenter.loadUserProile((String) SharedPreferencesUtils.getFromSpfs(getApplicationContext(),"access_token",null));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mPresenter=new BrowsePresenterImpl(this);
        if(!ifPermissionGranted()){
            requestPermissions();
        }else{
            init();
        }
    }

    private void init() {
        isLogin=
            (boolean) SharedPreferencesUtils.getFromSpfs(getApplicationContext(),"login",false);
        mSnackBar=Snackbar.make(mContainer,"Sure to exit?",Snackbar.LENGTH_INDEFINITE).setAction("Sure", new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        mFragments[0]=BaseFragment.newInstance(R.layout.fragment_layout);
        mFragments[1]=BaseFragment.newInstance(R.layout.fragment_layout);
        mFragments[2]=BaseFragment.newInstance(R.layout.fragment_layout);
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        for(int i=0;i<mTitles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab());
        }
        //设置左右各自缓存的fragment数量，默认为1，若保持默认，则当fragment切换至3时，1的fragment会调用onDestroyView，界面就销毁了。
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //控制只有当viewpager为静止状态时才能触发SwipeRefreshLayout
                mRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh(mViewPager.getCurrentItem());
                mRefreshLayout.setRefreshing(true);
            }
        });
        //设置下拉刷新的出发高度为屏幕高度的1/3
        mRefreshLayout.setDistanceToTriggerSync(ScreenUtils.getScreenWidthAndHeight(getApplicationContext())[1]/3);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
        loadData();
        if(!NetUtils.isNetworkOnline(this)){
            Toast.makeText(this, "客官，您没有联网呦~", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        for(int i=0;i<mFragments.length;i++){
            mPresenter.load(i);
            mFragments[i].showLoadingView();
            final int finalI = i;
            mFragments[i].setListener(new BaseFragment.SlideToBottomListener() {
                @Override
                public void whenSlideToBottom() {
                    mPresenter.loadMore(finalI);
                }
            });
        }
    }

    @Override
    public void loadShots(int position, List<Shot> shots,boolean success) {
        mRefreshLayout.post(runnable);
        if(!success){
            mFragments[position].setLoading(false);
            Toast.makeText(this, "客官，数据加载出错了呦~", Toast.LENGTH_SHORT).show();
        }else{
          mFragments[position].setData(shots);
        }
    }

    @Override
    public void refreshShots(int position, List<Shot> shots,boolean success) {
        mRefreshLayout.post(runnable);
        if(!success){
            mFragments[position].setLoading(false);
            Toast.makeText(this, "客官，数据加载出错了呦~", Toast.LENGTH_SHORT).show();
            return;
        }else{
          mFragments[position].refreshDate(shots);
        }
    }

    @Override
    public void loadMore(int position, List<Shot> shots,boolean success) {
        mRefreshLayout.post(runnable);
        mFragments[position].cancleLoadingView();
        if(!success){
            mFragments[position].setLoading(false);
            Toast.makeText(this, "客官，加载出错了呦~", Toast.LENGTH_SHORT).show();
            return;
        }else{
          mFragments[position].loadMoreData(shots);
          mRefreshLayout.setRefreshing(false);
        }
    }

    public boolean ifPermissionGranted(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    @Override
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }

    @Override public void openUserPanel(User user) {
        if(isLogin){
            Intent intent=new Intent(BrowseActivity.this,PlayerActivity.class);
            intent.putExtra("author",user);
            startActivity(intent);
        }else{
            Toast.makeText(this, "You are not signed in.", Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length<3){
                finish();
            }else{
                init();
            }
        }
    }

    @Override public void onBackPressed() {
        if(isSnackBarShown){
            mSnackBar.dismiss();
            isSnackBarShown=false;
        }else{
            mSnackBar.show();
            isSnackBarShown=true;
        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse,menu);
        MenuItem item=menu.getItem(1);
        if(isLogin){
            item.setTitle("Log out");
        }else{
            item.setTitle("Sign in");
        }
        return super.onCreateOptionsMenu(menu);
    }
}
