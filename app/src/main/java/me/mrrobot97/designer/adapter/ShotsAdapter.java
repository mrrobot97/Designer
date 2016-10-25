package me.mrrobot97.designer.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.mrrobot97.designer.R;
import me.mrrobot97.designer.Utils.ScreenUtils;
import me.mrrobot97.designer.model.Shot;

/**
 * Created by mrrobot on 16/10/24.
 */
public class ShotsAdapter extends RecyclerView.Adapter{

    public void setData(List<Shot> data) {
        mData = data;
    }

    private List<Shot> mData;
    private int screenWidth;
    private Context mContext;
    private static final int offset=8;

    public ShotsAdapter(List<Shot> data, Context context) {
        mData = data;
        mContext=context;
        screenWidth= ScreenUtils.getScreenWidthAndHeight(mContext)[0];
    }

    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_layout,null);
        //设置宽度
        int width=(screenWidth-offset*4)/3;
        int height= width*3/4;
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(width,height);
        view.setLayoutParams(params);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Shot shot=mData.get(position);
        //根据实际屏幕分辨率确定要加载的缩略图的尺寸
        if(screenWidth>=720){
            Glide.with(mContext).load(shot.getImages().getNormal()).crossFade().into(((MyHolder)holder).mImageView);
        }else{
            Glide.with(mContext).load(shot.getImages().getTeaser()).crossFade().into(((MyHolder)holder).mImageView);
        }
        ((MyHolder)holder).mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.OnItemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        public ImageView getImageView() {
            return mImageView;
        }

        @BindView(R.id.image_view)ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(offset,offset,offset,offset);
        }
    }

    public interface OnItemClickListener{
        void OnItemClicked(int position);
    }
}


