package me.mrrobot97.designer.view;

import java.util.List;

import me.mrrobot97.designer.model.Shot;

/**
 * Created by mrrobot on 16/10/21.
 */

public interface IBrowseView {
    void loadShots(int position,List<Shot> shots,boolean success);
    void refreshShots(int position,List<Shot> shots,boolean success);
    void loadMore(int position,List<Shot> shots,boolean success);
    void showLoading();
    void showError();
    void requestPermissions();
}
