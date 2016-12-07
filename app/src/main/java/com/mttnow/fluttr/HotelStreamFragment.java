package com.mttnow.fluttr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.squareup.picasso.Picasso;

public class HotelStreamFragment extends Fragment implements View.OnClickListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String HOTEL_DATA = "param1";

  // TODO: Rename and change types of parameters
  private Hotel hotel;
  private long startTime;
  private TextView desc;

//  private OnFragmentInteractionListener mListener;

  public HotelStreamFragment() { }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      hotel = (Hotel) getArguments().getSerializable(HOTEL_DATA);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    startTime = System.currentTimeMillis();

    ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_hotel_stream, container, false);

    TextView name = (TextView) v.findViewById(R.id.hotel_name);
    ImageView image = (ImageView) v.findViewById(R.id.hotel_image);
    desc = (TextView) v.findViewById(R.id.hotel_desc);

    name.setText(hotel.getHotelName());
    TextView price = (TextView) v.findViewById(R.id.hotel_price);
    price.setText(hotel.getPriceString());

    Picasso.with(getContext()).load("https:" + hotel.getHotelImage()).into(image);

    desc.setText(hotel.getHotelDescription());
    desc.setOnClickListener(this);

    ViewGroup leftCol = (ViewGroup) v.findViewById(R.id.hotel_keys_left_col);

    for(String key : hotel.getPreferenceKeys()) {
      ViewGroup tagView = (ViewGroup) inflater.inflate(R.layout.key_tag_view, leftCol, false);
      TextView txt1 = (TextView) tagView.findViewById(R.id.tag_text);
      txt1.setText(key);
      leftCol.addView(tagView);
    }

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
//    if (context instanceof OnFragmentInteractionListener) {
//      mListener = (OnFragmentInteractionListener) context;
//    } else {
//      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//    }
  }

  @Override
  public void onDetach() {
    logTimingEvent();
    super.onDetach();
//    mListener = null;
  }

  private void logTimingEvent() {
    long endTime = System.currentTimeMillis();
    FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(getContext());
    Bundle params = new Bundle();
    params.putString("hotel_view_timing", Long.toString(endTime - startTime));
    analytics.logEvent("user_timing", params);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.hotel_desc:
        desc.setMaxLines(Integer.MAX_VALUE);
        break;
    }
  }

//  public interface OnFragmentInteractionListener {
//    // TODO: Update argument type and name
//    void onFragmentInteraction(Uri uri);
//  }

}
