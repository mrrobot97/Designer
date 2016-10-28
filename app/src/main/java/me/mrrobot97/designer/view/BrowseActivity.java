package me.mrrobot97.designer.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.Utils.NetUtils;
import me.mrrobot97.designer.Utils.ScreenUtils;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.presenter.BrowsePresenterImpl;
import me.mrrobot97.designer.presenter.IBrowsePresenter;

import static android.view.View.GONE;

public class BrowseActivity extends AppCompatActivity implements IBrowseView{
    @BindView(R.id.tool_bar)Toolbar mToolbar;
    @BindView(R.id.swipe_refresh_layout)SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tab_layout)TabLayout mTabLayout;
    @BindView(R.id.view_pager)ViewPager mViewPager;
    @BindView(R.id.net_error_view)RelativeLayout mNetErrorView;
    private FragmentPagerAdapter mAdapter;
    private String[] mTitles=new String[]{"Popular","Debuts","Recent"};
    private static final int REQUEST_CODE=0;


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
        mPresenter=new BrowsePresenterImpl(this);
        if(!ifPermissionGranted()){
            requestPermissions();
        }else{
            init();
        }
    }

    private void init() {
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
        if(NetUtils.isNetworkOnline(this)){
            loadData();
        }else{
            Toast.makeText(this, "No internet connect", Toast.LENGTH_SHORT).show();
            mNetErrorView.setVisibility(View.VISIBLE);
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
            for(BaseFragment fragment:mFragments){
                fragment.showNetErrorView();
            }
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
    public void loadShots(int postion, List<Shot> shots,boolean success) {
        if(!success){
            mNetErrorView.setVisibility(View.VISIBLE);
            mFragments[postion].showNetErrorView();
            mRefreshLayout.setRefreshing(false);
            return;
        }
        mNetErrorView.setVisibility(GONE);
        mFragments[postion].setData(shots);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshShots(int position, List<Shot> shots,boolean success) {
        if(!success){
            mNetErrorView.setVisibility(View.VISIBLE);
            mFragments[position].showNetErrorView();
            mRefreshLayout.setRefreshing(false);
            return;
        }
        mNetErrorView.setVisibility(GONE);
        mFragments[position].refreshDate(shots);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(int position, List<Shot> shots,boolean success) {
        mFragments[position].loadMoreData(shots);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

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
}
