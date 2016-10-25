package me.mrrobot97.designer.SwipeActivity;

import android.app.Application;

/**
 * Created by mrrobot on 16/10/21.
 */

public class MyApplication extends Application {
    public ActivityLifeCycleHelper getHelper() {
        return mHelper;
    }

    private ActivityLifeCycleHelper mHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        mHelper=new ActivityLifeCycleHelper();
        //store all the activities
        registerActivityLifecycleCallbacks(mHelper);
    }
}
