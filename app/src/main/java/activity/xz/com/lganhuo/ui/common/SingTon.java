package activity.xz.com.lganhuo.ui.common;

import android.widget.Switch;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/18.
 */

public class SingTon {
    int a = 2;
 private volatile static SingTon instance;
     private SingTon(){
     }
   public static SingTon getInstance(){
       if(instance == null){
           synchronized (SingTon.class){
               if (instance == null)
                   instance = new SingTon();
           }
       }
       return instance;
   }
}
