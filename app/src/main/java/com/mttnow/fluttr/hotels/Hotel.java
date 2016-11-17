package com.mttnow.fluttr.hotels;

import java.util.List;

/**
 * Created by seanoflaherty on 14/11/2016.
 */

public class Hotel {

    private String name;
    private String desc;
    private List<String> amenities;
    private String starRating;
    private String imageUrl;
    private String price;
    private long lat;
    private long lng;
    private String nearestIata;
    private List<String> neighbourhoods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public String getNearestIata() {
        return nearestIata;
    }

    public void setNearestIata(String nearestIata) {
        this.nearestIata = nearestIata;
    }

    public List<String> getNeighbourhoods() {
        return neighbourhoods;
    }

    public void setNeighbourhoods(List<String> neighbourhoods) {
        this.neighbourhoods = neighbourhoods;
    }
}
