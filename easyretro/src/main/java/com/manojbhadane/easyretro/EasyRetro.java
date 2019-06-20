package com.manojbhadane.easyretro;

import android.content.Context;

import retrofit2.Call;

//Version Commit guide on Jitpack , execute follwing commands
//
//git tag -a v1.0 -m "first commit"
//git push origin v1.2

public class EasyRetro {

    private static Context mContext;

    public static void init(Context context, String baseUrl) {
        mContext = context;
        RetrofitInstance.init(baseUrl);
    }

    public static Context getContext(){
        return mContext;
    }

    public static <T> T setServices(final Class<T> service) {
        return RetrofitInstance.getInstance().create(service);
    }

    public static void request(Call call, ResponseListener listener) {
        if (NetworkManager.getInstance().isConnectingToInternet(mContext)) {
            MakeRequest.getInstance().request(call, true, listener);
        } else {
            MakeRequest.getInstance().request(call, false, listener);
        }

    }

    public static void requestStringAsResponse(Call call, ResponseListener listener) {
        if (NetworkManager.getInstance().isConnectingToInternet(mContext)) {
            MakeRequest.getInstance().requestStringAsResponse(call, true, listener);
        } else {
            MakeRequest.getInstance().requestStringAsResponse(call, false, listener);
        }

    }


//    public static void request(Call call, final Class<?> responseModel, final ResponseListener listener) {
//        MakeRequest.getInstance().request(call, responseModel, listener);
//    }

//    public static void request(Call call, final ResponseListener listener) {
//        MakeRequest.getInstance().request(call, listener);
//    }
}
