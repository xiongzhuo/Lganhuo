package activity.xz.com.lganhuo.ui.Fragment;

import android.content.Intent;
import android.view.View;

import activity.xz.com.lganhuo.ui.DetailActivity;
import activity.xz.com.lganhuo.ui.adapter.HomeRecyclerviewAdapter;
import activity.xz.com.lganhuo.ui.common.Constant;
import activity.xz.com.lganhuo.ui.model.GankModel;

/**
 * 作者：Leon
 * 时间：2017/6/6
 */
public class HomeIOSFragment extends BaseHomeFragment {
    public View mView;

    @Override
    public String getApiCategory() {
        return "iOS";
    }

    @Override
    protected int getItemType() {
        return Constant.ITEM_TYPE_TEXT;
    }

    @Override
    protected void initListener(HomeRecyclerviewAdapter mHomeRecyclerviewAdapter) {
        mHomeRecyclerviewAdapter.addOnClickListener(new HomeRecyclerviewAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity entity) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {
                cloverClick(position, entity);
            }
        });
    }
}
