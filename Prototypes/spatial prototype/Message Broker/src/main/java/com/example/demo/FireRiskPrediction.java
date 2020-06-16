package com.example.demo;

import Utilities.GeoJsonPointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties({ "primaryX", "primaryY", "stationX", "stationY"})
public class FireRiskPrediction {
    private @Id ObjectId id;
    private int day;
    private double risk; // en skala fra 1-10

    @GeoSpatialIndexed
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint primaryLocation;
    @JsonDeserialize(using= GeoJsonPointDeserializer.class)
    private GeoJsonPoint stationLocation;

    public FireRiskPrediction(){}

    public FireRiskPrediction(int day, double risk, double primaryX, double primaryY, double stationX, double stationY) {
        this.day = day;
        this.risk = risk;
        this.primaryLocation = new GeoJsonPoint(primaryX,primaryY);
        this.stationLocation = new GeoJsonPoint(stationX,stationY);
    }
    public double getPrimaryX(){
        return primaryLocation.getX();
    }
    public double getPrimaryY(){
        return primaryLocation.getY();
    }
    public void setPrimaryX(double newX){
        if(primaryLocation!= null)
            primaryLocation=new GeoJsonPoint(newX,primaryLocation.getY());
        else
            primaryLocation=new GeoJsonPoint(newX,0);
    }
    public void setPrimaryY(double newY){
        if(primaryLocation!= null)
            primaryLocation=new GeoJsonPoint(primaryLocation.getX(),newY);
        else
            primaryLocation=new GeoJsonPoint(0,newY);
    }
    public double getStationX(){
        return stationLocation.getX();
    }
    public double getStationY(){
        return stationLocation.getY();
    }
    public void setStationX(double newX){
        if(stationLocation!=null)
            stationLocation = new GeoJsonPoint(newX,stationLocation.getY());
        else
            stationLocation=new GeoJsonPoint(newX,0);
    }
    public void setStationY(double newY){
        if(stationLocation!=null)
            stationLocation= new GeoJsonPoint(stationLocation.getX(),newY);
        else
            stationLocation=new GeoJsonPoint(0,newY);
    }


}