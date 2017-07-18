package activity.xz.com.lganhuo.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MainAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private List<Fragment> mList;

    public MainAdapter(FragmentManager fm, Context mContext, List<Fragment> mList) {
        super(fm);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        if(mList.isEmpty()){
           return 0;
        }else {
            return mList.size();
        }
    }

    public void setmList(List<Fragment> mList) {
        this.mList = mList;
    }
}
