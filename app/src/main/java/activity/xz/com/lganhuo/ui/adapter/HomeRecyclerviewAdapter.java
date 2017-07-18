package activity.xz.com.lganhuo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import activity.xz.com.lganhuo.R;
import activity.xz.com.lganhuo.ui.common.Constant;
import activity.xz.com.lganhuo.ui.common.Utils;
import activity.xz.com.lganhuo.ui.image.ImageManager;
import activity.xz.com.lganhuo.ui.model.GankModel;

/**
 * Created by Administrator on 2017/7/17.
 */
public class HomeRecyclerviewAdapter extends RecyclerView.Adapter<HomeRecyclerviewAdapter.ViewHolder> {
    private Context mContext;
    private List<GankModel.ResultsEntity> mListData;
    private int mItemType;//条目布局类型
    private boolean mIsStaggered = false;
    private OnBaseClickListener mBaseClickListener;
    private List<Integer> mHeights = new ArrayList<>();

    public interface OnBaseClickListener {
        void onClick(int position, GankModel.ResultsEntity entity);

        void onCoverClick(int position, GankModel.ResultsEntity entity);
    }

    public HomeRecyclerviewAdapter(Context mContext, List<GankModel.ResultsEntity> mListData, int mItemType) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.mItemType = mItemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment_girl, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GankModel.ResultsEntity resultsEntity = mListData.get(position);
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            holder.tvTitle.setText(resultsEntity.getDesc());
            holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Utils.formatDateFromStr(resultsEntity.getPublishedAt())));
            holder.tvAuthor.setText(resultsEntity.getWho());
            if (resultsEntity.getImages() != null && resultsEntity.getImages().size() > 0) {
                ImageManager.getInstance().loadImage(mContext, resultsEntity.getImages().get(0), holder.ivCover);
            } else {
                ImageManager.getInstance().loadImage(mContext, R.drawable.notfound, holder.ivCover);
            }
            holder.ivCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resultsEntity.getImages() != null && resultsEntity.getImages().size() > 0) {
                        mBaseClickListener.onCoverClick(position, resultsEntity);
                    } else {
                        ToastUtils.showShortSafe("木有发现图片哟");
                    }
                }
            });
        } else {
            //福利界面
            if (mIsStaggered) {
                //瀑布流模式
                //随机高度，为了达到瀑布流效果
                if (mHeights.size() <= position) {
                    mHeights.add((int) (Math.random() * 300 + 500));
                }
                //设置每个条目的高度，高度是随机的
                ViewGroup.LayoutParams lp = holder.cardView.getLayoutParams();
                lp.height = mHeights.get(position);
                holder.cardView.setLayoutParams(lp);
            }
            ImageManager.getInstance().loadImage(mContext, resultsEntity.getUrl(), holder.ivGirl);

        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseClickListener.onClick(position, resultsEntity);
            }
        });

    }

    public void addOnClickListener(OnBaseClickListener baseClickListener) {
        this.mBaseClickListener = baseClickListener;
    }

    public void setmListData(List<GankModel.ResultsEntity> mListData) {
        this.mListData = mListData;
    }

    public void setmIsStaggered(boolean mIsStaggered) {
        this.mIsStaggered = mIsStaggered;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvTime;
        ImageView ivCover;//封面缩率图
        ImageView ivGirl;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            ivGirl = (ImageView) itemView.findViewById(R.id.iv_girl);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
