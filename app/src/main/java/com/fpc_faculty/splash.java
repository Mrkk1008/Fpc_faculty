package com.fpc_faculty;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class splash extends AppCompatActivity {
    ImageView sp1;
    Animation topAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.animation);
        sp1 = findViewById(R.id.sp);
        sp1.setAnimation(topAnim);
        new CountDownTimer(3000,1000) {
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }


        }.start();
    }
}
