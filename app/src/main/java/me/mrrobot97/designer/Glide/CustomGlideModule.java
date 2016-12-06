package me.mrrobot97.designer.Glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by mrrobot on 16/11/4.
 */

public class CustomGlideModule implements GlideModule {
    private int cacheSize = 1024 * 1024 * 200;  //200MB
    private int memoryCacheSize=1024*30;    //30MB

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSize))
                .setMemoryCache(new LruResourceCache(memoryCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
