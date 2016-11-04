package me.mrrobot97.designer.SwipeActivity;

import android.app.Application;
import android.content.Context;

/**
 * Created by mrrobot on 16/10/21.
 */

public class MyApplication extends Application {
    public ActivityLifeCycleHelper getHelper() {
        return mHelper;
    }

    private static Context mContext;

    private ActivityLifeCycleHelper mHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        mHelper=new ActivityLifeCycleHelper();
        //store all the activities
        registerActivityLifecycleCallbacks(mHelper);
        mContext=this.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
