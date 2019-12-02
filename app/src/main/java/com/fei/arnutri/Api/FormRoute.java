package com.fei.arnutri.Api;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FormRoute {


    @FormUrlEncoded
    @POST("/diagnostic/anthropometric")
    Call<ResponseBody> sendAntropometric(
            @Field("weight") String wight,
            @Field("height") String height,
            @Field("armCircunference") String armCircunference,
            @Field("waistCircunference") String waistCircunference,
<<<<<<< HEAD
            @Field("sagittalAbdominalDiameter") String sagittalAbdominalDiameter,
            @Field("fistStrength") String fistStrength,
            @Field("heartBeats") String heartBeats,
            @Field("bmi") String bmi,
            @Field("systolicPressure") String systolicPressure,
            @Field("diastolicPressure") String diastolicPressure
    );

    @FormUrlEncoded
    @POST("/diagnostic/personalData")
    Call<ResponseBody> sendPersonal(

            @Field("age") String age,
            @Field("gender") String gender,
            @Field("educationalLevel") String educationalLevel,
            @Field("householdIncome") String householdIncome,
            @Field("totalPeopleResidence") String totalPeopleResidence
    );

    @FormUrlEncoded
    @POST("/diagnostic/nutrients")
    Call<ResponseBody> sendNutrients(
            @Field("calories") String calories,
            @Field("proteins") String proteins,
            @Field("carbohydrates") String carbohydrates,
            @Field("totalSugar") String totalSugar,
            @Field("fibers") String fibers,
            @Field("fats") String fats,
            @Field("saturatedFat") String saturatedFat,
            @Field("monounsaturatedFat") String monounsaturatedFat,
            @Field("polyunsaturatedFat") String polyunsaturatedFat,
            @Field("cholesterol") String cholesterol,
            @Field("alcohol") String alcohol
    );
=======
            @Field("sagittalAbdominalDiameter") String sagittalAbdominalDiameter);

>>>>>>> pinho_dev

    @FormUrlEncoded
    @POST("/diagnostic/makeDiagnostic")
    Call<ResponseBody> makeDiagnostic();
}
