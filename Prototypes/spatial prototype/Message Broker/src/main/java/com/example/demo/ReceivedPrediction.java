package com.example.demo;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class ReceivedPrediction {
    private @Id ObjectId id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="prediction_id", referencedColumnName = "id")
    private FireRiskPrediction prediction;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="subscriber_id", referencedColumnName = "id")
    private Set<Subscription> subscribers;

    public ReceivedPrediction(){
        this.subscribers = new HashSet<>();
    }
    public ReceivedPrediction(FireRiskPrediction prediction, Set<Subscription> subscribers){
        this.prediction=prediction;
        this.subscribers=subscribers;
    }
    public void addSubscriber(Subscription subscription){
        subscribers.add(subscription);
    }
    public void removeSubscriber(Subscription subscription){
        if(subscribers.contains(subscription))
            subscribers.remove(subscription);
    }
}
