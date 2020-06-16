package model;

import Utilities.GeoJsonPointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.DoubleNode;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;

@Data
@Entity
@JsonIgnoreProperties({ "longitude", "latitude" })
public class WeatherForecast {
    private @Id ObjectId id;
    private int date;
    private int value;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint location;

    public WeatherForecast(){}
    public WeatherForecast(int date, int value, double longitude, double latitude){
        this.date=date;
        this.value=value;
        this.location=new GeoJsonPoint(longitude,latitude);
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