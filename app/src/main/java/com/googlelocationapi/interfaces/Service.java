package com.googlelocationapi.interfaces;

import com.googlelocationapi.Data.ResponseData;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

        @POST("maps/api/place/textsearch/json")
        Call<ResponseData> getComment(
                @Query("key") String key,
               @Query("query") String query
         );
}

