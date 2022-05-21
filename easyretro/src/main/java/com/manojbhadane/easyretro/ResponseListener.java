package com.manojbhadane.easyretro;

import retrofit2.Response;

public interface ResponseListener<D> {
    void onResponse(D model, Response response);

    void onError(String msg, Response response);

//    public void showHideProgress(boolean shouldShow);
}
