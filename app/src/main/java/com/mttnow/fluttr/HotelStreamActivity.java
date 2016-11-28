package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mttnow.fluttr.listeners.OnSwipeTouchListener;
import com.mttnow.fluttr.managers.HotelStreamManager;
import com.mttnow.fluttr.managers.HotelStreamManagerCallback;
import com.mttnow.fluttr.managers.ProfileManager;
import com.mttnow.fluttr.service.hotels.HotelStreamFragment;

import java.util.List;

public class HotelStreamActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String DESTINATION = "destination";
  private static final String DEPART = "depart";
  private static final String RETURN = "return";
  private static final String NUM_TRAVELLERS = "numTravellers";

  private ViewGroup container;
  private TextView position;

  private ProfileManager profileManager;
  private HotelStreamManager hotelStreamManager;
  private FragmentTransaction ft;

  private String destination;
  private String departDate;
  private String returnDate;
  private String numTravellers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotel_stream);

    Bundle extras = getIntent().getExtras();
    destination = extras.getString(DESTINATION);
    departDate = extras.getString(DEPART);
    returnDate = extras.getString(RETURN);
    numTravellers = extras.getString(NUM_TRAVELLERS);

    ft = getSupportFragmentManager().beginTransaction();

    profileManager = new ProfileManager();

    hotelStreamManager = new HotelStreamManager(this, destination);
    hotelStreamManager.startStream(new HotelStreamManagerCallback() {
      @Override
      public void streamReady(List<HotelStreamFragment> results) {
        Fragment fragment = results.get(0);

        ft.replace(R.id.stream_container, fragment);
        ft.commit();
      }
    });

    position = (TextView) findViewById(R.id.position);

    container = (ViewGroup) findViewById(R.id.stream_container);
    container.setOnTouchListener(new OnSwipeTouchListener(HotelStreamActivity.this) {
      public void onSwipeRight() {
        goToNextHotel();
      }
      public void onSwipeLeft() {
        goToNextHotel();
      }
    });
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.search_btn:
        startActivity(new Intent(this, SearchActivity.class));
        break;
    }
  }

  float startDownX, startDownY;
  float distX, distY;

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getActionMasked();

    switch(action) {
      case MotionEvent.ACTION_DOWN:
        startDownX = event.getX();
        startDownY = event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        distX = event.getX() - startDownX;
        distY = event.getY() - startDownY;
        break;
      case MotionEvent.ACTION_UP:
        distX = 0;
        distY = 0;
        startDownX = 0;
        startDownY = 0;
        break;
    }

    position.setText(distX + ", " + distY);
    return true;
  }

  private void goToNextHotel () {
    profileManager.checkHotelOnLike(hotelStreamManager.getCurrentHotel());

    ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.stream_container, hotelStreamManager.getNextFragment());
    ft.commit();
  }


}
