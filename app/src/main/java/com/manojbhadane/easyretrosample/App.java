package com.manojbhadane.easyretrosample;

import android.app.Application;

import com.manojbhadane.easyretro.EasyRetro;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EasyRetro.init(getApplicationContext(),"https://reqres.in/api/users/");

    }
}
