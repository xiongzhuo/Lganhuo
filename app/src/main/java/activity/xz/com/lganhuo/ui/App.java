package activity.xz.com.lganhuo.ui;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by Administrator on 2017/7/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //日志框架初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
        //换肤框架初始化
        SkinCompatManager.init(this).addInflater(new SkinMaterialViewInflater()).loadSkin();
        //工具类初始化
        Utils.init(this);
        //数据库初始化
        Realm.init(this);
    }
}
