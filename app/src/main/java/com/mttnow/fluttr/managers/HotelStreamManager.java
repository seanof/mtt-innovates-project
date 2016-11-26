package com.mttnow.fluttr.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mttnow.fluttr.api.HotelService;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelStreamFragment;
import com.mttnow.fluttr.service.hotels.HotelsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class HotelStreamManager {

    private Context context;
    private HotelsService hotelsService;

    public List<Hotel> hotelList;
    public ArrayList<Hotel> currentStream;

    public HotelStreamManager(Context c) {
        context = c;
        hotelsService = new HotelsService(new Gson());
    }

    public void startStream (StreamManagerCallback callback) {
        HotelService service = HotelService.retrofit.create(HotelService.class);
        Call<List<Hotel>> call = service.listHotels("sfo");
        try {
            hotelList = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("app", hotelList.toString());



//    currentStream = new ArrayList<>();
//    try {
//      currentStream = (ArrayList<Hotel>) hotelsService.getHotels("sfo", context);
//      callback.streamReady(getHotelFragments(currentStream));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
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
