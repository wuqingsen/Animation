package com.example.qd.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.util.ArrayList;
import java.util.List;

public class NiceAnimator {
    private Activity activity;
    private RelativeLayout rlRoot;
    private ArrayList<String> stringList;
    private List<String> judgeType;//判断520、1314
    private SVGAImageView svgaImage;
    private SVGAParser parser;
    private View animatorView;
    private RelativeLayout rl_520, rl_1314;
    private AnimatorSet animatorSet = new AnimatorSet();
    private PropertyValuesHolder animatorX;
    private PropertyValuesHolder animatorY;

    public NiceAnimator(Activity activity, RelativeLayout rlRoot) {
        this.activity = activity;
        this.rlRoot = rlRoot;
    }

    //初始化数据
    public void initAnimator() {
        animatorView = LayoutInflater.from(activity).inflate(R.layout.pop_animator, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //设置底部
        params.gravity = Gravity.CENTER;
        rlRoot.addView(animatorView, params);
        svgaImage = animatorView.findViewById(R.id.svgaImage);
        parser = new SVGAParser(activity);
        rl_520 = animatorView.findViewById(R.id.rl_520);
        rl_1314 = animatorView.findViewById(R.id.rl_1314);
        stringList = new ArrayList<>();
        judgeType = new ArrayList<>();
        //监听大动画的控件周期
        svgaImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                //执行完成隐藏布局
                if (judgeType.get(0).equals("520")) {
                    for (int i = 0; i < rl_520.getChildCount(); i++) {
                        setZoomGone(rl_520.getChildAt(i));
                    }
                } else {
                    for (int i = 0; i < rl_1314.getChildCount(); i++) {
                        setZoomGone(rl_1314.getChildAt(i));
                    }
                }
                Log.e("setCallback", "onFinished: stringList.size()=" + stringList.size());
                //当大动画结束，如果数组容器大于0，则移除容器第一位的数据。
                if (stringList != null && stringList.size() > 0) {
                    stringList.remove(0);
                    judgeType.remove(0);
                    //如果移除之后的容器大于0，则开始展示新一个的大动画
                    if (stringList != null && stringList.size() > 0) {
                        try {
                            parseSVGA();//解析加载动画
                        } catch (Exception e) {

                        }
                    } else {
                        stopSVGAAnima();
                    }
                } else {
                    stopSVGAAnima();
                }
            }

            @Override
            public void onRepeat() {
                stopSVGAAnima();
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    public void startAnimator(String type) {
        judgeType.add(type);
        if (type.equals("520")) {
            stringList.add(stringList.size(), "aixing520.svga");
        } else {
            stringList.add(stringList.size(), "aixing1314.svga");
        }
        //如果礼物容器列表的数量是1，则解析动画，如果数量不是1，则此处不解析动画，在上一个礼物解析完成之后加载再动画
        if (stringList.size() == 1) {
            parseSVGA();
        }
    }

    //停止动画
    private void stopSVGAAnima() {
        if (svgaImage.isAnimating() && stringList.size() == 0) {
            svgaImage.stopAnimation();
        }
    }

    //解析加载动画
    private void parseSVGA() {
        if (stringList.size() > 0) {
            try {
                parser.parse(stringList.get(0), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                        svgaImage.setImageDrawable(drawable);
                        svgaImage.startAnimation();
                        if (judgeType.get(0) != null && judgeType.get(0).equals("520")) {
                            set520Visible();
                        } else {
                            set1314Visible();
                        }
                    }

                    @Override
                    public void onError() {
                        if (stringList.size() > 0) {//如果动画数组列表大于0
                            stringList.remove(0);//移除第一位的动画
                            judgeType.remove(0);
                            parseSVGA();//继续循环解析
                        } else {
                            stopSVGAAnima();
                        }
                    }
                });
            } catch (Exception e) {
            }
        } else {
            stopSVGAAnima();
        }
    }

    private void set1314Visible() {
        for (int i = 0; i < rl_1314.getChildCount(); i++) {
            setZoomVisible(rl_1314.getChildAt(i));
        }
        rl_1314.setVisibility(View.VISIBLE);
    }

    private void set520Visible() {
        for (int i = 0; i < rl_520.getChildCount(); i++) {
            setZoomVisible(rl_520.getChildAt(i));
        }
        rl_520.setVisibility(View.VISIBLE);
    }

    //属性动画开始
    private void setZoomVisible(View view) {
        /**
         * 五个参数：
         * 1.要放大/缩小的控件
         * 2.方向：scaleX为沿X轴缩放，scaleY为沿Y轴缩放
         * 3.开始的大小：1.0f为原图开始
         * 4.放大/缩小的倍数：1.5f为放大1.5倍
         * 5.结束的大小：1.0f为原图结束
         */
        animatorX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1.0f);
        animatorY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1.0f);
        ObjectAnimator.ofPropertyValuesHolder(view, animatorX, animatorY).start();
    }

    //属性动画结束
    private void setZoomGone(View view) {
        /**
         * 五个参数：
         * 1.要放大/缩小的控件
         * 2.方向：scaleX为沿X轴缩放，scaleY为沿Y轴缩放
         * 3.开始的大小：1.0f为原图开始
         * 4.放大/缩小的倍数：1.5f为放大1.5倍
         * 5.结束的大小：1.0f为原图结束
         */
        animatorX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0f);
        animatorY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0f);
        ObjectAnimator.ofPropertyValuesHolder(view, animatorX, animatorY).start();
    }
}
