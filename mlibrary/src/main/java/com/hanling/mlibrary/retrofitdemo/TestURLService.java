package com.hanling.mlibrary.retrofitdemo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by XueYang on 2017/5/2.
 */

public interface TestURLService {
//    @GET("/ZJWAPPproject/Android/SelGZZAndMT.action")
//    Call<ResponseBody> getGZZ(@Query("id") String id);

    @GET("book/search")
    Call<BookSearchResponse> getSearchBooks(@Query("q") String name,
                                            @Query("tag") String tag, @Query("start") int start,
                                            @Query("count") int count);
}
