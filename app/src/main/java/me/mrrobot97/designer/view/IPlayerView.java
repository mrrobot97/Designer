package me.mrrobot97.designer.view;

import java.util.List;

import me.mrrobot97.designer.model.Shot;

/**
 * Created by mrrobot on 16/10/24.
 */

public interface IPlayerView {
    void showPlayerInfo();
    void showShots(List<Shot> shots);
}
