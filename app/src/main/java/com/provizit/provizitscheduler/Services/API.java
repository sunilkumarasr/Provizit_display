package com.provizit.provizitscheduler.Services;

import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import retrofit2.http.Query;

public interface API {

    @GET("masters/getuserDetails")
    Call<Model> getuserDetails(@Header("Authorization") String Bearer,@Header("DeviceId") String header, @Query("id") String id);

    @POST("setup/schedulerLogin")
    Call<Model> getuserLogin(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Body JsonObject jsonBody);

    @GET("company/getmeetingrooms")
    Call<Model1> getmeetingrooms(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Query("id") String id, @Query("location") String location);
    @GET("company/getworkingdaymrddetails")
    Call<Model> getworkingdaymrddetails(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Query("id") String id);

     @GET("meeting/getrmslots")
    Call<Model1> getrmslots(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Query("type") String type, @Query("comp_id") String comp_id, @Query("start") Long start, @Query("end") Long end, @Query("rm_id") String rm_id);


    @GET("setup/getappointments")
    Call<OutlookModel> getappointments(@Query("comp_id") String comp_id, @Query("rm_id") String rm_id, @Query("start") Long start, @Query("end") Long end, @Query("type") String type);

    @GET("microsoft/meetingRoomData")
    Call<OutlookModel> getMicrosoftMeetings(@Header("Authorization") String Bearer,@Header("DeviceId") String header, @Query("id") String id);


 }
