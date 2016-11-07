package me.mrrobot97.designer.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import me.mrrobot97.designer.Utils.SharedPreferencesUtils;
import me.mrrobot97.designer.adapter.AttachmentsAdapter;
import me.mrrobot97.designer.contracts.DetailContract;
import me.mrrobot97.designer.customViews.CircleImageView;
import me.mrrobot97.designer.customViews.HoverView;
import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.presenter.DetailPresenter;
import me.mrrobot97.designer.retrofit.ApiClient;
import okhttp3.OkHttpClient;

import static me.mrrobot97.designer.Utils.StringUtils.checkAvailable;

//// TODO: 16/11/4 DetailActivity界面加载图片太慢，待解决
public class DetailActivity extends SwipeBackActivity implements DetailContract.IDetailView {
  @BindView(R.id.avatar) CircleImageView avatar;
  @BindView(R.id.shot_title) TextView shotTitle;
  @BindView(R.id.author) TextView author;
  @BindView(R.id.image_view) ImageView mImageView;
  @BindView(R.id.tool_bar) Toolbar mToolbar;
  @BindView(R.id.view_cnt) TextView viewCnt;
  @BindView(R.id.comments_cnt) TextView commentsCnt;
  @BindView(R.id.likes_cnt) TextView likesCnt;
  @BindView(R.id.hover_view) HoverView mHoverView;
  @BindView(R.id.front_image_view) ImageView frontImageView;
  @BindView(R.id.comments_recycler_view) RecyclerView mCommentsRecycler;
  @BindView(R.id.share_iv) ImageView mShareIv;
  @BindView(R.id.recyclerview) RecyclerView attachmentRecyclerview;
  @BindView(R.id.attachment_layout) RelativeLayout attachmentLayout;
  @BindView(R.id.profile_layout)RelativeLayout profileLayout;
  @BindView(R.id.edit_comment)EditText mEditComment;
  @BindView(R.id.bt_send)Button mBtSend;

  public static final int ANIM_DURATION=500;

