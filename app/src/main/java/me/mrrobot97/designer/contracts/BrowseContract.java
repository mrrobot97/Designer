package me.mrrobot97.designer.contracts;

import java.util.List;
import me.mrrobot97.designer.model.Shot;

/**
 * Created by mrrobot on 16/11/7.
 */

public interface BrowseContract {
   public interface IBrowseView {
    void loadShots(int position,List<Shot> shots,boolean success);
    void refreshShots(int position,List<Shot> shots,boolean success);
    void loadMore(int position,List<Shot> shots,boolean success);
    void requestPermissions();
    void showUserProfile();
  }

   public interface IBrowsePresenter {
    void load(int position);
    void refresh(int position);
    void loadMore(int position);
  }


}
