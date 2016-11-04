package me.mrrobot97.designer.presenter;

import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;
import me.mrrobot97.designer.view.IPlayerView;

/**
 * Created by mrrobot on 16/10/24.
 */

public class PlayerPresenterImpl implements IPlayerPresenter {
    private IPlayerView mView;
    private IModel mModel;

    public PlayerPresenterImpl(IPlayerView view) {
        mView = view;
        mModel=new ModelImpl();
    }

    @Override
    public void loadUserShots(String userId) {
        mModel.loadUserShots(userId, shots -> mView.showShots(shots));
    }

    @Override public void loadUserProfile(String token) {
        mModel.loadUserProfile(token, user -> mView.showPlayerInfoAsync(user));
    }
}
