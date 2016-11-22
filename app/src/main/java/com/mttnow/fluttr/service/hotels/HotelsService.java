package com.mttnow.fluttr.service.hotels;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mttnow.fluttr.domain.hotels.Hotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seanb on 22/11/2016.
 */

public class HotelsService {

  private Gson gson;

   public HotelsService(Gson g) {
    gson = g;
  }

  public List<Hotel> getHotels(String destination, Context c) throws IOException {
    Reader reader = new BufferedReader(new InputStreamReader(c.getAssets().open("mock-expedia-hotels-" + destination + ".json"), "UTF-8"));
    return gson.fromJson(reader, new TypeToken<ArrayList<Hotel>>() {}.getType());
  }

}
