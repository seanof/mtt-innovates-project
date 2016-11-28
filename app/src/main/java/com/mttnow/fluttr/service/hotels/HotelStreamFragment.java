package com.mttnow.fluttr.service.hotels;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mttnow.fluttr.R;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class HotelStreamFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String HOTEL_DATA = "param1";

  // TODO: Rename and change types of parameters
  private Hotel hotel;

//  private OnFragmentInteractionListener mListener;

  public HotelStreamFragment() { }

  public static HotelStreamFragment newInstance(Hotel param1) {
    HotelStreamFragment fragment = new HotelStreamFragment();
    Bundle args = new Bundle();
    args.putSerializable(HOTEL_DATA, param1);
    fragment.setArguments(args);
    return fragment;
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
    ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_hotel_stream, container, false);

    TextView name = (TextView) v.findViewById(R.id.hotel_name);
    ImageView image = (ImageView) v.findViewById(R.id.hotel_image);

    name.setText(hotel.getHotelName());
    Picasso.with(getContext()).load("https:" + hotel.getHotelImage()).into(image);

    ViewGroup leftCol = (ViewGroup) v.findViewById(R.id.hotel_keys_left_col);
    ViewGroup rightCol = (ViewGroup) v.findViewById(R.id.hotel_keys_right_col);
    int i = 0;
    for(String key : hotel.getPreferenceKeys()) {
      TextView txt1 = new TextView(getContext());
      txt1.setText(key);
      if (i%2 == 0) {
        txt1.setTextAppearance(getContext(), R.style.stream_key_item);
        leftCol.addView(txt1);
      } else {
        txt1.setTextAppearance(getContext(), R.style.stream_key_item_right);
        rightCol.addView(txt1);
      }
      i++;
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
    super.onDetach();
//    mListener = null;
  }

//  public interface OnFragmentInteractionListener {
//    // TODO: Update argument type and name
//    void onFragmentInteraction(Uri uri);
//  }

}
