package com.mttnow.fluttr.managers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.domain.profile.PreferenceKey;
import com.mttnow.fluttr.domain.profile.Profile;

/**
 * Created by seanb on 26/11/2016.
 */

public class ProfileManager {

  private Profile profile;

  private DatabaseReference databaseRef;


  public ProfileManager(String uid) {
    profile = new Profile();

    databaseRef = FirebaseDatabase.getInstance().getReference();
    Query res = databaseRef.child("users").child(uid);
    res.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        snapshot.getValue();
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  // [START basic_write]
  private void writeNewUser(String userId, Profile profile) {
    databaseRef.child("users").child(userId).setValue(profile);
  }
  // [END basic_write]

  public void checkHotelOnLike (Hotel hotel) {
    //TODO: Some logic to check if we should actually increment the values. Check if we should bother
    for (String string : hotel.getPreferenceKeys()) {
      addHotelKey(string);
    }
  }

  private void addHotelKey (String key) {
    int hotelPrefIndex = profile.getHotelKeyIndex(key);
    if (hotelPrefIndex > -1) {
      profile.getHotelPreferenceKeys().get(hotelPrefIndex).incrementValue();
    } else {
      PreferenceKey newKey = new PreferenceKey();
      newKey.setKey(key);
      newKey.setValue(1);
      profile.getHotelPreferenceKeys().add(newKey);
    }
  }

}
