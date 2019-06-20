package com.manojbhadane.easyretro;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static String BASE_URL;
    private static Retrofit retrofit;

    private static int cacheSize = 10 * 1024 * 1024; // 10 MB

    public static void init(String baseURL) {
        BASE_URL = baseURL;
        getInstance();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {

            Cache cache = new Cache(EasyRetro.getContext().getCacheDir(), cacheSize);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(offlineInterceptor)
                    .addNetworkInterceptor(onlineInterceptor)
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

//    public static ApiService getService() {
//        return getInstance().create(ApiService.class);
//    }

    public static <T> T getService(final Class<T> service) {
        return getInstance().create(service);
    }

    static Interceptor onlineInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());
            int maxAge = 60*5; // read from cache for 300(5 min.) seconds even if there is internet connection
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    static Interceptor offlineInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (NetworkManager.getInstance().isConnectingToInternet(EasyRetro.getContext())) {
                int maxStale = 60 * 60 * 24 * 1; // Offline cache available for 1 days
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return chain.proceed(request);
        }
    };


}
