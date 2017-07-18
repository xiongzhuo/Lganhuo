package activity.xz.com.lganhuo.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import activity.xz.com.lganhuo.R;
import activity.xz.com.lganhuo.ui.image.ImageManager;
import activity.xz.com.lganhuo.ui.image.PinchImageView;

/**
 * Created by Administrator on 2017/7/18.
 */

public class PicAdapter extends PagerAdapter{
    private Context mContext;
    private List<String> mListUrl;

    public PicAdapter(List<String> mListUrl, Context mContext) {
        this.mListUrl = mListUrl;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mListUrl.isEmpty()){
            return 0;
        }else{
            return mListUrl.size();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //ImageView imageView = new ImageView(mContext);
        PinchImageView imageView = new PinchImageView(mContext);
        imageView.setImageResource(R.drawable.header);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (mListUrl == null || mListUrl.size() < 1) {
            ImageManager.getInstance().loadImage(mContext, R.drawable.placeholder, imageView);
        } else {
            ImageManager.getInstance().loadImage(mContext, mListUrl.get(position), imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setmListUrl(List<String> mListUrl) {
        this.mListUrl = mListUrl;
    }
}
