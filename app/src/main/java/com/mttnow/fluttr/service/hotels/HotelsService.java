package com.mttnow.fluttr.service.hotels;

import android.content.Context;

import com.google.gson.Gson;
import com.mttnow.fluttr.domain.hotels.Hotel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by seanb on 22/11/2016.
 */

public class HotelsService {

  private static Gson gson;

  private HotelsService () throws FileNotFoundException {
    gson = new Gson();
  }

  public static List<Hotel> getHotels(String destination) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("mock-expedia-hotels-" + destination + ".json"))));
    return (List<Hotel>) gson.fromJson(reader, Hotel.class);
  }

}
