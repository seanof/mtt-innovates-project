package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

  private FirebaseAuth firebaseAuth;

  private ViewGroup container;
  private TextView position;
  private ProgressBar progressBar;
  private View dislike;
  private View like;

  private ProfileManager profileManager;
  private HotelStreamManager hotelStreamManager;
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

    userSignIn("sean.bolger@mttnow.com", "test1234");

    Bundle extras = getIntent().getExtras();
    destination = extras.getString(DESTINATION);
    departDate = extras.getString(DEPART);
    returnDate = extras.getString(RETURN);
    numTravellers = extras.getInt(NUM_TRAVELLERS);

    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    like = findViewById(R.id.like_button);
    dislike = findViewById(R.id.dislike_button);
    like.setOnClickListener(this);
    dislike.setOnClickListener(this);

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

    hotelStreamManager = new HotelStreamManager(this, destination);

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

  private void userSignIn(String email, String password) {
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener(this, new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(HotelStreamActivity.this, "Authentication failed. Please check your email and password.",
                        Toast.LENGTH_SHORT).show();
                finish();
              }
            })
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FLUTTR", "signInWithEmail:onComplete:" + task.isSuccessful());
                if (firebaseAuth.getCurrentUser() != null) {
                  profileManager = new ProfileManager(firebaseAuth.getCurrentUser().getUid());
                  profileManager.getProfile(firebaseAuth.getCurrentUser().getUid());
                  startUI();
                }

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                  Log.w("FLUTTR", "signInWithEmail:failed", task.getException());
                  Toast.makeText(HotelStreamActivity.this, "Authentication failed. Please check your email and password.",
                          Toast.LENGTH_SHORT).show();
                  finish();
                }
              }
            });
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
