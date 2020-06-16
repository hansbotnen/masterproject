package model;

import Utilities.GeoJsonPointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties({ "longitude", "latitude"})
public class WeatherMeasurement {
    private @Id ObjectId id;
    private int date;
    private int value;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint location;

    public WeatherMeasurement(){}
    public WeatherMeasurement(int date, int value, double longitude, double latitude){
        this.date=date;
        this.value=value;
        this.location = new GeoJsonPoint(longitude,latitude);
    }
    public double getLongitude(){
        return location.getX();
    }
    public double getLatitude(){
        return location.getY();
    }
    public void setLongitude(double longitude){
        double lat= location != null ? location.getY():0.0;
        location= new GeoJsonPoint(longitude,lat);
    }
    public void setLatitude(double latitude){
        double lon= location != null ? location.getX():0.0;
        location= new GeoJsonPoint(lon,latitude);
    }
}
