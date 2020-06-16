package com.example.demo;

import lombok.Data;

@Data
public class SubscriptionWrapper {
    private String username;
    private double longitude;
    private double latitude;

    public SubscriptionWrapper(){
    }
    public SubscriptionWrapper(String username, double longitude, double latitude){
        this.username=username;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Subscription getSubscription(){
        return new Subscription(username,longitude,latitude);
    }
}
