package com.example.demo;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Subscription {
    private @Id ObjectId id;
    private String username;
    private double longitude;
    private double latitude;
    private double nearestLat;
    private double nearestLong;

    public Subscription(){}

    public Subscription(String username, double longitude, double latitude) {
        this.username = username;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void updateSubscription(Subscription subscription){
        this.username=subscription.username;
        this.longitude=subscription.longitude;
        this.latitude=subscription.latitude;
        this.nearestLat=subscription.nearestLat;
        this.nearestLong=subscription.nearestLong;
    }
}
