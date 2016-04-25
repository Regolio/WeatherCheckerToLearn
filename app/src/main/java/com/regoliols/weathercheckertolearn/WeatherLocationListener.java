package com.regoliols.weathercheckertolearn;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 22/02/16.
 */
public class WeatherLocationListener implements LocationListener {

    // fields
    Context context;
    LocationManager locationManager;
    private double latitude;
    private double longitude;

    // constructor
    public WeatherLocationListener(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // methods
    @Override
    public void onLocationChanged(Location location) {
        Log.d("Lev", "onLocationChanged " + location.toString());
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Lev", "In onLocationChanged " + String.valueOf(longitude) + " ," + String.valueOf(latitude));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, provider + " enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Please enable: " + provider, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);

    }
}
