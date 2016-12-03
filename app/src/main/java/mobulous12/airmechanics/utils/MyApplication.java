package mobulous12.airmechanics.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.androidquery.callback.BitmapAjaxCallback;
import com.crashlytics.android.Crashlytics;

import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import mobulous12.airmechanics.fonts.FontCache;
import mobulous12.airmechanics.fonts.TypefaceUtil;


/**
 * Created by mobulous10 on 2/9/16.
 */
public class MyApplication extends MultiDexApplication
{
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        mInstance = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Raleway-Regular.ttf");
        FontCache.getInstance().addFont("edward", "fonts/EdwardianScriptITC.ttf");
        FontCache.getInstance().addFont("blackitalic", "fonts/Raleway-BlackItalic.ttf");
        FontCache.getInstance().addFont("bold", "fonts/Raleway-Bold_0.ttf");
        FontCache.getInstance().addFont("bolditalic", "fonts/Raleway-BoldItalic.ttf");
        FontCache.getInstance().addFont("extrabold", "fonts/Raleway-ExtraBold_0.ttf");
        FontCache.getInstance().addFont("extrabolditalic","fonts/Raleway-ExtraBoldItalic.ttf");
        FontCache.getInstance().addFont("extralight","fonts/Raleway-ExtraLight_0.ttf");
        FontCache.getInstance().addFont("heavy","fonts/Raleway-Heavy.ttf");
        FontCache.getInstance().addFont("italic", "fonts/Raleway-Italic.ttf");
        FontCache.getInstance().addFont("light", "fonts/Raleway-Light_0.ttf");
        FontCache.getInstance().addFont("lightitalic","fonts/Raleway-LightItalic.ttf");
        FontCache.getInstance().addFont("medium","fonts/Raleway-Medium_0.ttf");
        FontCache.getInstance().addFont("mediumitalic","fonts/Raleway-MediumItalic.ttf");
        FontCache.getInstance().addFont("regular", "fonts/Raleway-Regular.ttf");
        FontCache.getInstance().addFont("semibold", "fonts/Raleway-SemiBold_0.ttf");
        FontCache.getInstance().addFont("semibolditalic", "fonts/OpenSans-SemiboldItalic_0.ttf");
        FontCache.getInstance().addFont("thin", "fonts/Raleway-Thin_0.ttf");
        FontCache.getInstance().addFont("thinitalic", "fonts/Raleway-ThinItalic.ttf");
        FontCache.getInstance().addFont("roboto", "fonts/Roboto-Regular_0.ttf");
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        BitmapAjaxCallback.clearCache();
    }

}
