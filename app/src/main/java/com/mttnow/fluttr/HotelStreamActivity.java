package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.managers.HotelStreamManager;
import com.mttnow.fluttr.managers.HotelStreamManagerCallback;
import com.mttnow.fluttr.managers.StreamManagerCallback;
import com.mttnow.fluttr.service.hotels.HotelStreamFragment;

import java.util.List;

public class HotelStreamActivity extends AppCompatActivity implements View.OnClickListener {

  private ViewGroup container;

  private HotelStreamManager hotelStreamManager;
  private FragmentTransaction ft;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotel_stream);

    ft = getSupportFragmentManager().beginTransaction();

    hotelStreamManager = new HotelStreamManager(this);
    hotelStreamManager.startStream(new HotelStreamManagerCallback() {
      @Override
      public void streamReady(List<HotelStreamFragment> results) {
        Fragment fragment = results.get(0);

        ft.replace(R.id.stream_container, fragment);
        ft.commit();
      }
    });

    ((Button) findViewById(R.id.like_button)).setOnClickListener(this);

    container = (ViewGroup) findViewById(R.id.stream_container);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.begin_btn:
        startActivity(new Intent(this, PresenterActivity.class));
        break;
      case R.id.like_button:
        goToNextHotel();
        break;
    }
  }

  private void goToNextHotel () {
    ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.stream_container, hotelStreamManager.getNextFragment());
    ft.commit();
  }
}
