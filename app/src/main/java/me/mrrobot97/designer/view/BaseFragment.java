package me.mrrobot97.designer.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.adapter.ShotsAdapter;
import me.mrrobot97.designer.model.Shot;

/**
 * Created by mrrobot on 16/10/21.
 */

public class BaseFragment extends Fragment {

    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.view_loading)TextView loadingView;

    private static final String LAYOUT_ID="layout_id";
    private int layoutId;
    private ShotsAdapter mAdapter;
    private List<Shot> mData;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    private boolean isLoading=false;

    public interface SlideToBottomListener{
        void whenSlideToBottom();
    }
    private SlideToBottomListener mListener;

    public void setListener(SlideToBottomListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId=getArguments().getInt(LAYOUT_ID);
        View view=inflater.inflate(layoutId,container,false);
        ButterKnife.bind(this,view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.addItemDecoration(new ShotsAdapter.MyItemDecoration());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isSlideToBottom()){
                    if(mListener!=null&&!isLoading){
                        showLoadingView();
                        mListener.whenSlideToBottom();
                    }
                }
            }
        });
        return view;
    }

    public void setData(List<Shot> shots){
        if(mData!=null){
            throw new RuntimeException("You can call setData() only once!");
        }
        mData=shots;
        mAdapter=new ShotsAdapter(mData,getContext());
        mAdapter.setListener(new ShotsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClicked(int position) {
                Intent intent=new Intent(getContext(),DetailActivity.class);
                intent.putExtra("shot",mData.get(position));
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                    startActivity(intent);
                }else{
                    //5.0及以上系统实现共享元素动画
                    ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()
                            ,((ShotsAdapter.MyHolder)mRecyclerView.findViewHolderForAdapterPosition(position)).getImageView(),getString(R.string.transitionImage));
                    ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        loadingView.setVisibility(View.GONE);
        isLoading=false;
    }

    public void refreshDate(List<Shot> shots){
        mData=shots;
        mAdapter.notifyDataSetChanged();
        loadingView.setVisibility(View.GONE);
        isLoading=false;
    }

    public void loadMoreData(List<Shot> shots){
        if(mData==null){
            throw new RuntimeException("You must call setData() before refreshData()!");
        }
        for(Shot shot:shots){
            mData.add(shot);
        }
        mAdapter.notifyDataSetChanged();
        loadingView.setVisibility(View.GONE);
        isLoading=false;
    }

    public void showLoadingView(){
        if(loadingView!=null){
            loadingView.setVisibility(View.VISIBLE);
            isLoading=true;
        }
    }

    public void cancleLoadingView(){
      loadingView.setVisibility(View.GONE);
    }

    private boolean isSlideToBottom(){
        if(mRecyclerView==null) return false;
        if(mRecyclerView.computeVerticalScrollExtent()+mRecyclerView.computeVerticalScrollOffset()
                >=mRecyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    public static BaseFragment newInstance(@LayoutRes int layout_id){
        Bundle bundle=new Bundle();
        bundle.putInt(LAYOUT_ID,layout_id);
        BaseFragment fragment=new BaseFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


}
