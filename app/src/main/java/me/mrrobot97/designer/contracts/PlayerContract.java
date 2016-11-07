package me.mrrobot97.designer.contracts;

import java.util.List;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;

/**
 * Created by mrrobot on 16/11/7.
 */

public interface PlayerContract {
   interface IPlayerView {
    void showPlayerInfo();
    void showPlayerInfoAsync(User user);
    void showShots(List<Shot> shots);
  }

   interface IPlayerPresenter {
    void loadUserShots(String userId);
    void loadUserProfile();
  }
}
