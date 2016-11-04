package me.mrrobot97.designer.view;

import java.util.List;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;

/**
 * Created by mrrobot on 16/10/24.
 */

public interface IPlayerView {
    void showPlayerInfo();
    void showPlayerInfoAsync(User user);
    void showShots(List<Shot> shots);
}
