package com.example.demo;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class MetLocation {
    private @Id ObjectId id;
    private double longitude;
    private double latitude;
    private String sourceId;

    public MetLocation(){}

    public MetLocation(String sourceId, double longitude, double latitude){
        this.sourceId=sourceId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
