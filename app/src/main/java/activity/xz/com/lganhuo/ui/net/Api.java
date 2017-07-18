package activity.xz.com.lganhuo.ui.net;


import activity.xz.com.lganhuo.ui.model.GankModel;
import activity.xz.com.lganhuo.ui.model.TianModel;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
/**
 * Created by Administrator on 2017/7/18.
 */

public interface Api {
    /**
     * 获取分类数据
     * 示例：http://gank.io/api/data/Android/10/1
     *
     * @param category 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param pageSize 请求个数： 数字，大于0
     * @param page     第几页：数字，大于0
     * @return
     */
    @GET("data/{category}/{pageSize}/{page}")
    Observable<GankModel> getCategoryData(@Path("category") String category,
                                                       @Path("pageSize") int pageSize,
                                                       @Path("page") int page);

    @GET("search/query/{searchkey}/category/all/count/10/page/{page}")
    Observable<GankModel> getSearchData(@Path("searchkey") String searchkey,
                                        @Path("page") int page);

    @GET("mobile/")
    Observable<TianModel> getReadData(@Query("key") String key,
                                      @Query("num") int num,
                                      @Query("page") int page);

}
