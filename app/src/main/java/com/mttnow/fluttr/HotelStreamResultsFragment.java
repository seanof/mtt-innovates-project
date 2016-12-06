package com.mttnow.fluttr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelStreamResultsFragment extends Fragment implements View.OnClickListener {

  private List<Hotel> likedHotels;

  public HotelStreamResultsFragment() { }

  public void setHotelData (List<Hotel> hotels) {
    likedHotels = hotels;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_hotel_stream, container, false);

    ListView hotelResultList = (ListView) v.findViewById(R.id.results_list);
    hotelResultList.setAdapter(new HotelResultAdapter(likedHotels));

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.hotel_desc:

        break;
    }
  }

}
