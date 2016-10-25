package me.mrrobot97.designer.SwipeActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by mrrobot on 16/10/21.
 */

public class TouchHelper {
    //定义当前所处的状态
    private boolean isIdle=true;
    private boolean isSlinding=false;
    private boolean isAnimating=false;

    //左边触发的宽度
    private int triggerWidth=50;

    private int SHADOW_WIDTH=30;


    private Window mWindow;
    private ViewGroup preContentView;
    private ViewGroup curContentView;
    private ViewGroup curView;
    private ViewGroup preView;
    private Activity preActivity;

    private ShadowView mShadowView;



    public TouchHelper(Window window){
        mWindow=window;
    }

    private Context getContext(){
        return mWindow.getContext();
    }


    //决定是否拦截事件
    public boolean processTouchEvent(MotionEvent event){
        if(isAnimating) return true;
        float x=event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x<=triggerWidth){
                    isIdle=false;
                    isSlinding=true;
                    startSlide();
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(isSlinding) return true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isSlinding){
                    if(event.getActionIndex()!=0) return true;
                    sliding(x);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(!isSlinding) return false;
                int width=getContext().getResources().getDisplayMetrics().widthPixels;
                isAnimating=true;
                isSlinding=false;
                startAnimating(width/x<=3,x);
                return true;
            default:
                break;
        }
        return false;
    }

    private void startAnimating(final boolean isFinishing, float x) {
        int width=getContext().getResources().getDisplayMetrics().widthPixels;
        ValueAnimator animator=ValueAnimator.ofFloat(x,isFinishing?width:0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                sliding((Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                doEndWorks(isFinishing);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void doEndWorks(boolean isFinishing) {
        if(preActivity==null) return;
        if(isFinishing){
            //更改当前activity的底view为preView,防止当前activity finish时的白屏闪烁
            BackView view=new BackView(getContext());
            view.cacheView(preView);
            curContentView.addView(view,0);
        }
        curContentView.removeView(mShadowView);
        if(curContentView==null||preContentView==null) return;
        curContentView.removeView(preView);
        preContentView.addView(preView);
        if(isFinishing){
            ((Activity)getContext()).finish();
            ((Activity)getContext()).overridePendingTransition(0,0);
        }
        isAnimating=false;
        isSlinding=false;
        isIdle=true;
        preView=null;
        curView=null;
    }

    private void sliding(float rawX) {
        if(preActivity==null) return;
        curView.setX(rawX);
        preView.setX(-preView.getWidth()/3+rawX/3);
        mShadowView.setX(-SHADOW_WIDTH+rawX);
    }

    private void startSlide() {
        preActivity=((MyApplication)getContext().getApplicationContext()).getHelper().getPreActivity();
        if(preActivity==null) return;
        preContentView=(ViewGroup) preActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        preView= (ViewGroup) preContentView.getChildAt(0);
        preContentView.removeView(preView);
        curContentView=(ViewGroup) mWindow.findViewById(Window.ID_ANDROID_CONTENT);
        curView= (ViewGroup) curContentView.getChildAt(0);
        preView.setX(-preView.getWidth()/3);
        //经过实际验证,addView中的index越大,则显示的越靠上,即越靠后绘制
        curContentView.addView(preView,0);
        if(mShadowView==null){
            mShadowView=new ShadowView(getContext());
        }
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(SHADOW_WIDTH, FrameLayout.LayoutParams.MATCH_PARENT);
        curContentView.addView(mShadowView,1,params);
        mShadowView.setX(-SHADOW_WIDTH);
    }
}

//用于防止白屏闪烁
class BackView extends View {

    private View mView;
    public BackView(Context context) {
        super(context);
    }

    public void cacheView(View view){
        mView=view;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mView!=null){
            mView.draw(canvas);
            mView=null;
        }
    }
}

class ShadowView extends View{

    private Drawable mDrawable;

    public ShadowView(Context context) {
        super(context);
        int[] colors=new int[]{0x00000000, 0x17000000, 0x43000000};
        mDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        mDrawable.draw(canvas);
    }
}
