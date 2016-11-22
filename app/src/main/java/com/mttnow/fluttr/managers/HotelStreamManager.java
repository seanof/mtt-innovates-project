package com.mttnow.fluttr.managers;

import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelsService;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by seanb on 22/11/2016.
 */

public class HotelStreamManager {

  ArrayList<Hotel> currentStream;

  public HotelStreamManager() {
    startStream();
  }

  private void startStream () {
    currentStream = new ArrayList<Hotel>();
    try {
      currentStream = (ArrayList<Hotel>) HotelsService.getHotels("SFO");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}
