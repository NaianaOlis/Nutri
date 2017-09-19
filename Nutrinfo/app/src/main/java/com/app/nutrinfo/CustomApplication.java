package com.app.nutrinfo;

/**
 * Created by NaianaPC on 15/09/2017.
 */
import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}