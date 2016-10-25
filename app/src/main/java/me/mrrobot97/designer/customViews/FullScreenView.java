package me.mrrobot97.designer.customViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import me.mrrobot97.designer.Utils.ScreenUtils;

/**
 * Created by mrrobot on 16/10/22.
 */

public class FullScreenView extends ImageView {
    private Paint mPaint;
    private int bgColor= Color.parseColor("#CC000000");
    private int width,height;
    private Drawable mDrawable;
    private Bitmap bitmap;
    private Matrix mMatrix;
    private ValueAnimator mAnimator;


    public FullScreenView(Context context) {
        this(context,null);
    }

    public FullScreenView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FullScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mMatrix=new Matrix();
        mPaint.setColor(bgColor);
        width= ScreenUtils.getScreenWidthAndHeight(getContext())[0];
        height=ScreenUtils.getScreenWidthAndHeight(getContext())[1]-ScreenUtils.getStatusBarHeight(getContext());
        mAnimator=ValueAnimator.ofFloat(1f,0f);
        mAnimator.setDuration(200);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(GONE);
                setAlpha(1f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(bgColor);
        canvas.drawColor(bgColor);
        mDrawable=getDrawable();
        if(mDrawable!=null){
            bitmap= ((BitmapDrawable)mDrawable).getBitmap();
        }
        if(bitmap!=null){
            int w=bitmap.getWidth();
            int h=bitmap.getHeight();
            mMatrix.reset();
            float scaleSize=width*1f/w;
            mMatrix.setScale(scaleSize,scaleSize);
            mMatrix.postTranslate(0,(height-h*scaleSize)/2);
            mPaint.setColor(Color.WHITE);
            canvas.drawBitmap(bitmap,mMatrix,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!mAnimator.isRunning()){
                mAnimator.start();
            }
        }
        return true;
    }
}