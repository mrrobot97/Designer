package me.mrrobot97.designer.model;

import java.util.List;

/**
 * Created by mrrobot on 16/10/21.
 */

public interface IModel {
  void loadShots(String sort, int page, int per_page, ShotsListener listener);

  void loadShotDetail(String id, ShotListener listener);

  void loadUser(String id, UserListener listener);

  void loadComments(String shotId, CommentsListener listener);

  void loadUserShots(String userId, UserShotsLoadListener listener);

  void loadAttachments(String id, AttachmentsLoadListener listener);

  void loadUserProfile( UserListener listener);

  void postComment(String id,String comment,CommentPostListener listener);

  interface ShotsListener {
    void onShotsLoaded(List<Shot> shots, boolean success);
  }

  interface ShotListener {
    void onShotLoaded(Shot shot);
  }

  interface UserListener {
    void onUserLoaded(User user);
  }

  interface CommentsListener {
    void onCommentsLoader(List<Comment> comments);
  }

  interface UserShotsLoadListener {
    void onUserShotsLoaded(List<Shot> shots);
  }

  interface AttachmentsLoadListener {
    void onAttachmentsLoaded(List<Attachment> attachments);
  }

  interface CommentPostListener {
    void onCommentPosted(Comment comment,boolean success);
  }
}
