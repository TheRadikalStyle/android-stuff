/*
 * @author: David Ochoa Gutierrez
 * @contact: @theradikalstyle - david.ochoa.gtz@outlook.com
 * @copyright: TheRadikalSoftware - 2019
 */

package com.theradikalsoftware.examennexusarkus;

import java.io.Serializable;

public class PlacesData implements Serializable {
    String PlaceId;
    String PlaceName;
    String Address;
    String Category;
    String IsOpenNow;
    String Latitude;
    String Longitude;
    String Thumbnail;
    String Rating;
    String IsPetFriendly;
    String AddressLine1;
    String AddressLine2;
    String PhoneNumber;
    String Site;
    String distance;

    public String getPlaceId() {
        return PlaceId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public String getAddress() {
        return Address;
    }

    public String getCategory() {
        return Category;
    }

    public String getIsOpenNow() {
        return IsOpenNow;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getRating() {
        return Rating;
    }

    public String getIsPetFriendly() {
        return IsPetFriendly;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getSite() {
        return Site;
    }

    public String getDistance() {
        return distance;
    }
}
