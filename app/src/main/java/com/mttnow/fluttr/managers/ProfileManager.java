package com.mttnow.fluttr.managers;

import com.mttnow.fluttr.domain.hotels.Hotel;
import com.mttnow.fluttr.domain.profile.PreferenceKey;
import com.mttnow.fluttr.domain.profile.Profile;

/**
 * Created by seanb on 26/11/2016.
 */

public class ProfileManager {

  private Profile profile;

  public ProfileManager () {
    profile = new Profile();
  }

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
