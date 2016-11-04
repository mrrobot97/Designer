package me.mrrobot97.designer.presenter;

import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;
import me.mrrobot97.designer.view.IDetailView;

/**
 * Created by mrrobot on 16/10/23.
 */

public class DetailPresenter implements IDetailPresenter {
    private IDetailView mView;
    private IModel mModel;

    public DetailPresenter(IDetailView view) {
        mView = view;
        mModel=new ModelImpl();
    }

    @Override
    public void loadComments(String shotId) {
        mModel.loadComments(shotId, comments -> mView.showComments(comments));
    }

    @Override
    public void loadAttachments(String id) {
        mModel.loadAttachments(id, attachments -> mView.showAttachments(attachments));
    }
}
