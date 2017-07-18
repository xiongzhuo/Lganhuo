package activity.xz.com.lganhuo.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import activity.xz.com.lganhuo.R;
import activity.xz.com.lganhuo.ui.adapter.HistorySearchAdapter;
import activity.xz.com.lganhuo.ui.adapter.HomeRecyclerviewAdapter;
import activity.xz.com.lganhuo.ui.common.Constant;
import activity.xz.com.lganhuo.ui.common.IPreference;
import activity.xz.com.lganhuo.ui.common.PreferenceImpl;
import activity.xz.com.lganhuo.ui.model.GankModel;
import activity.xz.com.lganhuo.ui.net.Api;
import activity.xz.com.lganhuo.ui.net.HttpManager;
import activity.xz.com.lganhuo.ui.widget.EmptyRecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import skin.support.app.SkinCompatDelegate;
import skin.support.content.res.SkinCompatResources;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;
import skin.support.widget.SkinCompatThemeUtils;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;
import static skin.support.widget.SkinCompatHelper.checkResourceId;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SearchActivity extends AppCompatActivity implements SkinObserver{
    private FlexboxLayout mFlexboxLayout;
    private PreferenceImpl mPreUtils;
    private EmptyRecyclerView mRecyclerViewHistory;
    private final String HISTORY_SEARCH = "history_search";
    private HistorySearchAdapter mHistorySearchAdapter;
    private HomeRecyclerviewAdapter mHomeRecyclerviewAdapter;
    private List<String> mHotTitles = new ArrayList<>();
    private List<String> mHistoryTitles = new ArrayList<>();
    private ImageView mIvDeleteAll;
    private ImageView mIvBack;
    private TextView mTvHotSearch;
    private RelativeLayout mLayoutHistory;
    private int mPage = 1;
    private Disposable mDisposable;
    protected List<GankModel.ResultsEntity> mList = new ArrayList<>();

    private AVLoadingIndicatorView mAvi;
    private AVLoadingIndicatorView mAviLoadMore;
    private LinearLayout mLayoutLoadMore;
    private String mKeywords;//搜索关键字
    private EditText mEtSearch;
    private TextView mTvSearch;
    private SkinCompatDelegate mSkinDelegate;//换肤实现

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        updateStatusBarColor();
        updateWindowBackground();
        SwipeBackHelper.onCreate(this);
        initView();
        mPreUtils = (PreferenceImpl) IPreference.prefHolder.getPreference(this,HISTORY_SEARCH);
        if (mPreUtils.getAll(HISTORY_SEARCH) != null) {
            mHistoryTitles = mPreUtils.getAll(HISTORY_SEARCH);
        }
        mHistorySearchAdapter = new HistorySearchAdapter(this,mHistoryTitles);
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewHistory.setAdapter(mHistorySearchAdapter);
        mRecyclerViewHistory.setmEmptyView(new TextView(this));
        //监听上拉加载更多
        mRecyclerViewHistory.addOnScrollListener(new RecyclerViewScrollListener());
        stopLoading();
        mHistorySearchAdapter.setmOnMyClickListener(new HistorySearchAdapter.OnClickListener(){

            @Override
            public void onClick(int position) {
                mKeywords = mHistoryTitles.get(position);
                mEtSearch.setText(mKeywords);
                showSearchResult(true);
                startLoading();
                getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        mHomeRecyclerviewAdapter = new HomeRecyclerviewAdapter(this, mList, Constant.ITEM_TYPE_TEXT);
        mHomeRecyclerviewAdapter.addOnClickListener(new HomeRecyclerviewAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity entity) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("entity",entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {
                cloverClick(position, entity);
            }
        });

        mIvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreUtils.remove(HISTORY_SEARCH);
                mHistoryTitles.clear();
                mHistorySearchAdapter.setmListData(mPreUtils.getAll(HISTORY_SEARCH));
                mHistorySearchAdapter.notifyDataSetChanged();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywords = mEtSearch.getText().toString().trim();
                if (StringUtils.isEmpty(mKeywords)) {
                    ToastUtils.showShortSafe("请输入搜索条件");
                    return;
                }
                mHistoryTitles.add(mKeywords);
                mPreUtils.putAll(HISTORY_SEARCH, mHistoryTitles);
                showSearchResult(true);
                startLoading();
                getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        initTag();
    }

    private void initTag() {
        mHotTitles.add("RxJava");
        mHotTitles.add("RxAndroid");
        mHotTitles.add("数据库");
        mHotTitles.add("自定义控件");
        mHotTitles.add("下拉刷新");
        mHotTitles.add("mvp");
        mHotTitles.add("直播");
        mHotTitles.add("权限管理");
        mHotTitles.add("Retrofit");
        mHotTitles.add("OkHttp");
        mHotTitles.add("WebWiew");
        mHotTitles.add("热修复");
        // 通过代码向FlexboxLayout添加View
        for (int i = 0; i < mHotTitles.size(); i++) {
            TextView textView = new TextView(this);
            textView.setBackground(ContextCompat.getDrawable(SearchActivity.this,R.drawable.flexbox_text_bg));
            textView.setText(mHotTitles.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(30, 30, 30, 30);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.flexbox_text_color));
            mFlexboxLayout.addView(textView);
            //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
            ViewGroup.LayoutParams params = textView.getLayoutParams();
            if (params instanceof FlexboxLayout.LayoutParams) {
                FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
                //layoutParams.setFlexBasisPercent(0.5f);
                layoutParams.setMargins(10, 10, 20, 10);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    mHistoryTitles.add(tv.getText().toString().trim());
                    mPreUtils.putAll(HISTORY_SEARCH, mHistoryTitles);
                    //得到搜索条件，首先屏蔽掉历史搜索和热门搜索
                    showSearchResult(true);
                    //发起服务请求
                    mKeywords = tv.getText().toString().trim();
                    startLoading();
                    mEtSearch.setText(mKeywords);
                    getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
                }
            });
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        mRecyclerViewHistory = (EmptyRecyclerView) findViewById(R.id.recyclerview_history);
        mIvDeleteAll = (ImageView) findViewById(R.id.iv_deleteall);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvHotSearch = (TextView) findViewById(R.id.tv_hotsearch);
        mLayoutHistory = (RelativeLayout) findViewById(R.id.layout_history);
        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mAviLoadMore = (AVLoadingIndicatorView) findViewById(R.id.avi_loadmore);
        mLayoutLoadMore = (LinearLayout) findViewById(R.id.layout_loadmore);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    /**
     * 默认点击缩率图跳转到图片展示界面,子类可以选择调用
     */
    protected void cloverClick(int position, GankModel.ResultsEntity entity) {
        Intent intent = new Intent(this, PicActivity.class);
        ArrayList mListPicUrls = new ArrayList<>();
        mListPicUrls = (ArrayList) entity.getImages();
        intent.putStringArrayListExtra("piclist", mListPicUrls);
        startActivity(intent);
    }
    @NonNull
    public SkinCompatDelegate getSkinDelegate() {
        if (mSkinDelegate == null) {
            mSkinDelegate = SkinCompatDelegate.create(this);
        }
        return mSkinDelegate;
    }
    /**
     * @return true: 打开5.0以上状态栏换肤, false: 关闭5.0以上状态栏换肤;
     */
    protected boolean skinStatusBarColorEnable() {
        return true;
    }

    protected void updateStatusBarColor() {
        if (skinStatusBarColorEnable() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarColorResId = SkinCompatThemeUtils.getStatusBarColorResId(this);
            int colorPrimaryDarkResId = SkinCompatThemeUtils.getColorPrimaryDarkResId(this);
            if (checkResourceId(statusBarColorResId) != INVALID_ID) {
                getWindow().setStatusBarColor(SkinCompatResources.getInstance().getColor(statusBarColorResId));
            } else if (checkResourceId(colorPrimaryDarkResId) != INVALID_ID) {
                getWindow().setStatusBarColor(SkinCompatResources.getInstance().getColor(colorPrimaryDarkResId));
            }
        }
    }
    /**
     * 开启加载中动画
     */
    public void startLoading() {
        mAvi.setVisibility(View.VISIBLE);
        mRecyclerViewHistory.setVisibility(View.GONE);
        mAvi.smoothToShow();
    }
    /**
     * 开启加载更多动画
     */
    public void startLoadingMore() {
        stopLoading();
        mLayoutLoadMore.setVisibility(View.VISIBLE);
        mAviLoadMore.smoothToShow();
    }
    /**
     * 关闭加载中动画
     */
    public void stopLoading() {
        mAvi.setVisibility(View.GONE);
        mRecyclerViewHistory.setVisibility(View.VISIBLE);
        mAvi.smoothToHide();
    }
    /**
     * 是否显示搜索结果,显示时需要隐藏掉热门搜索和历史搜索条件
     *
     * @param flag
     */
    public void showSearchResult(boolean flag) {
        if (flag) {
            mTvHotSearch.setVisibility(View.GONE);
            mLayoutHistory.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        } else {
            mTvHotSearch.setVisibility(View.VISIBLE);
            mLayoutHistory.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 关闭加载更多动画
     */
    public void stopLoadingMore() {
        mLayoutLoadMore.setVisibility(View.GONE);
        mAviLoadMore.smoothToHide();
    }
    /**
     * 发起搜索请求
     */
    private void getDataFromServer(String searchkey, final int type) {
        Api api = HttpManager.getInstance().getApiService();
        api.getSearchData(searchkey, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(GankModel value) {
                        Logger.i("next----------" + value);
                        if (value.getError()) {
                            //服务端请求数据发生错误
                            ToastUtils.showShortSafe("服务端异常，请稍后再试");
                            return;
                        }
                        //更新界面数据
                        if (Constant.GET_DATA_TYPE_NOMAL == type) {
                            //正常模式下，清空之前数据，重新加载
                            mList.clear();
                            mList = value.getResults();
                        } else {
                            //加载更多模式
                            mList.addAll(value.getResults());
                        }
                        if (mRecyclerViewHistory.getAdapter() instanceof HistorySearchAdapter) {
                            mRecyclerViewHistory.setAdapter(mHomeRecyclerviewAdapter);
                        }
                        mHomeRecyclerviewAdapter.setmListData(mList);
                        mHomeRecyclerviewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopLoading();
                        stopLoadingMore();
                    }

                    @Override
                    public void onComplete() {
                        stopLoading();
                        stopLoadingMore();
                    }
                });
    }

    protected void updateWindowBackground() {
        int windowBackgroundResId = SkinCompatThemeUtils.getWindowBackgroundResId(this);
        if (checkResourceId(windowBackgroundResId) != INVALID_ID) {
            String typeName = getResources().getResourceTypeName(windowBackgroundResId);
            if ("color".equals(typeName)) {
                Drawable drawable = new ColorDrawable(SkinCompatResources.getInstance().getColor(windowBackgroundResId));
                getWindow().setBackgroundDrawable(drawable);
            } else if ("drawable".equals(typeName)) {
                Drawable drawable = SkinCompatResources.getInstance().getDrawable(windowBackgroundResId);
                getWindow().setBackgroundDrawable(drawable);
            } else if ("mipmap".equals(typeName)) {
                Drawable drawable = SkinCompatResources.getInstance().getMipmap(windowBackgroundResId);
                getWindow().setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {
        updateStatusBarColor();
        updateWindowBackground();
        getSkinDelegate().applySkin();
    }

    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(mList.isEmpty()||mRecyclerViewHistory.getAdapter() instanceof HistorySearchAdapter)
                return;
            //当前RecyclerView显示出来的最后一个的item的position,默认为-1
            int lastPosition = -1;
//当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //分别判断三种类型
                if (layoutManager instanceof GridLayoutManager) {
                    //通过LayoutManager找到当前显示的最后的item的position
                    lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);
                }
                // 判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    //此时需要请求等过数据，显示加载更多界面
                    mPage++;
                    startLoadingMore();
                    getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
