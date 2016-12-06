package me.mrrobot97.designer;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import me.mrrobot97.designer.SwipeActivity.ActivityLifeCycleHelper;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        mHelper=new ActivityLifeCycleHelper();
        //store all the activities
        registerActivityLifecycleCallbacks(mHelper);
        mContext=this.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
