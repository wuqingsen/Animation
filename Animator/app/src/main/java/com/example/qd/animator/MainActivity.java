package com.example.qd.animator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private NiceAnimator niceAnimator;
    private RelativeLayout rl_root;//根布局
    private Button button1,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_root = findViewById(R.id.rl_root);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        //创建对象并初始化动画
        niceAnimator = new NiceAnimator(MainActivity.this, rl_root);
        niceAnimator.initAnimator();

        //520动画
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                niceAnimator.startAnimator("520");
            }
        });

        //1314动画
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                niceAnimator.startAnimator("1314");
            }
        });
    }
}
