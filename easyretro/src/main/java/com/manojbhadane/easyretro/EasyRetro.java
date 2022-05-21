package com.manojbhadane.easyretro;

import android.content.Context;

import retrofit2.Call;

//Version Commit guide on Jitpack , execute following commands
//
//git tag -a v1.0 -m "first commit"
//git push origin v1.2

//Visit https://jitpack.io to build library

public class EasyRetro {

    public static void init(Context context, String baseUrl) {
        RetrofitInstance.initCache(context);
        RetrofitInstance.init(baseUrl);
    }

    public static <T> T setServices(final Class<T> service) {
        return RetrofitInstance.getInstance().create(service);
    }

    @SuppressWarnings("unchecked")
    public static void request(Call call, ResponseListener listener) {
        MakeRequest.getInstance().request(call, true, listener);
    }

    public static void requestStringAsResponse(Call call, ResponseListener listener) {
        MakeRequest.getInstance().requestStringAsResponse(call, true, listener);
    }
}
