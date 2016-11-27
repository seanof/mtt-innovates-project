package com.mttnow.fluttr.domain.profile;

import java.util.List;

/**
 * Created by seanb on 26/11/2016.
 */

public class Profile {

  List<PreferenceKey> hotelPreferenceKeys;

  public int getHotelKeyIndex(String key) {
    for(int i = 0; i < hotelPreferenceKeys.size(); i++) {
      PreferenceKey prefKey = hotelPreferenceKeys.get(i);
      if (prefKey.getKey().equals(key)) {
        return i;
      }
    }

    return -1;
  }

  public List<PreferenceKey> getHotelPreferenceKeys() {
    return hotelPreferenceKeys;
  }

  public void setHotelPreferenceKeys(List<PreferenceKey> hotelPreferenceKeys) {
    this.hotelPreferenceKeys = hotelPreferenceKeys;
  }


}
