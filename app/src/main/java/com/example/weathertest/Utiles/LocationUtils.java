package com.example.weathertest.Utiles;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    private static Geocoder _geocoder = null;
    private static Geocoder getGeocoder(Context context) {
        if (_geocoder == null) {
            _geocoder = new Geocoder(context);
        }
        return _geocoder;
    }

    public static ArrayList<GeocodeUtil.GeoLocation> getGeoLocationListUsingAddress(Context context, String address) {
        ArrayList<GeocodeUtil.GeoLocation> resultList = new ArrayList<>();

        try {
            List<Address> list = getGeocoder(context).getFromLocationName(address, 10);

            for (Address addr : list) {
                resultList.add(new GeocodeUtil.GeoLocation(addr.getLatitude(), addr.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public ArrayList<String> getAddressListUsingGeolocation(Context context, GeocodeUtil.GeoLocation location) {
        ArrayList<String> resultList = new ArrayList<>();

        try {
            List<Address> list = getGeocoder(context).getFromLocation(location.latitude, location.longitude, 10);

            for (Address addr : list) {
                resultList.add(addr.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

}
