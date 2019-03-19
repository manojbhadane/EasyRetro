package com.manojbhadane.easyretrosample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.manojbhadane.easyretro.EasyRetro;
import com.manojbhadane.easyretro.ResponseListener;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI();
            }
        });
    }

    /**
     * Features :
     * 1. No need to check for internet connection each time
     * 2. Intelligent crash handling at the time of response parsing
     * 3. Easy to Use
     * 4. Minimal configuration
     *
     *
     */

    private void callAPI() {

        ApiService apiService = EasyRetro.setServices(ApiService.class);

        Call<ResModel> call = apiService.test("https://raw.githubusercontent.com/manojbhadane/Kotlin-LambdaExpression/master/sample.json");

        EasyRetro.request(call, new ResponseListener<ResModel>() {
            @Override
            public void onResponse(ResModel model, Response response) {
                Toast.makeText(MainActivity.this, model.getData().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg, Response response) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
