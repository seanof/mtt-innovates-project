package com.mttnow.fluttr.domain.hotels;

import java.io.Serializable;
import java.util.List;

/**
 * Created by seanb on 22/11/2016.
 */

public class Hotel implements Serializable {

  private HotelInfo retailHotelInfoModel;
  private HotelPricing retailHotelPricingModel;

  private List<String> preferenceKeys;

  public String getHotelName() {
    return retailHotelInfoModel.getName();
  }

  public String getHotelDescription() {
    return retailHotelInfoModel.getDesc();
  }

  public String getHotelImage() {
    return retailHotelInfoModel.getImageUrl();
  }

  public List<String> getPreferenceKeys() {
    return preferenceKeys;
  }

  public void setPreferenceKeys(List<String> preferenceKeys) {
    this.preferenceKeys = preferenceKeys;
  }

}
