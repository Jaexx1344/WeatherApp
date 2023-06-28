package com.example.pogodynkakoniec;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Charts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        Forecast forecast  = (Forecast) intent.getSerializableExtra("FORECAST_PLUS");
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(forecast.getCityAndCountry());
        List<DataEntry> cloudsData = new ArrayList<>();
        List<DataEntry> temperatureData = new ArrayList<>();
        for (int i = 0; i < forecast.getClouds().size(); i += 8){
            String date = forecast.getTemperatures().get(i).getDate().split(" ")[0].substring(8)
                    + "/" + forecast.getTemperatures().get(i).getDate().split(" ")[0].substring(5,7);
            temperatureData.add(new ValueDataEntry(date, forecast.getTemperatures().get(i).getTemperature()));
            cloudsData.add(new ValueDataEntry(date, forecast.getClouds().get(i).getClouds()));
            Log.e("TAG", String.valueOf(forecast.getClouds().get(i).getClouds()));
        }
        AnyChartView anyChartView = findViewById(R.id.any_chart_view2);
        Cartesian cartesian = AnyChart.column();
        cartesian.column(cloudsData);
        cartesian.setBackground("#e1f5fe");
        cartesian.setTitle("Zachmurzenie");
        cartesian.getXAxis().setTitle("Data");
        cartesian.getYAxis().setTitle("Chmury [%]");
        cartesian.getYScale().setMinimum(0.);
        cartesian.getYScale().setMaximum(100.);

        anyChartView.setChart(cartesian);
        AnyChartView tempChartView = findViewById(R.id.any_chart_view);
        Cartesian tempCartesian = AnyChart.line();
        tempCartesian.line(temperatureData);
        tempCartesian.setTitle("Temperatura");
        tempCartesian.getXAxis().setTitle("Data");
        tempCartesian.getYAxis().setTitle("Temperatura [°C]");
        tempCartesian.setBackground("#e1f5fe");
        tempChartView.setChart(tempCartesian);



    }
}