package com.example.demo;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class WeatherForecast {
    private @Id ObjectId id;
    private int date;
    private int value;
    private double longitude;
    private double latitude;

    public WeatherForecast(){}
    public WeatherForecast(int date, int value, double longitude, double latitude){
        this.date=date;
        this.value=value;
        this.longitude=longitude;
        this.latitude=latitude;
    }


}
