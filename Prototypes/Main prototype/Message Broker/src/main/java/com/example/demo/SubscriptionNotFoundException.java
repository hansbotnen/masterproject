package com.example.demo;

import org.bson.types.ObjectId;

public class SubscriptionNotFoundException extends RuntimeException {
    SubscriptionNotFoundException(ObjectId id){
        super("Could not find subscription " + id);
    }
}
