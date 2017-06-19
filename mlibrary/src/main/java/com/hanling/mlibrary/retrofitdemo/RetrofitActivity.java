package com.hanling.mlibrary.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hanling.mlibrary.R;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TestURLService service = retrofit.create(TestURLService.class);
        Call<BookSearchResponse>  call = service.getSearchBooks("小王子", "", 0, 3);
        call.enqueue(new Callback<BookSearchResponse>() {
            @Override
            public void onResponse(Call<BookSearchResponse> call,        Response<BookSearchResponse> response) {
                BookSearchResponse body = response.body();
                Logger.d("success"+body);

            }
            @Override
            public void onFailure(Call<BookSearchResponse> call, Throwable t) {
                Logger.d("failuer"+t.getMessage());
            }
        });
    }

}
