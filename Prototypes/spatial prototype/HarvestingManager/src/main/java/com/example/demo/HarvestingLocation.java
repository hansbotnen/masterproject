package com.example.demo;

import Utilities.GeoJsonPointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.persistence.*;

@Data
@Entity
@JsonIgnoreProperties({ "longitude", "latitude","nearestLong","nearestLat" })
public class HarvestingLocation {

    private @Id ObjectId id;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint primaryLocation;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint stationLocation;
    private int count; //number of subscribers

    public HarvestingLocation(){};
    public HarvestingLocation(double px, double py, double nx, double ny){
        primaryLocation = new GeoJsonPoint(px,py);
        stationLocation = new GeoJsonPoint(nx,ny);
        count=1;
    }
    public void increaseCount(){
        count++;
    }
    public void decreaseCount(){
        count--;
    }

    @Override
    public String toString() {
        return "Primary location: ("+getLongitude()+", "+getLatitude()+
                "), nearest station: ("+getNearestLong()+", "+getNearestLat()+")";
    }

    public double getLongitude(){
        return primaryLocation.getX();
    }
    public double getLatitude(){
        return primaryLocation.getY();
    }
    public void setLongitude(double newX){
        if(primaryLocation!=null)
            primaryLocation=new GeoJsonPoint(newX,primaryLocation.getY());
        else
            primaryLocation=new GeoJsonPoint(newX,0);
    }
    public void setLatitude(double newY){
        if (primaryLocation!=null)
            primaryLocation=new GeoJsonPoint(primaryLocation.getX(),newY);
        else
            primaryLocation=new GeoJsonPoint(0,newY);
    }
    public double getNearestLong(){
        return stationLocation.getX();
    }
    public double getNearestLat(){
        return stationLocation.getY();
    }
    public void setNearestLong(double newX){
        if(stationLocation!=null)
            stationLocation = new GeoJsonPoint(newX,stationLocation.getY());
        else
            stationLocation = new GeoJsonPoint(newX,0);
    }
    public void setNearestLat(double newY){
        if(stationLocation!=null)
            stationLocation= new GeoJsonPoint(stationLocation.getX(),newY);
        else
            stationLocation=new GeoJsonPoint(0,newY);
    }

}
