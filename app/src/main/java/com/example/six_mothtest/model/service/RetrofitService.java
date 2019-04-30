package com.example.six_mothtest.model.service;

import com.example.six_mothtest.model.bean.GuShiBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 13:54
 * Describe: ${as}
 */
public interface RetrofitService {

    @FormUrlEncoded
    @POST
    Call<GuShiBean> getGuShi(@Url String url, @Field("page")String page, @Field("count")String count);


    @Multipart
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0", "Connection: keep-alive"})
    @POST("upload")
    Call<ResponseBody> getAvater(@Part("format") RequestBody format, @Part MultipartBody.Part smfile);

}
