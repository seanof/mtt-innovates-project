package com.mttnow.fluttr.managers;

import android.content.Context;
import android.widget.Toast;

import com.mttnow.fluttr.HotelStreamFragment;
import com.mttnow.fluttr.api.BaseRetrofit;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.service.hotels.HotelsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelStreamManager {

    private Context context;
    private List<Hotel> currentHotelStream;
    private List<Hotel> likedHotelStream;
    private ArrayList<HotelStreamFragment> currentHotelFragments;
    private ProfileManager profileManager;

    private int hotelIndex;

    private String destination;

    public HotelStreamManager(Context c, String destination, ProfileManager profileManager) {
        this.context = c;
        this.destination = destination;
        this.likedHotelStream = new ArrayList<>();
        this.profileManager = profileManager;
    }

    public void startStream (final StreamManagerCallback<HotelStreamFragment> callback) {
        if (destination != null) {
            HotelsService service = BaseRetrofit.retrofit.create(HotelsService.class);
            Call<List<Hotel>> call = service.listHotels(destination, profileManager.getCurrentProfile().getHotelPreferenceKeys().toString());
            call.enqueue(new Callback<List<Hotel>>() {
                @Override
                public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                    if (response != null && response.body() != null) {
                        currentHotelStream = response.body();
                        callback.streamReady(getHotelFragments(new ArrayList<>(response.body())));
                    }
                }

                @Override
                public void onFailure(Call<List<Hotel>> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(context, "An error occurred, please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public Hotel getCurrentHotel () {
        return currentHotelStream.get(hotelIndex);
    }

    public void likeCurrentHotel () {
        likedHotelStream.add(getCurrentHotel());
    }

    public HotelStreamFragment getNextFragment() {
        hotelIndex++;
        return currentHotelFragments.get(hotelIndex);
    }

    private ArrayList<HotelStreamFragment> getHotelFragments(ArrayList<Hotel> hotels) {
        currentHotelFragments = new ArrayList<>();

        for (Hotel hotel : hotels) {
            HotelStreamFragment hotelFragment = new HotelStreamFragment();
            hotelFragment.setHotel(hotel);
            currentHotelFragments.add(hotelFragment);
        }

        return currentHotelFragments;
    }

    public int getHotelIndex() {
        return hotelIndex;
    }

    public List<Hotel> getLikedHotelStream() {
        return likedHotelStream;
    }

    public boolean isEndOfStream() {
        return hotelIndex >= currentHotelStream.size() - 1;
    }
}
