package com.mttnow.fluttr.domain.hotels;

import java.io.Serializable;

/**
 * Created by seanb on 22/11/2016.
 */

public class Hotel implements Serializable {

  private HotelInfo retailHotelInfoModel;
  private HotelPricing retailHotelPricingModel;

  public String getHotelName() {
    return retailHotelInfoModel.getName();
  }

  public String getHotelImage() {
    return retailHotelInfoModel.getImageUrl();
  }
}
