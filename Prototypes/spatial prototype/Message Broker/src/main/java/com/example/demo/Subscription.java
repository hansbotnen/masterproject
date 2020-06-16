package com.example.demo;

import Utilities.GeoJsonPointDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.DoubleNode;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;

@Data
@Entity
@JsonIgnoreProperties({ "longitude", "latitude", "nearestLong", "nearestLat" })
public class Subscription {
    private @Id ObjectId id;
    private String username;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint primaryLocation;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint stationLocation;

    public Subscription(){}

    public Subscription(String username, double longitude, double latitude) {
        this.username = username;
        this.primaryLocation = new GeoJsonPoint(longitude,latitude);
    }

    public void updateSubscription(Subscription subscription){
        this.username=subscription.username;
        this.primaryLocation=subscription.getPrimaryLocation();
        this.stationLocation=subscription.getStationLocation();
    }
    public double getLongitude(){
        return primaryLocation.getX();
    }
    public double getLatitude(){
        return primaryLocation.getY();
    }
    public void setLongitude(double newX){
        primaryLocation=new GeoJsonPoint(newX,primaryLocation.getY());
    }
    public void setLatitude(double newY){
        primaryLocation=new GeoJsonPoint(primaryLocation.getX(),newY);
    }
    public double getNearestLong(){
        return stationLocation.getX();
    }
    public double getNearestLat(){
        return stationLocation.getY();
    }
    public void setNearestLong(double newX){
        stationLocation = new GeoJsonPoint(newX,stationLocation.getY());
    }
    public void setNearestLat(double newY) {
        stationLocation = new GeoJsonPoint(stationLocation.getX(), newY);
    }

}
