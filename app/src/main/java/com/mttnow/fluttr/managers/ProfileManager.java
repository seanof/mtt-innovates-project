package com.mttnow.fluttr.managers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.domain.profile.PreferenceKey;
import com.mttnow.fluttr.domain.profile.Profile;

/**
 * Created by seanb on 26/11/2016.
 */

public class ProfileManager {

  private Profile profile;

  private DatabaseReference databaseRef;

  private String uid;


  public ProfileManager(String uid) {
    this.profile = new Profile();
    this.databaseRef = FirebaseDatabase.getInstance().getReference();
    this.uid = uid;
    getProfile(uid);
  }

  // [START basic_write]
  public void writeProfile() {
    databaseRef.child("users").child(uid).setValue(profile);
  }

  public void getProfile(String userId) {
    databaseRef.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        profile = dataSnapshot.getValue(Profile.class);
        if (profile == null) {
          profile = new Profile();
          writeProfile();
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }
  // [END basic_write]

  public void checkHotelOnLike (Hotel hotel) {
    //TODO: Some logic to check if we should actually increment the values. Check if we should bother
    for (String string : hotel.getPreferenceKeys()) {
      addHotelKey(string);
    }
    writeProfile();
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
