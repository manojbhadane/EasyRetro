package com.manojbhadane.easyretro;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static String BASE_URL;
    private static Retrofit retrofit;

    private static Cache cache;

    public static void initCache(Context context){
        // 10 MB
        int cacheSize = 10 * 1024 * 1024;
        cache = new Cache(context.getCacheDir(), cacheSize);
    }
    public static void init(String baseURL) {
        BASE_URL = baseURL;
        getInstance();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(offlineInterceptor())
                    .addNetworkInterceptor(onlineInterceptor())
                    .cache(cache)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static <T> T getService(final Class<T> service) {
        return getInstance().create(service);
    }

    private static Interceptor onlineInterceptor() {
        return chain -> {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);
            String cacheControl = originalResponse.header("Cache-Control");

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {

                CacheControl cc = new CacheControl.Builder()
                        .maxStale(1, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cc)
                        .build();

                return chain.proceed(request);

            } else {
                return originalResponse;
            }
        };
    }

    private static Interceptor offlineInterceptor() {
        return chain -> {
            try {
                return chain.proceed(chain.request());
            } catch (Exception e) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(1, TimeUnit.DAYS)
                        .build();

                Request offlineRequest = chain.request().newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                return chain.proceed(offlineRequest);
            }
        };
    }
}
