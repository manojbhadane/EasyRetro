package com.manojbhadane.easyretrosample;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    public Call<ResModel> test(@Url String url);

    @GET
    public Call<ResponseBody> test1();

}
