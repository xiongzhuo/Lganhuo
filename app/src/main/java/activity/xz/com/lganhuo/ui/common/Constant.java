package activity.xz.com.lganhuo.ui.common;

/**
 * Created by Administrator on 2017/7/14.
 */

public interface Constant {
    String BASE_URL = "http://gank.io/api/";
    String BASE_URL_READ = "http://api.tianapi.com/";
    String APIKEY = "692622924e6ae9d170868b43dbb91956";
    String CATEGORY_EXPANDRESOURCE = "拓展资源";
    String CATEGORY_APP = "App";
    String CATEGORY_VIDEO = "休息视频";
    String CATEGROY_RECOMMEND = "瞎推荐";
    String CATEGORY_CLIENT = "前端";
    String CATEGORY_ALL = "all";
    //列表条目布局类型 0：文字布局（Android ,iOS Fragement）  1：图片布局（福利 Fragement）
    int ITEM_TYPE_TEXT = 0;
    int ITEM_TYPE_IMAGE = 1;
    //每次请求大小
    int PAGE_SIZE = 10;
    //更新数据类型 0:正常加载、下拉刷新   1: 加载更多
    int GET_DATA_TYPE_NOMAL = 0;
    int GET_DATA_TYPE_LOADMORE = 1;
    //个人信息设置
    String NICKNAME = "leonHua";
    String BLOGURL = "https://leonhua.github.io/";
    String OTHERURL = "https://github.com/leonHua";
}
