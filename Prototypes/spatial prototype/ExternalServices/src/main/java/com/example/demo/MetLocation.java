package com.example.demo;

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
@JsonIgnoreProperties({ "longitude", "latitude"})
public class MetLocation {
    private @Id ObjectId id;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint location;
    private String sourceId;

    public MetLocation(){}

    public MetLocation(String sourceId, double longitude, double latitude){
        this.sourceId=sourceId;
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
    static class GeoJsonPointDeserializer extends JsonDeserializer<GeoJsonPoint> {

        @Override
        public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException, JsonProcessingException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Double longitude = (Double) ((DoubleNode) node.get("x")).doubleValue();
            Double latitude = (Double) ((DoubleNode) node.get("y")).doubleValue();
            return new GeoJsonPoint(longitude,latitude);
        }
    }
}
