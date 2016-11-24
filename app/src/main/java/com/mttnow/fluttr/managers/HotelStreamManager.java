package com.mttnow.fluttr.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelStreamFragment;
import com.mttnow.fluttr.service.hotels.HotelsService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by seanb on 22/11/2016.
 */

public class HotelStreamManager {

  private Context context;
  private HotelsService hotelsService;

  public ArrayList<Hotel> currentStream;

  public HotelStreamManager(Context c) {
    context = c;
    hotelsService = new HotelsService(new Gson());
  }

  public void startStream (StreamManagerCallback callback) {
    currentStream = new ArrayList<>();
    try {
      currentStream = (ArrayList<Hotel>) hotelsService.getHotels("sfo", context);
      callback.streamReady(getHotelFragments(currentStream));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<HotelStreamFragment> getHotelFragments(ArrayList<Hotel> hotels) {
    ArrayList<HotelStreamFragment> hotelFragments = new ArrayList<>();

    for (Hotel hotel : hotels) {
      HotelStreamFragment hotelFragment = HotelStreamFragment.newInstance(hotel);
      hotelFragments.add(hotelFragment);
    }

    return hotelFragments;
  }

}
