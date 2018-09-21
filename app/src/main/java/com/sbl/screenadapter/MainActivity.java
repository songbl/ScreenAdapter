package com.sbl.screenadapter;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    private float sNonCompatDensity;
    private float sNonCompatScaleDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加屏幕适配
        setCustomDensity(this, this.getApplication());

        setContentView(R.layout.activity_main);
        this.getResources().getDisplayMetrics();
    }


    private void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    sNonCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                }

                @Override
                public void onLowMemory() {

                }
            });

        }
        //屏幕宽 的像素除以360 获得Density的值
        final float targetDensity = appDisplayMetrics.widthPixels / 360;         //px=density/dp
        final float targetScaledDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (targetDensity * 160);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        //这里把scaledDensity的值 等于获得Density的值（会有bug 下面会解决）
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
