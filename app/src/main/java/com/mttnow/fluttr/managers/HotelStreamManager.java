package com.mttnow.fluttr.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelsService;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by seanb on 22/11/2016.
 */

public class HotelStreamManager {

  private Context context;
  private HotelsService hotelsService;
  ArrayList<Hotel> currentStream;

  public HotelStreamManager(Context c) {
    context = c;
    hotelsService = new HotelsService(new Gson());
    startStream();
  }

  private void startStream () {
    currentStream = new ArrayList<Hotel>();
    try {
      currentStream = (ArrayList<Hotel>) hotelsService.getHotels("sfo", context);
      Log.v("", currentStream.size() + "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
