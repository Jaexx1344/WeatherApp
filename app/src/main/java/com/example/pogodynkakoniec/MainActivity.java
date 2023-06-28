package com.example.pogodynkakoniec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tv;
    private AdView mADView;
    public Criteria criteria;
    public String bestProvider;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        mADView=findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mADView.loadAd(adRequest);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tv = (TextView) findViewById(R.id.textView4);
    }

    //Pobieranie danych z poratlu openweathermap i tworzenie instacji klasy weaterh oraz umieszczanie
    // w niej wszystkich dancych "city"

    public void getData(String cityName){
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+cityName+
                "&units=metric&appid=96cd966c0fda10b8db21b31cffb9adac";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);
            String cityAndCountry = json.get("name").getAsString() + ", " + json.get("sys").getAsJsonObject().get("country")
                    .getAsString();
            String iconNumber = json.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
            String main = json.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
            String description = json.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
            String pressure = json.get("main").getAsJsonObject().get("pressure").getAsString();
            String wind = json.get("wind").getAsJsonObject().get("speed").getAsString();
            String humidity = json.get("main").getAsJsonObject().get("humidity").getAsString();
            int temperature = (int) Math.round(json.get("main").getAsJsonObject().get("temp").getAsDouble());
            int feelsTemperature = (int) Math.round(json.get("main").getAsJsonObject().get("feels_like").getAsDouble());
            Weather weather = new Weather(cityAndCountry,iconNumber, main, description, pressure, wind, humidity, temperature,
                    feelsTemperature);
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("WEATHER_PLUS", weather);
            startActivity(intent);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Zła nazwa miasta!",
                        Toast.LENGTH_LONG).show();}
        });
        queue.add(stringRequest);
    }

    //DLa chartsów
    private void getForecastData(String cityName) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+cityName+
                "&units=metric&appid=96cd966c0fda10b8db21b31cffb9adac";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);
            String cityAndCountry = json.get("city").getAsJsonObject().get("name").getAsString() + ", "
                    + json.get("city").getAsJsonObject().get("country").getAsString();
            ArrayList<Temperature> temperatures = new ArrayList<>();
            ArrayList<Cloudiness> clouds = new ArrayList<>();
            JsonArray arr = json.get("list").getAsJsonArray();
            for (int i = 0; i<arr.size(); i++){
                temperatures.add(new Temperature(arr.get(i).getAsJsonObject().get("main").getAsJsonObject().get("temp")
                        .getAsDouble(), arr.get(i).getAsJsonObject().get("dt_txt").getAsString()));
                clouds.add(new Cloudiness(arr.get(i).getAsJsonObject().get("clouds").getAsJsonObject()
                        .get("all").getAsInt(), arr.get(i).getAsJsonObject().get("dt_txt").getAsString()));
            }
            Forecast forecast = new Forecast(cityAndCountry, clouds, temperatures);
            Intent intent = new Intent(this, Charts.class);
            intent.putExtra("FORECAST_PLUS", forecast);
            startActivity(intent);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Zła nazwa miasta!",
                        Toast.LENGTH_LONG).show();
            }}
        );
        queue.add(stringRequest);}

  //Poprzez lat lon wyszukuje miasto ktorego szukamy

    private String hereLocation(double lat, double lon){
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0){
                for (Address adr : addresses){
                    if (adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }


//wywoluje metode here location
    public void onLocationClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }
            else {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("T321AG", "GPS on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(MainActivity.this, "Szerokość:" + latitude + " Długość:" + longitude,
                        Toast.LENGTH_SHORT).show();
                String city = hereLocation(location.getLatitude(), location.getLongitude());
                getData(city);
            }
            else{
                locationManager.requestLocationUpdates(bestProvider,1000,0,this);
            }
            }
    }

   //pokazuje ostatecznie dane
    public void showWeather(View view) {
        EditText cityInput = (EditText)findViewById(R.id.cityInput);
        String cityName = cityInput.getText().toString();

        getData(cityName);
    }
    //chartsy
    public void showForecast(View view) {
        EditText cityInput = (EditText)findViewById(R.id.cityInput);
        String cityName = cityInput.getText().toString();

        getForecastData(cityName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null){
        locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        locationManager.removeUpdates(this);

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(MainActivity.this, "Szerokość:" + latitude + " Długość:" + longitude, Toast.LENGTH_SHORT).show();
        String city = hereLocation(location.getLatitude(), location.getLongitude());
        getData(city);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}