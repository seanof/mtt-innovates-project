package com.mttnow.fluttr.api;


import com.mttnow.fluttr.domain.hotels.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotelService {
    @GET("/hotels/{destination}")
    Call<List<Hotel>> listHotels(@Path("destination") String destination);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .build();
}
