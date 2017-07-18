package activity.xz.com.lganhuo.ui.net;

import java.util.concurrent.TimeUnit;

import activity.xz.com.lganhuo.ui.common.Constant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/18.
 */

public class HttpManager {
    private volatile static HttpManager httpManager;
    private HttpManager(){
    }
  public static HttpManager getInstance(){
      if(httpManager == null){
          synchronized (HttpManager.class){
              if (httpManager == null)
                  httpManager = new HttpManager();
          }
      }
      return httpManager;
  }
   public Api getApiService(){
       OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
       builder.readTimeout(15, TimeUnit.SECONDS);
       builder.connectTimeout(10, TimeUnit.SECONDS);

       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(Constant.BASE_URL)
               .client(builder.build())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(GsonConverterFactory.create()).build();
       Api api = retrofit.create(Api.class);
       return api;
   }

    /**
     * 获取对应的接口服务
     *
     * @return Api
     */
    public Api getApiService(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        return api;
    }
}
