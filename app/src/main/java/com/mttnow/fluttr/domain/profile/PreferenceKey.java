package com.mttnow.fluttr.domain.profile;

/**
 * Created by seanb on 26/11/2016.
 */

public class PreferenceKey {

  private String key;
  private int value;

  public void incrementValue() {
    value++;
  }

  public void decrementValue() {
    value--;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
