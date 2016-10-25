package me.mrrobot97.designer.Utils;

/**
 * Created by mrrobot on 16/10/21.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by mrrobot on 16/9/13.
 */
public class ScreenUtils {

    public static int dp2px(Context context, int dpVal){
        return (int) (dpVal*context.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context context,int pxVal){
        return (int) (pxVal/context.getResources().getDisplayMetrics().density);
    }

    public static int sp2px(Context cOntext,int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                ,spVal,cOntext.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context,int pxVal){
        return (int) (pxVal/context.getResources().getDisplayMetrics().scaledDensity);
    }

    //return the size of screen in pixel unit
    public static int[] getScreenWidthAndHeight(Context context){
        int []wh=new int[2];
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        wh[0]=metrics.widthPixels;
        wh[1]=metrics.heightPixels;
        return wh;
    }

    //return the height of statusBar
    public static int getStatusBarHeight(Context context){
        int statusBarHeight=0;
        int resourceId=context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId>0){
            statusBarHeight=context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    //return a screen capture bitmap
    public static Bitmap getScreenCapture(Activity activity){
        View view=activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap=view.getDrawingCache();
        int []wh=getScreenWidthAndHeight(activity);
        Bitmap scaled=Bitmap.createBitmap(bitmap,0,0,wh[0],wh[1]);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return scaled;
    }

}