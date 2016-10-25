package me.mrrobot97.designer.retrofit;

import java.util.List;

import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mrrobot on 16/10/21.
 */

public interface DribbbleService {
    @GET("users/{id}")
    rx.Observable<User> loadUser(@Path("id") String id);

    @GET("shots")
    rx.Observable<List<Shot>> loadShots(@Query("sort")String sort,@Query("page")int page,@Query("per_page")int per_page);

    @GET("shots")
    rx.Observable<List<Shot>> loadDebutsShots(@Query("list")String list,@Query("page")int page,@Query("per_page")int per_page);

    @GET("shots/{id}")
    rx.Observable<Shot> loadShot(@Path("id")String id);

    @GET("shots/{id}/comments")
    rx.Observable<List<Comment>> loadComments(@Path("id")String id);

    @GET("users/{id}/shots")
    rx.Observable<List<Shot>> loadUserShots(@Path("id")String userId);

    @GET("shots/{id}/attachments")
    rx.Observable<List<Attachment>> loadAttachments(@Path("id")String id);


}
