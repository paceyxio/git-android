package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * Created by xiong on 2016/8/13.
 */
public class HomePage extends Activity
{
    private ImageButton startButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
//        //移除标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //移除通知栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        startButton = (ImageButton) findViewById(R.id.start_Button);

        //注册点击事件
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
