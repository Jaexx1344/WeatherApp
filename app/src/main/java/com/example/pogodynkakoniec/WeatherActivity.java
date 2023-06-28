package com.example.pogodynkakoniec;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;


public class WeatherActivity extends AppCompatActivity {

    public static final Weather WEATHER_PLUS = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        Weather weather = (Weather) intent.getSerializableExtra("WEATHER_PLUS");
        TextView cityAndCountryTv = (TextView)findViewById(R.id.cityAndCountryTV);
        ImageView iconIv = (ImageView)findViewById(R.id.iconIV);
        TextView mainTv = (TextView)findViewById(R.id.mainTV);
        TextView pressureTv = (TextView)findViewById(R.id.pressureTV);
        TextView windTv = (TextView)findViewById(R.id.windTV);
        TextView humidityTv = (TextView)findViewById(R.id.humidityTV);
        TextView temperatureTv = (TextView)findViewById(R.id.temperatureTV);
        TextView feelsTemperatureTv = findViewById(R.id.feelsTemperatureTV);
        cityAndCountryTv.setText(weather.getCityAndCountry());
        mainTv.setText(weather.getMain());
        pressureTv.setText(weather.getPressure() + " hPa");
        windTv.setText(weather.getWind() + " m/s");
        humidityTv.setText(weather.getHumidity() + "%");
        temperatureTv.setText(weather.getTemperature()+"°C");
        feelsTemperatureTv.setText("Temperatura odczuwalna " + weather.getFeelsTemperature()+"°C");


        ConstraintLayout constraintLayout = findViewById(R.id.weatherLayout);
        switch (weather.getMain().toLowerCase()) {
            case "clear":
                constraintLayout.setBackgroundResource(R.drawable.clearsky);
                iconIv.setImageResource(R.drawable.sun);

                break;
            case "few clouds":
            case "clouds":
            case "broken clouds":
                constraintLayout.setBackgroundResource(R.drawable.clouds);
                iconIv.setImageResource(R.drawable.cloudcomputing);

                break;
            case "drizzle":
            case "rain":
                constraintLayout.setBackgroundResource(R.drawable.showerrain);
                iconIv.setImageResource(R.drawable.rain);
                break;
            case "thunderstorm":
                constraintLayout.setBackgroundResource(R.drawable.thunderstorm);
                iconIv.setImageResource(R.drawable.storm);

                break;
            case "snow":
                constraintLayout.setBackgroundResource(R.drawable.snow2);
                iconIv.setImageResource(R.drawable.snow);

                break;
            case "mist":
            case "fog":
                constraintLayout.setBackgroundResource(R.drawable.mist);
                iconIv.setImageResource(R.drawable.foggy);

                break;


        }


    }
}