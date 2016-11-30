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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

  private FirebaseAuth firebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;

  private ViewGroup container;
  private TextView position;

  private ProfileManager profileManager;
  private HotelStreamManager hotelStreamManager;
  private FragmentTransaction ft;

  private String destination;
  private String departDate;
  private String returnDate;
  private int numTravellers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotel_stream);

    firebaseAuth = FirebaseAuth.getInstance();
    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
          Log.d("Fluttr", "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
          // User is signed out
          Log.d("Fluttr", "onAuthStateChanged:signed_out");
        }
        // ...
      }
    };

    userSignIn("sean.bolger@mttnow.com", "test1234");

    Bundle extras = getIntent().getExtras();
    destination = extras.getString(DESTINATION);
    departDate = extras.getString(DEPART);
    returnDate = extras.getString(RETURN);
    numTravellers = extras.getInt(NUM_TRAVELLERS);

    ft = getSupportFragmentManager().beginTransaction();

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
        goToNextHotel();
      }
    });
  }

  private void userSignIn(String email, String password) {
    firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FLUTTR", "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                  Log.w("FLUTTR", "signInWithEmail:failed", task.getException());
                  Toast.makeText(HotelStreamActivity.this, "Authentication failed. Please check your email and password.",
                          Toast.LENGTH_SHORT).show();
                }

                // ...
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



  private void goToNextHotel () {
    profileManager.checkHotelOnLike(hotelStreamManager.getCurrentHotel());

    ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.stream_container, hotelStreamManager.getNextFragment());
    ft.commit();
  }


  @Override
  public void onStart() {
    super.onStart();
    firebaseAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mAuthListener != null) {
      firebaseAuth.removeAuthStateListener(mAuthListener);
    }
  }


}
