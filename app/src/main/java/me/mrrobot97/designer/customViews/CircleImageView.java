package me.mrrobot97.designer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.Utils.ScreenUtils;


/**
 * Created by mrrobot on 16/10/21.
 */

public class CircleImageView extends View {

    private Paint mPaint;
    private Bitmap bitmap;
    private int resId=R.drawable.ic_account;
    private int width;
    private int height;
    private int radius;
    private BitmapShader mBitmapShader;
    private Matrix matrix;
    private int bitmapWidth;
    private int bitmapHeigh;
    private float scaleSize=1f;

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        resId=ta.getResourceId(R.styleable.CircleImageView_src,resId);
        bitmap= BitmapFactory.decodeResource(getResources(),resId);
        ta.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        width=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode==MeasureSpec.AT_MOST){
            width= ScreenUtils.dp2px(getContext(),50);
        }
        if(heightMode==MeasureSpec.AT_MOST){
            height=ScreenUtils.dp2px(getContext(),50);
        }
        setMeasuredDimension(width,height);
        radius=Math.min(width,height)/2;
        setBitmap(bitmap);
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        matrix=new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        radius=Math.min(width,height)/2;
        setBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap){
        matrix.reset();
        this.bitmap=bitmap;
        mBitmapShader=new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapWidth=bitmap.getWidth();
        bitmapHeigh=bitmap.getHeight();
        if(bitmapWidth>width&&bitmapHeigh>height){
            scaleSize=Math.min(height*1f/bitmapHeigh,width*1f/bitmapWidth);
        }else if(bitmapWidth<width&&bitmapHeigh<height){
            scaleSize=Math.max(height*1f/bitmapHeigh,width*1f/bitmapWidth);
        }
        matrix.setScale(scaleSize,scaleSize);
        mBitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(mBitmapShader);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width/2,height/2,radius,mPaint);
    }
    
}

