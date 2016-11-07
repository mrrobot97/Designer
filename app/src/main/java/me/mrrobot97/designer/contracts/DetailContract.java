package me.mrrobot97.designer.contracts;

import java.util.List;
import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;

/**
 * Created by mrrobot on 16/11/7.
 */

public interface DetailContract {
   interface IDetailView {
    void showComments(List<Comment> commments);
    void showAttachments(List<Attachment> attachments);
    void showIfCommentSuccess(Comment comment,boolean success);
  }

   interface IDetailPresenter {
    void loadComments(String shotId);

    void loadAttachments(String id);

    void postComment(String id, String comment);
  }

}
