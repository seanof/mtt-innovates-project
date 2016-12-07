package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.mttnow.fluttr.listeners.OnSwipeTouchListener;
import com.mttnow.fluttr.managers.HotelStreamManager;
import com.mttnow.fluttr.managers.HotelStreamManagerCallback;
import com.mttnow.fluttr.managers.ProfileManager;

import java.util.List;

public class HotelStreamActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String DESTINATION = "destination";
  private static final String DEPART = "depart";
  private static final String RETURN = "return";
  private static final String NUM_TRAVELLERS = "numTravellers";

  private ViewGroup container;
  private TextView position;
  private ProgressBar progressBar;

  private HotelStreamManager hotelStreamManager;
  private ProfileManager profileManager;
  private FragmentTransaction ft;

  private String destination;
  private String departDate;
  private String returnDate;
  private int numTravellers;

  InterstitialAd interstitialAd;
  private static final int INTERSTITIAL_LOAD_THRESHOLD = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_hotel_stream);

    MobileAds.initialize(getApplicationContext(), getString(R.string.ads_app_id));

    Bundle extras = getIntent().getExtras();
    destination = extras.getString(DESTINATION);
    departDate = extras.getString(DEPART);
    returnDate = extras.getString(RETURN);
    numTravellers = extras.getInt(NUM_TRAVELLERS);

    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    findViewById(R.id.like_button).setOnClickListener(this);
    findViewById(R.id.dislike_button).setOnClickListener(this);

    profileManager = new ProfileManager(FirebaseAuth.getInstance().getCurrentUser().getUid());
    startUI();

    interstitialAd = new InterstitialAd(this);
    interstitialAd.setAdUnitId(getString(R.string.hotel_interstitial_ad));
    interstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
        requestNewInterstitial();
      }
    });

    requestNewInterstitial();

  }

  private void startUI() {

    ft = getSupportFragmentManager().beginTransaction();

    hotelStreamManager = new HotelStreamManager(this, destination, null);

    hotelStreamManager.startStream(new HotelStreamManagerCallback() {
      @Override
      public void streamReady(List<HotelStreamFragment> results) {
        Fragment fragment = results.get(0);

        ft.replace(R.id.stream_container, fragment);
        ft.commit();

        showButtonControls();

        position = (TextView) findViewById(R.id.position);

        container = (ViewGroup) findViewById(R.id.stream_container);
        container.setOnTouchListener(new OnSwipeTouchListener(HotelStreamActivity.this) {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            super.onTouch(v, event);
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
          public void onSwipeRight() {
            goToNextHotel();
          }
          public void onSwipeLeft() {
            //Like
            hotelStreamManager.likeCurrentHotel();
            goToNextHotel();
          }
        });

        hideProgress();
      }
    });

    AdView mAdView = (AdView) findViewById(R.id.adView);

    // for real devices
//    AdRequest request = new AdRequest.Builder()
//      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//      .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
//      .build();

    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    interstitialAd = new InterstitialAd(this);
    interstitialAd.setAdUnitId(getString(R.string.hotel_interstitial_ad));

    interstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
        requestNewInterstitial();
      }
    });

    requestNewInterstitial();

  }

  private void requestNewInterstitial() {
    AdRequest adRequest = new AdRequest.Builder()
            .build();

    interstitialAd.loadAd(adRequest);
  }

  private void hideProgress() {
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.search_btn:
        startActivity(new Intent(this, SearchActivity.class));
        break;
      case R.id.like_button:
        hotelStreamManager.likeCurrentHotel();
        goToNextHotel();
        break;
      case R.id.dislike_button:
        goToNextHotel();
        break;
    }
  }

  float startDownX, startDownY;
  float distX, distY;

  private void goToNextHotel () {
    if (hotelStreamManager.isEndOfStream()) {

      HotelStreamResultsFragment hotelStreamResultsFragment = new HotelStreamResultsFragment();
      hotelStreamResultsFragment.setHotelData(hotelStreamManager.getLikedHotelStream());

      ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.stream_container, hotelStreamResultsFragment);
      ft.commit();

      hideButtonControls();

    } else {
      if (hotelStreamManager.getHotelIndex() % INTERSTITIAL_LOAD_THRESHOLD == 0 && hotelStreamManager.getHotelIndex() != 0 && interstitialAd.isLoaded()) {
        interstitialAd.show();
      }

      profileManager.checkHotelOnLike(hotelStreamManager.getCurrentHotel());

      ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.stream_container, hotelStreamManager.getNextFragment());
      ft.commit();

    }
  }

  private void showButtonControls () {
    findViewById(R.id.hotel_buttons_layout).setVisibility(View.VISIBLE);
  }

  private void hideButtonControls () {
    findViewById(R.id.hotel_buttons_layout).setVisibility(View.GONE);
  }

}
