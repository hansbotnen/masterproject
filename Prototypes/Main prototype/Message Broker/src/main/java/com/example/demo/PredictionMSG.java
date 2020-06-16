package com.example.demo;

import lombok.Data;

@Data
public class PredictionMSG {
    private String userName;
    private double longitude;
    private double latitude;
    private int day;
    private double risk;

    public PredictionMSG(String userName , int day, double risk, double longitude, double latitude) {
        this.userName = userName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.day = day;
        this.risk = risk;
    }

    public PredictionMSG(){}
}
