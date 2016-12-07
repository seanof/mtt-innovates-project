package com.mttnow.fluttr;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mttnow.fluttr.domain.hotels.Hotel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by seanb on 06/12/2016.
 */

public class HotelResultAdapter extends BaseAdapter {

  List<Hotel> hotels;

  public HotelResultAdapter(List<Hotel> likedHotels) {
    hotels = likedHotels;
  }

  @Override
  public boolean areAllItemsEnabled() {
    return false;
  }

  @Override
  public boolean isEnabled(int position) {
    return false;
  }

  @Override
  public void registerDataSetObserver(DataSetObserver observer) {

  }

  @Override
  public void unregisterDataSetObserver(DataSetObserver observer) {

  }

  @Override
  public int getCount() {
    return hotels.size();
  }

  @Override
  public Object getItem(int position) {
    return hotels.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    Hotel hotel = hotels.get(position);
    View item;

    if (convertView != null) {
      item = convertView;
    } else {
      item = inflater.inflate(R.layout.hotel_result_item, parent, false);
    }

    ((TextView) item.findViewById(R.id.result_item_name)).setText(hotel.getHotelName());
    Picasso.with(parent.getContext()).load("https:" + hotel.getHotelImage()).into(
      (CircleImageView) item.findViewById(R.id.result_item_image));
    ((TextView) item.findViewById(R.id.result_item_price)).setText(hotel.getPriceString());

    return item;
  }



  @Override
  public boolean isEmpty() {
    return false;
  }
}
