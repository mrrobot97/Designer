package me.mrrobot97.designer.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.SwipeActivity.SwipeBackActivity;
import me.mrrobot97.designer.Utils.ScreenUtils;
import me.mrrobot97.designer.adapter.AttachmentsAdapter;
import me.mrrobot97.designer.customViews.CircleImageView;
import me.mrrobot97.designer.customViews.HoverView;
import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.presenter.DetailPresenter;
import me.mrrobot97.designer.presenter.IDetailPresenter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class DetailActivity extends SwipeBackActivity implements IDetailView {
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
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R.id.share_iv) ImageView mShareIv;
  @BindView(R.id.recyclerview) RecyclerView attachmentRecyclerview;
  @BindView(R.id.attachment_layout) RelativeLayout attachmentLayout;

  private Shot mShot;
  private int screenWidth;
  private ValueAnimator mAnimator;
  private AlertDialog.Builder mBuilder;
  private AlertDialog dialog;
  private OkHttpClient client;
  private String url;
  private List<Comment> mComments;
  private IDetailPresenter mPresenter;
  private MyAdapter mAdapter = new MyAdapter();
  private AttachmentsAdapter attachAdapter;
  private String frontImageUrl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        finish();
      }
    });
    init();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    init();
  }

  private void init() {
    mShot = (Shot) getIntent().getSerializableExtra("shot");
    mPresenter = new DetailPresenter(this);
    client = new OkHttpClient();
    mAnimator = ValueAnimator.ofFloat(1f, 0f);
    mAnimator.setDuration(200);
    mAnimator.setInterpolator(new DecelerateInterpolator());
    mAnimator.addUpdateListener(valueAnimator -> {
      float alpha = (float) valueAnimator.getAnimatedValue();
      mHoverView.setAlpha(alpha);
      frontImageView.setAlpha(alpha);
    });
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    attachmentRecyclerview.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
    screenWidth = ScreenUtils.getScreenWidthAndHeight(getApplicationContext())[0];
    shotTitle.setText(mShot.getTitle());
    author.setText(mShot.getUser().getUsername());
    viewCnt.setText(mShot.getViews_count() + "");
    commentsCnt.setText(mShot.getComments_count() + "");
    likesCnt.setText(mShot.getLikes_count() + "");
    mShareIv.setOnClickListener(view -> shareShot());
    SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        avatar.setBitmap(resource);
      }
    };
    screenWidth = ScreenUtils.getScreenWidthAndHeight(getApplicationContext())[0];
    mImageView.setMinimumHeight(screenWidth * 3 / 4);
    url = mShot.getImages().getHidpi();
    if (url == null || url.trim().length() == 0) {
      url = mShot.getImages().getNormal();
    }
    Glide.with(this).load(mShot.getUser().getAvatar_url()).asBitmap().into(target);
    Glide.with(this).load(url).crossFade().into(mImageView);
    Glide.with(this).load(url).crossFade().into(frontImageView);
    mImageView.setOnClickListener(view -> {frontImageUrl = url;
      showFullScreenView();});
    mBuilder = new AlertDialog.Builder(this);
    mBuilder.setItems(new String[] { "保存图片" }, (DialogInterface dialogInterface,int i)->{saveImage();});
    dialog = mBuilder.create();
    frontImageView.setOnClickListener(view -> mAnimator.start());
    mHoverView.setOnClickListener(view -> mAnimator.start());
    frontImageView.setOnLongClickListener(view -> {
      dialog.show();
      return true;});
    //load attachments
    if (mShot.getAttachments_count() > 0) {
      mPresenter.loadAttachments(mShot.getId());
    }
    //load comments
    mPresenter.loadComments(mShot.getId());

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

  private void saveImage() {
    if (!isExternalStroageWritable()) {
      Toast.makeText(this, "SD card not available", Toast.LENGTH_SHORT).show();
      return;
    }
    File dir = new File(Environment.getExternalStorageDirectory(), "Designer");
    if (!dir.exists()) dir.mkdir();
    final File file = new File(dir,
        frontImageUrl.substring(frontImageUrl.lastIndexOf(File.separator), frontImageUrl.length()));
    Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        Request request = new Request.Builder().url(frontImageUrl).build();
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
            Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
          }

          @Override public void onNext(Void aVoid) {
            Log.d("yjw", file.getAbsolutePath());
            Toast.makeText(DetailActivity.this, "Save image to " + file.getAbsolutePath(),
                Toast.LENGTH_SHORT).show();
          }
        });
  }

  private boolean isExternalStroageWritable() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      return true;
    }
    return false;
  }

  @Override public void showComments(List<Comment> commments) {
    mComments = commments;
    mAdapter = new MyAdapter();
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void showAttachments(final List<Attachment> attachments) {
    attachmentLayout.setVisibility(View.VISIBLE);
    attachmentRecyclerview.setVisibility(View.VISIBLE);
    attachAdapter = new AttachmentsAdapter(attachments, DetailActivity.this);
    attachAdapter.setListener(position->{
        Attachment attachment = attachments.get(position);
        frontImageUrl = attachment.getUrl();
        showFullScreenView();
    });
    attachmentRecyclerview.setAdapter(attachAdapter);
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
