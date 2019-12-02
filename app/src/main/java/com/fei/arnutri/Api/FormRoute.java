package com.fei.arnutri.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FormRoute {


    @FormUrlEncoded
    @POST("/diagnostic/anthropometric")
    Call<ResponseBody> sendAntropometric(
            @Field("age") String age,
            @Field("weight") String wight,
            @Field("height") String height,
            @Field("armCircunference") String armCircunference,
            @Field("waistCircunference") String waistCircunference,
            @Field("sagittalAbdominalDiameter") String sagittalAbdominalDiameter);

}
