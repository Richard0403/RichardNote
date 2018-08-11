package me.richard.note.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import me.richard.note.R;
import me.richard.note.activity.base.CommonActivity;
import me.richard.note.databinding.ActivitySplashBinding;
import me.richard.note.intro.IntroActivity;
import me.richard.note.util.LogUtils;

public class SplashActivity extends CommonActivity<ActivitySplashBinding> {
    CountDownTimer timer = new CountDownTimer(1*1000+50, 500) {
        @Override
        public void onTick(long millisUntilFinished) {
            LogUtils.i("=="+millisUntilFinished);
        }
        @Override
        public void onFinish() {
            startActivity(MainActivity.class);
            finish();
        }
    };


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }



    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        if(!IntroActivity.launchIfNecessary(this)){
            timer.start();
        }else{
            finish();
        }
    }

}
