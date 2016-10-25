package me.mrrobot97.designer.view;

import java.util.List;

import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;

/**
 * Created by mrrobot on 16/10/23.
 */

public interface IDetailView {
    void showComments(List<Comment> commments);
    void showAttachments(List<Attachment> attachments);
}
