package com.example.demo;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class FireRiskPrediction {
    private @Id ObjectId id;
    private int day;
    private double risk; // en skala fra 1-10
    private double primaryX;
    private double primaryY;
    private double stationX;
    private double stationY;

    public FireRiskPrediction(){}

    public FireRiskPrediction(int day, double risk, double primaryX, double primaryY, double stationX, double stationY) {
        this.day = day;
        this.risk = risk;
        this.primaryX = primaryX;
        this.primaryY = primaryY;
        this.stationX = stationX;
        this.stationY = stationY;
    }


}
