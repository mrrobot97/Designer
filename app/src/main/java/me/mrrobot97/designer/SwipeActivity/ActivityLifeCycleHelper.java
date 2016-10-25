package me.mrrobot97.designer.SwipeActivity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

/**
 * Created by mrrobot on 16/10/21.
 */

public class ActivityLifeCycleHelper implements Application.ActivityLifecycleCallbacks {
    private Stack<Activity> mActivities;

    public ActivityLifeCycleHelper(){
        mActivities=new Stack<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }

    public Activity getPreActivity(){
        int size=mActivities.size();
        if(size<2) return null;
        else return mActivities.get(size-2);
    }
}