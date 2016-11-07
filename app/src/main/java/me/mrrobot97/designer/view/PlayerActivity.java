package me.mrrobot97.designer.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.List;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.SwipeActivity.SwipeBackActivity;
import me.mrrobot97.designer.Utils.BitmapUtils;
import me.mrrobot97.designer.Utils.FileUtils;
import me.mrrobot97.designer.Utils.ScreenUtils;
import me.mrrobot97.designer.Utils.StringUtils;
import me.mrrobot97.designer.adapter.ShotsAdapter;
import me.mrrobot97.designer.contracts.PlayerContract;
import me.mrrobot97.designer.customViews.HoverView;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;
import me.mrrobot97.designer.presenter.PlayerPresenter;

//// TODO: 16/11/5 用户名与所在地址显示位置有问题,待解决
public class PlayerActivity extends SwipeBackActivity implements PlayerContract.IPlayerView, View.OnClickListener {
  @BindView(R.id.tool_bar) Toolbar mToolbar;
  @BindView(R.id.avatar) ImageView avatar;
  @BindView(R.id.name) TextView name;
  @BindView(R.id.location) TextView location;
  @BindView(R.id.follower_num) TextView followers;
  @BindView(R.id.following_num) TextView followings;
  @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
  @BindView(R.id.twitter_url) TextView twitterUrl;
  @BindView(R.id.website_url) TextView websiteUrl;
  @BindView(R.id.dribbble_url) TextView dribbbleUrl;
  @BindView(R.id.twitter_link) ImageView twitterLink;
  @BindView(R.id.browser_link) ImageView browserLink;
  @BindView(R.id.dribbble_link) ImageView dribbbleLink;
  @BindView(R.id.hover_view) HoverView mHoverView;
  @BindView(R.id.front_image_view) ImageView frontImageView;
  @BindView(R.id.twitter_view) RelativeLayout twitterView;
  @BindView(R.id.website_view) RelativeLayout webView;
  @BindView(R.id.dribbble_view) RelativeLayout dribbbleView;
  @BindView(R.id.blue_avatar) ImageView blurAvatar;

  private User mUser;
  private List<Shot> shots;
  private ShotsAdapter mAdapter;
  private PlayerContract.IPlayerPresenter mPresenter;
  private ValueAnimator mAnimator;
  private String frontImageUrl;
  private AlertDialog.Builder mBuilder;
  private AlertDialog dialog;
  private int screenWidth;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(view -> finish());
    init();
  }

  private void init() {
    mPresenter = new PlayerPresenter(this);
    mUser = (User) getIntent().getSerializableExtra("author");
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mRecyclerView.addItemDecoration(new ShotsAdapter.MyItemDecoration());
    mAnimator = ValueAnimator.ofFloat(1f, 0f);
    mAnimator.setDuration(200);
    mAnimator.setInterpolator(new DecelerateInterpolator());
    mAnimator.addUpdateListener(valueAnimator -> {
      float alpha = (float) valueAnimator.getAnimatedValue();
      mHoverView.setAlpha(alpha);
      frontImageView.setAlpha(alpha);
    });
    mAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {

      }

      @Override public void onAnimationEnd(Animator animator) {
        mHoverView.setVisibility(View.GONE);
        frontImageView.setVisibility(View.GONE);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    frontImageView.setOnClickListener(view -> mAnimator.start());
    frontImageView.setOnLongClickListener(view -> {
      dialog.show();
      return true;
    });
    mHoverView.setOnClickListener(view -> mAnimator.start());
    if (mUser != null) {
      showPlayerInfo();
    } else {
      mPresenter.loadUserProfile();
    }
    mBuilder = new AlertDialog.Builder(this);
    mBuilder.setItems(new String[] { "保存图片" }, (DialogInterface dialogInterface, int i) -> {
      FileUtils.saveImage(this, frontImageUrl);
    });
    dialog = mBuilder.create();
    screenWidth = ScreenUtils.getScreenWidthAndHeight(getApplicationContext())[0];
    blurAvatar.setMinimumHeight(screenWidth);

  }

  @Override public void showPlayerInfo() {
    generateBlurAvatar();
    Glide.with(this).load(mUser.getAvatar_url()).crossFade().into(avatar);
    name.setText(mUser.getName());
    location.setText(mUser.getLocation());
    followers.setText(mUser.getFollowers_count() + "");
    followings.setText(mUser.getFollowings_count() + "");
    mPresenter.loadUserShots(mUser.getId());
    if (checkNotNUll(mUser.getLinks().getTwitter())) {
      twitterUrl.setText(mUser.getLinks().getTwitter());
      twitterLink.setOnClickListener(this);
    } else {
      twitterView.setVisibility(View.GONE);
    }
    if (checkNotNUll(mUser.getLinks().getWeb())) {
      websiteUrl.setText(mUser.getLinks().getWeb());
      browserLink.setOnClickListener(this);
    } else {
      webView.setVisibility(View.GONE);
    }
    if (checkNotNUll(mUser.getUsername())) {
      dribbbleUrl.setText(mUser.getUsername());
      dribbbleLink.setOnClickListener(this);
    } else {
      dribbbleView.setVisibility(View.GONE);
    }
  }

  private void generateBlurAvatar() {
    SimpleTarget<Bitmap> target=new SimpleTarget<Bitmap>() {
      @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1) @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Bitmap blurImage= BitmapUtils.fastblur(resource,0.4f,8);
        runOnUiThread(()->{
          blurAvatar.setImageBitmap(blurImage);
        });
      }
    };
    Glide.with(this).load(mUser.getAvatar_url()).asBitmap().into(target);
  }

  @Override public void showPlayerInfoAsync(User user) {
    mUser = user;
    showPlayerInfo();
  }

  private boolean checkNotNUll(String str) {
    if (str == null) return false;
    if (str.trim().length() == 0) return false;
    return true;
  }

  void jumpToUrl(String url) {
    if (url == null) return;
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  @Override public void showShots(final List<Shot> shots) {
    this.shots = shots;
    mAdapter = new ShotsAdapter(shots, this);
    mAdapter.setListener(position -> {
      Shot shot = shots.get(position);
      frontImageUrl = shot.getImages().getHidpi();
      if (!StringUtils.checkAvailable(frontImageUrl)) {
        frontImageUrl = shot.getImages().getNormal();
      }
      if (!StringUtils.checkAvailable(frontImageUrl)) {
        frontImageUrl = shot.getImages().getTeaser();
      }
      Glide.with(PlayerActivity.this).load(frontImageUrl).crossFade().into(frontImageView);
      frontImageView.setAlpha(1f);
      mHoverView.setAlpha(1f);
      mHoverView.setVisibility(View.VISIBLE);
      frontImageView.setVisibility(View.VISIBLE);
    });
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onClick(View view) {
    String url = null;
    switch (view.getId()) {
      case R.id.twitter_link:
        url = mUser.getLinks().getTwitter();
        break;
      case R.id.browser_link:
        url = mUser.getLinks().getWeb();
        break;
      case R.id.dribbble_link:
        url = mUser.getHtml_url();
        break;
      default:
        break;
    }
    jumpToUrl(url);
  }
}
