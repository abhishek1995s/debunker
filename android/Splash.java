package com.hackthefake2.oui.hackthefake2;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    View decorView;
    int uiOptions;
    ImageView splbg;
    int t=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
             decorView= getWindow().getDecorView();
            uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
           android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }


        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        splbg=(ImageView)findViewById(R.id.imageView);
        if (android.os.Build.VERSION.SDK_INT >= 24 || android.os.Build.VERSION.SDK_INT < 19 ) {
            // only for gingerbread and newer versions
            splbg.setBackgroundResource(R.drawable.news_title);
            t=2000;
        }
        else{
            try
            {


            t=4000;
        splbg.setBackgroundResource(R.drawable.movie);
        AnimationDrawable anim = (AnimationDrawable) splbg.getBackground();
        anim.start();
            }

            catch(OutOfMemoryError e)
            {
                splbg.setBackgroundResource(R.drawable.news_title);
                t=2000;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                { @Override
                public void run()
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                },t) ;
                t=4000;
            }
            catch(Exception e){
                splbg.setBackgroundResource(R.drawable.news_title);
                t=2000;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                { @Override
                public void run()
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                },t) ;
                t=4000;
            }
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        { @Override
        public void run()
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        },t) ;
        t=4000;
    }
}
