package activity.xz.com.lganhuo.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import activity.xz.com.lganhuo.ui.DetailActivity;
import activity.xz.com.lganhuo.ui.adapter.HomeRecyclerviewAdapter;
import activity.xz.com.lganhuo.ui.common.Constant;
import activity.xz.com.lganhuo.ui.model.GankModel;

/**
 * Created by Administrator on 2017/6/11.
 */

public class OtherCategroyFragent extends BaseHomeFragment {
    private static final String CATEGROY = "categroy";

    @Override
    protected String getApiCategory() {
        String categroy = getArguments().getString("categroy");
        return categroy;
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
                String iamges = "";
                if (entity.getImages() != null && entity.getImages().size() > 0) {
                    iamges = entity.getImages().get(0);
                }

                intent.putExtra("entity",entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {
                cloverClick(position, entity);
            }
        });
    }

    public static Fragment newInstance(String arg) {
        OtherCategroyFragent fragment = new OtherCategroyFragent();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGROY, arg);
        fragment.setArguments(bundle);
        return fragment;
    }
}
