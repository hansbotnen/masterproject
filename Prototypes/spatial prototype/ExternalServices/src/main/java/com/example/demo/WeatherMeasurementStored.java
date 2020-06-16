package com.example.demo;

import com.example.demo.MetLocation;
import com.example.demo.WeatherMeasurement;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.*;

@Data
@Entity
public class WeatherMeasurementStored {
    private @Id ObjectId id;
    @ManyToOne()
    @JoinColumn(name="station_id", referencedColumnName = "id")
    private MetLocation station;
    private int value;
    private int date;

    public WeatherMeasurementStored(){}
    public WeatherMeasurementStored(MetLocation station, int value, int date) {
        this.station = station;
        this.value = value;
        this.date=date;
    }

    public WeatherMeasurement getMeasurement(){
        WeatherMeasurement measurement= new WeatherMeasurement();
        measurement.setId(id);
        measurement.setValue(value);
        measurement.setDate(date);
        measurement.setLatitude(station.getLatitude());
        measurement.setLongitude(station.getLongitude());
        return measurement;
    }
}
