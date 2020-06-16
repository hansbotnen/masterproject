package com.example.demo;

import java.util.Random;

public class WeatherDataFactory {
    public static WeatherMeasurementStored createMeasurement(int date, MetLocation station){
        int value = new Random().nextInt(10);
        return new WeatherMeasurementStored(station,value,date);
    }

    public static WeatherForecast createForecast(int date, double longitude, double latitude){
        int value = new Random().nextInt(10);
        return new WeatherForecast(date,value,longitude,latitude);
    }
}
