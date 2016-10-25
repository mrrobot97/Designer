package me.mrrobot97.designer.presenter;

import java.util.List;

import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;
import me.mrrobot97.designer.model.Shot;
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
        mModel.loadUserShots(userId, new IModel.UserShotsLoadListener() {
            @Override
            public void onUserShotsLoaded(List<Shot> shots) {
                mView.showShots(shots);
            }
        });
    }

}
