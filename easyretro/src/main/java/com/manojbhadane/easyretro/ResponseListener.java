package com.manojbhadane.easyretro;

import retrofit2.Response;

public interface ResponseListener<D> {
    public void onResponse(D model, Response response);

    public void onError(String msg,Response response);

//    public void showHideProgress(boolean shouldShow);
}
