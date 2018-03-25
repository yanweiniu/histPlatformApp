package com.marchsoft.organization;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.marchsoft.organization.db.DatabaseManager;
import com.marchsoft.organization.db.Preferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class app extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_photo_default_loading)
                .showImageOnFail(R.mipmap.ic_photo_default_fail)
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new
                ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        DatabaseManager.initialize(this);
        Preferences.init(this);
        Log.i("==========","初始化成功");
    }
}
