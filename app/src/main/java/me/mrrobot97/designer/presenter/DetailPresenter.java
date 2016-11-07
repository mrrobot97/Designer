package me.mrrobot97.designer.presenter;

import me.mrrobot97.designer.contracts.DetailContract;
import me.mrrobot97.designer.model.IModel;
import me.mrrobot97.designer.model.ModelImpl;

/**
 * Created by mrrobot on 16/10/23.
 */

public class DetailPresenter implements DetailContract.IDetailPresenter {
    private DetailContract.IDetailView mView;
    private IModel mModel;

    public DetailPresenter(DetailContract.IDetailView view) {
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

    @Override public void postComment(String id, String comment) {
        mModel.postComment(id,comment,(comment1,success) -> mView.showIfCommentSuccess(comment1,success));
    }
}
