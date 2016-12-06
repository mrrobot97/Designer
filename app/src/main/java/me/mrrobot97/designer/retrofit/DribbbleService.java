package me.mrrobot97.designer.retrofit;

import java.util.List;
import me.mrrobot97.designer.model.Attachment;
import me.mrrobot97.designer.model.Comment;
import me.mrrobot97.designer.model.Shot;
import me.mrrobot97.designer.model.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mrrobot on 16/10/21.
 */

public interface DribbbleService {
    @GET("users/{id}")
    Observable<User> loadUser(@Path("id") String id);

    @GET("shots")
    Observable<List<Shot>> loadShots(@Query("sort")String sort,@Query("page")int page,@Query("per_page")int per_page);

    @GET("shots")
    Observable<List<Shot>> loadDebutsShots(@Query("list")String list,@Query("page")int page,@Query("per_page")int per_page);

    @GET("shots/{id}")
    Observable<Shot> loadShot(@Path("id")String id);

    @GET("shots/{id}/comments")
    Observable<List<Comment>> loadComments(@Path("id")String id);

    @GET("users/{id}/shots")
    Observable<List<Shot>> loadUserShots(@Path("id")String userId);

    @GET("shots/{id}/attachments")
    Observable<List<Attachment>> loadAttachments(@Path("id")String id);

    @GET("user")
    Observable<User> loadUserProfile();

    //账号没有comment权限，无法测试功能是否正常
    @FormUrlEncoded
    @POST("shots/{id}/comments")
    Observable<Comment> postComment(@Path("id")String id,@Field("body") String comment);
}
