package com.mttnow.fluttr.service.hotels;


import com.mttnow.fluttr.domain.hotels.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by seanof on 26/11/2016.
 */

public interface HotelsService {

    @GET("/hotels")
    Call<List<Hotel>> listHotels(@Query("destination") String destination);
}
