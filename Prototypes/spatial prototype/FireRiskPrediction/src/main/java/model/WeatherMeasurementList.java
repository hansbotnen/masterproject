package model;

import com.example.demo.WeatherMeasurement;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeatherMeasurementList implements Serializable {
    List<WeatherMeasurement> measurements;
    public WeatherMeasurementList(){}
    public WeatherMeasurementList(List<WeatherMeasurement> measurements) {
        this.measurements = measurements;
    }

}
