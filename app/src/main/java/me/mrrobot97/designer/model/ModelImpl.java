package me.mrrobot97.designer.model;

import android.util.Log;
import java.util.List;
import me.mrrobot97.designer.retrofit.ApiClient;
import me.mrrobot97.designer.retrofit.DribbbleService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mrrobot on 16/10/21.
 */

public class ModelImpl implements IModel {
    private DribbbleService observable;

    public ModelImpl() {
        observable= ApiClient.getRetrofit().create(DribbbleService.class);
    }

    @Override
    public void loadShots(String sort, int page, int per_page, final ShotsListener listener) {
        if(sort.equals("debuts")){
            observable.loadDebutsShots(sort,page,per_page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Shot>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("yjw","onError");
                            listener.onShotsLoaded(null,false);
                        }

                        @Override
                        public void onNext(List<Shot> shots) {
                            listener.onShotsLoaded(shots,true);
                        }
                    });
        }else{
            observable.loadShots(sort,page,per_page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Shot>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("yjw","onError");
                            listener.onShotsLoaded(null,false);
                        }

                        @Override
                        public void onNext(List<Shot> shots) {
                            listener.onShotsLoaded(shots,true);
                        }
                    });
        }

    }

    @Override
    public void loadShotDetail(String id, final ShotListener listener) {
        observable.loadShot(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Shot>() {
                    @Override
                    public void onCompleted() {
                        Log.d("yjw","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("yjw","onError");
                    }

                    @Override
                    public void onNext(Shot shot) {
                        listener.onShotLoaded(shot);
                    }
                });

    }

    @Override
    public void loadUser(String id, final UserListener listener) {
        observable.loadUser(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d("yjw","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("yjw","onError");
                    }

                    @Override
                    public void onNext(User user) {
                        listener.onUserLoaded(user);
                    }
                });
    }

    @Override
    public void loadComments(String shotId, final CommentsListener listener) {
        observable.loadComments(shotId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("yjw","comments loading error");
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        listener.onCommentsLoader(comments);
                    }
                });
    }

    @Override
    public void loadUserShots(String userId, final UserShotsLoadListener listener) {
        observable.loadUserShots(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Shot>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("yjw","shots loading error");
                    }

                    @Override
                    public void onNext(List<Shot> shots) {
                        listener.onUserShotsLoaded(shots);
                    }
                });
    }

    @Override
    public void loadAttachments(String id, final AttachmentsLoadListener listener) {
        observable.loadAttachments(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Attachment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("yjw","shots loading error");
                    }

                    @Override
                    public void onNext(List<Attachment> attachments) {
                        listener.onAttachmentsLoaded(attachments);
                    }
                });
    }

  @Override public void loadUserProfile(String token, final UserListener listener) {
      observable.loadUserProfile(token)
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<User>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {
              Log.d("yjw","user profile load error");
            }

            @Override public void onNext(User user) {
              listener.onUserLoaded(user);
            }
          });
  }
}
