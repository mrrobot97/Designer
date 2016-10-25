package me.mrrobot97.designer.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.SwipeActivity.SwipeBackActivity;
import me.mrrobot97.designer.adapter.ShotsAdapter;
import me.mrrobot97.designer.customViews.HoverView;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;
import me.mrrobot97.designer.presenter.IPlayerPresenter;
import me.mrrobot97.designer.presenter.PlayerPresenterImpl;

public class PlayerActivity extends SwipeBackActivity implements IPlayerView ,View.OnClickListener{
    @BindView(R.id.tool_bar)Toolbar mToolbar;
    @BindView(R.id.avatar)ImageView avatar;
    @BindView(R.id.name)TextView name;
    @BindView(R.id.location)TextView location;
    @BindView(R.id.follower_num)TextView followers;
    @BindView(R.id.following_num)TextView followings;
    @BindView(R.id.recyclerview)RecyclerView mRecyclerView;
    @BindView(R.id.twitter_url)TextView twitterUrl;
    @BindView(R.id.website_url)TextView websiteUrl;
    @BindView(R.id.dribbble_url)TextView dribbbleUrl;
    @BindView(R.id.twitter_link)ImageView twitterLink;
    @BindView(R.id.browser_link)ImageView browserLink;
    @BindView(R.id.dribbble_link)ImageView dribbbleLink;
    @BindView(R.id.hover_view)HoverView mHoverView;
    @BindView(R.id.front_image_view)ImageView frontImageView;

    private User mUser;
    private List<Shot> shots;
    private List<Shot> likes;
    private ShotsAdapter mAdapter;
    private IPlayerPresenter mPresenter;
    private ValueAnimator mAnimator;

    //// TODO: 16/10/24  添加共享动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
    }

    private void init() {
        mPresenter=new PlayerPresenterImpl(this);
        mUser= (User) getIntent().getSerializableExtra("author");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.addItemDecoration(new ShotsAdapter.MyItemDecoration());
        mAnimator=ValueAnimator.ofFloat(1f,0f);
        mAnimator.setDuration(200);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha= (float) valueAnimator.getAnimatedValue();
                mHoverView.setAlpha(alpha);
                frontImageView.setAlpha(alpha);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mHoverView.setVisibility(View.GONE);
                frontImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        frontImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimator.start();
            }
        });
        mHoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimator.start();
            }
        });
        showPlayerInfo();

    }

    @Override
    public void showPlayerInfo() {
        Glide.with(this).load(mUser.getAvatar_url()).crossFade().into(avatar);
        name.setText(mUser.getName());
        location.setText(mUser.getLocation());
        followers.setText(mUser.getFollowers_count()+"");
        followings.setText(mUser.getFollowings_count()+"");
        mPresenter.loadUserShots(mUser.getId());
        twitterUrl.setText(mUser.getLinks().getTwitter());
        websiteUrl.setText(mUser.getLinks().getWeb());
        dribbbleUrl.setText(mUser.getUsername());
        twitterLink.setOnClickListener(this);
        browserLink.setOnClickListener(this);
        dribbbleLink.setOnClickListener(this);
    }

    void jumpToUrl(String url){
        if(url==null) return;
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void showShots(final List<Shot> shots) {
        this.shots=shots;
        mAdapter=new ShotsAdapter(shots,this);
        mAdapter.setListener(new ShotsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClicked(int position) {
                Shot shot=shots.get(position);
                Glide.with(PlayerActivity.this).load(shot.getImages().getHidpi()).crossFade().into(frontImageView);
                frontImageView.setAlpha(1f);
                mHoverView.setAlpha(1f);
                mHoverView.setVisibility(View.VISIBLE);
                frontImageView.setVisibility(View.VISIBLE);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        String url=null;
        switch (view.getId()){
            case R.id.twitter_link:
                url=mUser.getLinks().getTwitter();
                break;
            case R.id.browser_link:
                url=mUser.getLinks().getWeb();
                break;
            case R.id.dribbble_link:
                url=mUser.getHtml_url();
                break;
            default:
                break;
        }
        jumpToUrl(url);
    }
}
