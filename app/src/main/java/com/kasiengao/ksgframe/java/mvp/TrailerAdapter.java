package com.kasiengao.ksgframe.java.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;

import java.util.List;

/**
 * @ClassName: MvpAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:37
 * @Description: 预告视频Adapter
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;

    private List<TrailerBean.TrailersBean> mVideoBeans;

    private OnItemClickListener mOnItemClickListener;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    public void setTrailerBeans(List<TrailerBean.TrailersBean> videoBeans) {
        this.mVideoBeans = videoBeans;
        notifyDataSetChanged();
    }

    public List<TrailerBean.TrailersBean> getVideoBeans() {
        return mVideoBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_mvp_trailer, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrailerBean.TrailersBean trailersBean = mVideoBeans.get(position);

        GlideUtil.loadImageRound(mContext, trailersBean.getCoverImg(), holder.mIcon,4);
        holder.mTitle.setText(trailersBean.getMovieName());
        holder.mSummary.setText(trailersBean.getType().toString());
        holder.mRating.setRating((float) (1 + Math.random() * (5 - 1 + 1)));
    }

    @Override
    public int getItemCount() {
        return mVideoBeans == null ? 0 : mVideoBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mIcon;
        private final AppCompatTextView mTitle;
        private final AppCompatTextView mSummary;
        private final AppCompatRatingBar mRating;

        private ViewHolder(final View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.item_video_icon);
            mTitle = itemView.findViewById(R.id.item_video_title);
            mSummary = itemView.findViewById(R.id.item_video_summary);
            mRating = itemView.findViewById(R.id.item_video_rating);

            itemView.setOnClickListener(v -> {
                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(getLayoutPosition());
                }
            });
        }
    }

    private OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * item点击事件 回调
         *
         * @param position 位置
         */
        void onItemClick(int position);
    }
}
