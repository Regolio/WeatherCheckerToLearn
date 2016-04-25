package com.regoliols.weathercheckertolearn;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.neovisionaries.i18n.CountryCode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // constant's
    final private String API_KEY = "&APPID=b734f67c094feb5bfef0809b0841f1e3";
    final private String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    final private String WEATHER_DAILY = "weather?q=";
    final private String WEATHER_BY_GEO = "weather?";
    final private String WEATHER_BY_GEO_LAT = "lat=";
    final private String WEATHER_BY_GEO_LON = "&lon=";
    final private String BLANK_SPACE = " ";
    final private String UNDEFINE = CountryCode.UNDEFINED.getAlpha2();
    boolean locationOn = false;

    // field's
    private Spinner spnCountry;
    private EditText txtCity;
    private LinearLayout displayLayout;
    private Button btnChecker, btnGPSChecker;
    private String countryShortName, cityName;
    private String[] countryNameList = setupCountryNameList();
    private LocationManager locationManager;
    private LocationListener locationListener;

    private AdView mAdView;
    private Tracker mTracker;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        WeatherCheckerToLearn application = (WeatherCheckerToLearn) getApplication();
        mTracker = application.getDefaultTracker();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new WeatherLocationListener(this, locationManager);

        spnCountry = (Spinner) findViewById(R.id.spnCountry);
        displayLayout = (LinearLayout) findViewById(R.id.displayLayout);
        txtCity = (EditText) findViewById(R.id.txtCity);
        btnChecker = (Button) findViewById(R.id.btnChecker);
        btnGPSChecker = (Button) findViewById(R.id.btnGPSChecker);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryNameList);

        spnCountry.setAdapter(adapter);
        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<CountryCode> current = CountryCode.findByName((String) spnCountry.getSelectedItem());
                countryShortName = current.get(0).getAlpha2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
        if (!locationOn) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int permissionCheckInternet = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET);
                if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED && permissionCheckInternet == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
                    locationOn = !locationOn;
                }
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
                locationOn = !locationOn;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (!locationOn) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int permissionCheckInternet = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET);
                if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED && permissionCheckInternet == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
                    locationOn = !locationOn;
                }
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
                locationOn = !locationOn;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }

        if (locationOn) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(locationListener);
                    locationOn = !locationOn;
                }
            } else {
                locationManager.removeUpdates(locationListener);
                locationOn = !locationOn;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationOn) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(locationListener);
                    locationOn = !locationOn;
                }
            } else {
                locationManager.removeUpdates(locationListener);
                locationOn = !locationOn;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }

        if (locationOn) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(locationListener);
                    locationOn = !locationOn;
                }
            } else {
                locationManager.removeUpdates(locationListener);
                locationOn = !locationOn;
            }
        }
    }

    public void btnChecker(View view) {
        Log.d("Lev", "btnChecker is on line");
        blockUI();
        mTracker.setScreenName("By City&Country Button");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (countryShortName.equals(UNDEFINE)) {
            Toast.makeText(getBaseContext(), "Select a country to proceed", Toast.LENGTH_LONG).show();
            unblockUI();
            return;
        }
        displayLayout.removeAllViews();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            int permissionCheckInternet = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET);
            if (permissionCheckInternet == PackageManager.PERMISSION_GRANTED) {
                cityName = String.valueOf(txtCity.getText());
                if (cityName == null || cityName.equals(" ")) {
                    Toast.makeText(getBaseContext(), "Enter city name to proceed", Toast.LENGTH_LONG).show();
                    unblockUI();
                    return;
                }
                if (cityName.contains(BLANK_SPACE)) {
                    cityName = splitter(cityName);
                }
                String url = WEATHER_URL + WEATHER_DAILY + cityName + "," + countryShortName + API_KEY;
                new DownloadTextTask(this, displayLayout).execute(url);
            }
        } else {
            cityName = String.valueOf(txtCity.getText());
            if (cityName.contains(BLANK_SPACE)) {
                cityName = splitter(cityName);
            }
            String url = WEATHER_URL + WEATHER_DAILY + cityName + "," + countryShortName + API_KEY;
            new DownloadTextTask(this, displayLayout).execute(url);
        }
    }

    public void btnGPSChecker(View view) {
        Log.d("Lev", "btnGPSChecker is on line");
        displayLayout.removeAllViews();
        blockUI();
        mTracker.setScreenName("GeoLocation Button");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            int permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheckInternet = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET);
            if (permissionCheckGPS == PackageManager.PERMISSION_GRANTED && permissionCheckInternet == PackageManager.PERMISSION_GRANTED) {
                WeatherLocationListener geoW = (WeatherLocationListener) locationListener;
                if (geoW.getLongitude() == 0.0 || geoW.getLatitude() == 0.0) {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setBearingAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setPowerRequirement(Criteria.POWER_HIGH);
                    criteria.setBearingRequired(true);
                    criteria.setCostAllowed(true);
                    String bestProvider = locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    String url = WEATHER_URL + WEATHER_BY_GEO + WEATHER_BY_GEO_LAT + String.valueOf(location.getLatitude()) + WEATHER_BY_GEO_LON + String.valueOf(location.getLongitude()) + API_KEY;
                    new DownloadTextTask(this, displayLayout).execute(url);
                    return;
                }
                String url = WEATHER_URL + WEATHER_BY_GEO + WEATHER_BY_GEO_LAT + String.valueOf(geoW.getLatitude()) + WEATHER_BY_GEO_LON + String.valueOf(geoW.getLongitude()) + API_KEY;
                new DownloadTextTask(this, displayLayout).execute(url);
            }
        } else {
            WeatherLocationListener geoW = (WeatherLocationListener) locationListener;
            if (geoW.getLongitude() == 0.0 || geoW.getLatitude() == 0.0) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setBearingAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                criteria.setBearingRequired(true);
                criteria.setCostAllowed(true);
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                String url = WEATHER_URL + WEATHER_BY_GEO + WEATHER_BY_GEO_LAT + String.valueOf(location.getLatitude()) + WEATHER_BY_GEO_LON + String.valueOf(location.getLongitude()) + API_KEY;
                new DownloadTextTask(this, displayLayout).execute(url);
                return;
            }
            String url = WEATHER_URL + WEATHER_BY_GEO + WEATHER_BY_GEO_LAT + String.valueOf(geoW.getLatitude()) + WEATHER_BY_GEO_LON + String.valueOf(geoW.getLongitude()) + API_KEY;
            new DownloadTextTask(this, displayLayout).execute(url);
        }
    }

    private String[] setupCountryNameList() {
        CountryCode countriesList[] = CountryCode.values();
        String[] countryNameList = new String[countriesList.length];
        for (int i = 0; i < countriesList.length; i++) {
            countryNameList[i] = countriesList[i].getName();
        }
        return countryNameList;
    }

    private String splitter(String s) {
        String cityName[] = s.split(BLANK_SPACE);
        String cityNameFinal = "";
        for (String aCityName : cityName) {
            cityNameFinal += aCityName;
        }
        return cityNameFinal;
    }

    protected void blockUI() {
        spnCountry.setEnabled(false);
        txtCity.setEnabled(false);
        btnChecker.setEnabled(false);
        btnGPSChecker.setEnabled(false);
    }

    protected void unblockUI() {
        spnCountry.setEnabled(true);
        txtCity.setEnabled(true);
        btnChecker.setEnabled(true);
        btnGPSChecker.setEnabled(true);
    }
}
