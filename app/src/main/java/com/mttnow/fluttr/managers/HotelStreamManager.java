package com.mttnow.fluttr.managers;

import android.content.Context;
import android.widget.Toast;

import com.mttnow.fluttr.api.HotelService;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelStreamFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelStreamManager {

    private Context context;
    private List<Hotel> currentHotelStream;
    private ArrayList<HotelStreamFragment> currentHotelFragments;
    private int hotelIndex;

    public HotelStreamManager(Context c) {
        context = c;
    }

    public void startStream (final StreamManagerCallback callback) {
        HotelService service = HotelService.retrofit.create(HotelService.class);
        Call<List<Hotel>> call = service.listHotels();
        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response != null) {
                    currentHotelStream = response.body();
                    callback.streamReady(getHotelFragments(new ArrayList<>(response.body())));
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(context, "An error occurred, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Hotel getCurrentHotel () {
        return currentHotelStream.get(hotelIndex);
    }

    public HotelStreamFragment getNextFragment() {
        hotelIndex++;
        return currentHotelFragments.get(hotelIndex);
    }

    private ArrayList<HotelStreamFragment> getHotelFragments(ArrayList<Hotel> hotels) {
        currentHotelFragments = new ArrayList<>();

        for (Hotel hotel : hotels) {
            HotelStreamFragment hotelFragment = HotelStreamFragment.newInstance(hotel);
            currentHotelFragments.add(hotelFragment);
        }

        return currentHotelFragments;
    }
}
