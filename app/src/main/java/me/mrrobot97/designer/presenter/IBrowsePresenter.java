package me.mrrobot97.designer.presenter;

/**
 * Created by mrrobot on 16/10/21.
 */

public interface IBrowsePresenter {
    void load(int position);
    void refresh(int position);
    void loadMore(int position);
    void loadUserProile(String token);
}
