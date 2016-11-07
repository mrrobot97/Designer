package me.mrrobot97.designer.presenter;

import me.mrrobot97.designer.contracts.PlayerContract;
import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;

/**
 * Created by mrrobot on 16/10/24.
 */

public class PlayerPresenter implements PlayerContract.IPlayerPresenter {
    private PlayerContract.IPlayerView mView;
    private IModel mModel;

    public PlayerPresenter(PlayerContract.IPlayerView view) {
        mView = view;
        mModel=new ModelImpl();
    }

    @Override
    public void loadUserShots(String userId) {
        mModel.loadUserShots(userId, shots -> mView.showShots(shots));
    }

    @Override public void loadUserProfile() {
        mModel.loadUserProfile( user -> mView.showPlayerInfoAsync(user));
    }
}
