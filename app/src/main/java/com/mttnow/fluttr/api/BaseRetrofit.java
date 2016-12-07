package com.mttnow.fluttr.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by seanoflaherty on 27/11/2016.
 */

public interface BaseRetrofit {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.1.3.184:9000")
//            .baseUrl("http://10.1.3.184:9000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
