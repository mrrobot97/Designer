package me.mrrobot97.designer.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import me.mrrobot97.designer.Utils.ScreenUtils;

/**
 * Created by mrrobot on 16/10/22.
 */

public class HoverView extends View {
    private int bgColor= Color.parseColor("#CC000000");
    private int width,height;
    
    public HoverView(Context context) {
        this(context,null);
    }

    public HoverView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        width= ScreenUtils.getScreenWidthAndHeight(getContext())[0];
        height=ScreenUtils.getScreenWidthAndHeight(getContext())[1]-ScreenUtils.getStatusBarHeight(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
    }
}
