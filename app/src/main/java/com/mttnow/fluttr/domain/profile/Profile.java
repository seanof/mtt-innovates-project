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

  public void addHotelKey (String key) {
    int hotelPrefIndex = getHotelKeyIndex(key);
    if (hotelPrefIndex > -1) {
      hotelPreferenceKeys.get(hotelPrefIndex).incrementValue();
    } else {
      PreferenceKey newKey = new PreferenceKey();
      newKey.setKey(key);
      newKey.setValue(1);
      hotelPreferenceKeys.add(newKey);
    }
  }

}
