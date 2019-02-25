package com.manojbhadane.easyretro;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static String BASE_URL;
    private static Retrofit retrofit;

    public static void init(String baseURL) {
        BASE_URL = baseURL;
        getInstance();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
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

}