  private Shot mShot;
  private int screenWidth;
  private ValueAnimator mAnimator;
  private AlertDialog.Builder mBuilder;
  private AlertDialog dialog;
  private OkHttpClient client;
  private String url;
  private List<Comment> mComments;
  private DetailContract.IDetailPresenter mPresenter;
  private MyAdapter mAdapter = new MyAdapter();
  private AttachmentsAdapter attachAdapter;
  private String frontImageUrl;
  private boolean isLogin=false;
  private String token=null;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(view -> finish());
    screenWidth = ScreenUtils.getScreenWidthAndHeight(getApplicationContext())[0];
    isLogin= (boolean) SharedPreferencesUtils.getFromSpfs(this,"login",false);
    token= (String) SharedPreferencesUtils.getFromSpfs(this,"access_token",null);
    init();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    init();
  }

  private void prepareAnimator(){
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
  }

  private void init() {
    mShot = (Shot) getIntent().getSerializableExtra("shot");
    mPresenter = new DetailPresenter(this);
    client = ApiClient.getClient();
    mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
    attachmentRecyclerview.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mImageView.setMinimumHeight(screenWidth * 3 / 4);
    url = mShot.getImages().getHidpi();
    if (!checkAvailable(url)) {
      url = mShot.getImages().getNormal();
    }if (!checkAvailable(url)) {
      url = mShot.getImages().getTeaser();
    }
    mBuilder = new AlertDialog.Builder(this);
    mBuilder.setItems(new String[] { "保存图片" }, (DialogInterface dialogInterface, int i) -> {
      FileUtils.saveImage(this,frontImageUrl);
    });
    dialog = mBuilder.create();
    setListener();
    prepareAnimator();
    setTextInfo();
    changeToolBarColor();
    generateBlurBitmap();
    loadData();
  }

  private void setListener() {
    mImageView.setOnClickListener(view -> {
      frontImageUrl = url;
      showFullScreenView();
    });
    frontImageView.setOnClickListener(view -> mAnimator.start());
    mHoverView.setOnClickListener(view -> mAnimator.start());
    frontImageView.setOnLongClickListener(view -> {
      dialog.show();
      return true;
    });
    avatar.setOnClickListener(view -> {
      Intent intent = new Intent(DetailActivity.this, PlayerActivity.class);
      intent.putExtra("author", mShot.getUser());
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        startActivity(intent);
      } else {
        //5.0及以上系统实现共享元素动画
        ActivityOptionsCompat options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(DetailActivity.this, avatar,
                getString(R.string.transitionAvatar));
        ActivityCompat.startActivity(DetailActivity.this, intent, options.toBundle());
      }
    });
    if(!isLogin){
      mEditComment.setEnabled(false);
    }
    mBtSend.setOnClickListener(view -> {
      if(!isLogin){
        Toast.makeText(this, "Please sign in.", Toast.LENGTH_SHORT).show();
        return;
      }
      String comment=mEditComment.getText().toString();
      if(comment==null||comment.trim().length()==0) {
        Toast.makeText(this, "Empty comment.", Toast.LENGTH_SHORT).show();
        return;
      }
        mEditComment.setText("");
        mPresenter.postComment(mShot.getId(),comment);
    });
  }

  private void setTextInfo() {
    shotTitle.setText(mShot.getTitle());
    author.setText(mShot.getUser().getUsername());
    viewCnt.setText(mShot.getViews_count() + "");
    commentsCnt.setText(mShot.getComments_count() + "");
    likesCnt.setText(mShot.getLikes_count() + "");
    mShareIv.setOnClickListener(view -> shareShot());
  }

  private void loadData() {
    //load avatar
    SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        avatar.setBitmap(resource);
      }
    };
    Glide.with(this).load(mShot.getUser().getAvatar_url()).asBitmap().into(target);
    //load attachments
    if (mShot.getAttachments_count() > 0) {
      mPresenter.loadAttachments(mShot.getId());
    }
    //load comments
    mPresenter.loadComments(mShot.getId());
  }

  private void changeToolBarColor() {
    //使用Palette动态改变Toolbar背景色
    SimpleTarget<Bitmap> targetPalette=new SimpleTarget<Bitmap>() {
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Palette.from(resource).generate(palette -> {
          int vibrantColor=palette.getVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
          mToolbar.post(()->{
            mToolbar.setBackgroundColor(vibrantColor);
            profileLayout.setBackgroundColor(vibrantColor);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
              getWindow().setStatusBarColor(vibrantColor);
          });
        });
      }
    };
    Glide.with(this).load(mShot.getImages().getTeaser()).asBitmap().into(targetPalette);
  }

  private void generateBlurBitmap() {
    String blurUrl=null;
    //复用在BrowseAcrtivity中下载的缩略图
    if (screenWidth >= 720) {
      blurUrl=mShot.getImages().getNormal();
      if(!checkAvailable(url)) url=mShot.getImages().getTeaser();
    } else {
      blurUrl=mShot.getImages().getTeaser();
    }
    SimpleTarget<Bitmap> target=new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Bitmap blurImage= BitmapUtils.fastblur(resource,0.4f,8);
        BitmapDrawable drawable=new BitmapDrawable(blurImage);
        Glide.with(DetailActivity.this).load(url).placeholder(drawable).crossFade().into(mImageView);
        Glide.with(DetailActivity.this).load(url).placeholder(drawable).crossFade().into(frontImageView);
      }
    };
    Glide.with(DetailActivity.this).load(blurUrl).asBitmap().into(target);
  }

  void showFullScreenView() {
    mHoverView.setVisibility(View.VISIBLE);
    frontImageView.setVisibility(View.VISIBLE);
    mHoverView.setAlpha(1f);
    frontImageView.setAlpha(1f);
    Glide.with(DetailActivity.this).load(frontImageUrl).crossFade().into(frontImageView);
  }

  private void shareShot() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");

    intent.putExtra(Intent.EXTRA_SUBJECT, mShot.getTitle());
    intent.putExtra(Intent.EXTRA_TEXT,
        "I found an amazing design in dribbble: " + mShot.getHtml_url());
    startActivity(Intent.createChooser(intent, "Share design"));
  }


  @Override public void showComments(List<Comment> commments) {
    mComments = commments;
    mAdapter = new MyAdapter();
    mCommentsRecycler.setAdapter(mAdapter);
    mCommentsRecycler.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        mCommentsRecycler.getViewTreeObserver().removeOnPreDrawListener(this);
        mCommentsRecycler.setScaleY(0.1f);
        mCommentsRecycler.animate()
            .scaleY(1f)
            .setDuration(ANIM_DURATION)
            .setInterpolator(new AccelerateInterpolator())
            .start();
        return true;
      }
    });
  }

  @Override public void showAttachments(final List<Attachment> attachments) {
    attachmentLayout.setVisibility(View.VISIBLE);
    attachmentRecyclerview.setVisibility(View.VISIBLE);
    attachAdapter = new AttachmentsAdapter(attachments, DetailActivity.this);
    //attachAdapter.setListener(position -> {
    //  Attachment attachment = attachments.get(position);
    //  frontImageUrl = attachment.getUrl();
    //  showFullScreenView();
    //});
    attachAdapter.setListener(position -> {
      Attachment attachment = attachments.get(position);
      frontImageUrl = attachment.getUrl();
      showFullScreenView();
    });
    attachmentRecyclerview.setAdapter(attachAdapter);
  }

  @Override public void showIfCommentSuccess(Comment comment,boolean success) {
    if (success){
      mComments.add(comment);
      mAdapter.notifyItemInserted(mComments.size()-1);
    }else{
      Toast.makeText(this, "Comment failed,Please make that you have permission to comment.", Toast.LENGTH_SHORT).show();
    }
  }

  class MyAdapter extends RecyclerView.Adapter {

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(DetailActivity.this)
          .inflate(R.layout.comment_item_layout, parent, false);
      MyHolder holder = new MyHolder(view);
      return holder;
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
      Comment comment = mComments.get(position);
      SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
        @Override public void onResourceReady(Bitmap resource,
            GlideAnimation<? super Bitmap> glideAnimation) {
          ((MyHolder) holder).mAvatar.setBitmap(resource);
        }
      };
      Glide.with(DetailActivity.this)
          .load(comment.getUser().getAvatar_url())
          .asBitmap()
          .into(target);
      ((MyHolder) holder).mAuthor.setText(comment.getUser().getUsername());
      Spannable sp = (Spannable) Html.fromHtml(comment.getBody());
      ((MyHolder) holder).mContent.setText(sp.toString().trim());
      if (position % 2 != 0) {
        ((MyHolder) holder).mRelativeLayout.setBackgroundColor(getColor(R.color.textColorWhite));
      }
    }

    @Override public int getItemCount() {
      return mComments.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.comment_background) RelativeLayout mRelativeLayout;
      @BindView(R.id.comment_avatar) CircleImageView mAvatar;
      @BindView(R.id.comment_content) TextView mContent;
      @BindView(R.id.comment_author) TextView mAuthor;

      public MyHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }
}
