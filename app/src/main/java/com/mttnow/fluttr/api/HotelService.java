package com.mttnow.fluttr.api;


import com.mttnow.fluttr.domain.hotels.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface HotelService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.16:9000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/hotels")
    Call<List<Hotel>> listHotels();
}
