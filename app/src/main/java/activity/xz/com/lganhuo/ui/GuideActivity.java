package activity.xz.com.lganhuo.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import activity.xz.com.lganhuo.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by Administrator on 2017/7/14.
 */

public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.image_guide)
     ImageView mIv;
    @BindView(R.id.tv_time)
     TextView mTvTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        Glide.with(this)
                .load(R.drawable.guide_bg)
                // 设置高斯模糊
                .bitmapTransform(new BlurTransformation(this, 25, 4))
                .into(mIv);
        new CountDownTimer(4000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                mTvTime.setText(millisUntilFinished / 1000 + " s");
            }

            @Override
            public void onFinish() {
                setupWindowAnimations();
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                GuideActivity.this.finish();
            }
        }.start();
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(5000);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setExitTransition(fade);
        }
    }
}
