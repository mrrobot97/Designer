package me.mrrobot97.designer.presenter;

import java.util.List;

import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.view.IBrowseView;

/**
 * Created by mrrobot on 16/10/21.
 */

public class BrowsePresenterImpl implements IBrowsePresenter{

    private IBrowseView mView;
    private IModel mModel;
    private int[] pages=new int[]{1,1,1};
    private final int PER_PAGE=30;
    private String[] sorts=new String[]{"popular","debuts","recent"};

    public BrowsePresenterImpl(IBrowseView view) {
        mView = view;
        mModel=new ModelImpl();
    }


    @Override
    public void load(final int position) {
        pages[position]=1;
        mModel.loadShots(sorts[position], pages[position], PER_PAGE, new IModel.ShotsListener() {
            @Override
            public void onShotsLoaded(List<Shot> shots,boolean success) {
                mView.loadShots(position,shots,success);
                if(success) pages[position]++;
            }
        });
    }

    @Override
    public void refresh(final int position) {
        pages[position]=1;
        mModel.loadShots(sorts[position], pages[position], PER_PAGE, new IModel.ShotsListener() {
            @Override
            public void onShotsLoaded(List<Shot> shots,boolean success) {
                mView.refreshShots(position,shots,success);
                if(success)pages[position]++;
            }
        });
    }

    @Override
    public void loadMore(final int position) {
        mModel.loadShots(sorts[position], pages[position], PER_PAGE, new IModel.ShotsListener() {
            @Override
            public void onShotsLoaded(List<Shot> shots,boolean success) {
                mView.loadMore(position,shots,success);
                if(success)pages[position]++;
            }
        });
    }
}
