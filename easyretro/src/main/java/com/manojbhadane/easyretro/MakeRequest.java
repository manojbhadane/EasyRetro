package com.manojbhadane.easyretro;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unchecked")
public class MakeRequest<T> {

    private static MakeRequest mInstance;
    private final String mErrorWentWrong = "Something went wrong";

    private MakeRequest() {
    }

    public static synchronized MakeRequest getInstance() {
        if (mInstance == null)
            mInstance = new MakeRequest();
        return mInstance;
    }

    public static void printLog(String log) {
        if (BuildConfig.DEBUG)
            Log.e("--", log);
    }

    /**
     * Returns model as response
     *
     * @param call
     * @param isInternet
     * @param listener
     * @param <T>
     */
    public <T> void request(Call call, boolean isInternet, final ResponseListener<T> listener) {
        try {
            if (isInternet) {
                printLog("Request Url : " + call.request().url());
                //noinspection unchecked
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {
                        try {
                            printLog("onResponse : isSuccessful : " + response.isSuccessful());
                            if (response.isSuccessful()) {
                                T model = (T) response.body();
                                listener.onResponse(model, response);
                            } else {
                                listener.onError(mErrorWentWrong, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(mErrorWentWrong, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable t) {
                        try {
                            printLog("onFailure : Message : " + t.getMessage());
                            printLog("onFailure : Cause : " + t.getCause());
                            printLog("onFailure : StackTrace : " + t.getStackTrace());
                            listener.onError(mErrorWentWrong, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(mErrorWentWrong, null);
                        }
                    }
                });
            } else {
                listener.onError("No internet connection", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(mErrorWentWrong, null);
        }
    }

    /**
     * Returns String as response, later cast it to String
     *
     * @param call     call
     * @param listener result callback
     */
    public void requestStringAsResponse(Call call, boolean isInternet, final ResponseListener listener) {

        try {
            if (isInternet) {
                printLog("Request Url : " + call.request().url());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            printLog("onResponse : isSuccessful : " + response.isSuccessful());
                            if (response.isSuccessful()) {
                                listener.onResponse(new JSONObject(response.body().string()), response);
                            } else {
                                listener.onError(mErrorWentWrong, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(mErrorWentWrong, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            printLog("onFailure : Message : " + t.getMessage());
                            printLog("onFailure : Cause : " + t.getCause());
                            printLog("onFailure : StackTrace : " + t.getStackTrace());
                            listener.onError(mErrorWentWrong, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(mErrorWentWrong, null);
                        }
                    }
                });
            } else {
                listener.onError("No internet connection", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(mErrorWentWrong, null);
        }
    }
}
