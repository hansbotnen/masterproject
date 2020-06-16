package com.example.demo;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MessageBrokerController {

    @Autowired
    MessageBrokerService messageBrokerService;

    public MessageBrokerController(){}

    @PostMapping("/subscriptions")
    Subscription subscribe(@RequestBody Subscription subscription){
        return messageBrokerService.addSubscription(subscription);
    }
    @DeleteMapping("/subscriptions/{id}")
    void removeSubscription(@PathVariable ObjectId id){
        messageBrokerService.removeSubscription(id);
    }

    @DeleteMapping("/subscriptions")
    void unsubscribe(@RequestBody Subscription subscription){
        messageBrokerService.unsubscribe(subscription);
    }

    @PostMapping("/predictions")
    void addPrediction(@RequestBody FireRiskPrediction prediction){
        messageBrokerService.receivePrediction(prediction);
    }

    @GetMapping("/receivedpredictions")
    List<ReceivedPrediction> getpredictions(){
        return messageBrokerService.getReceivedPredictions();
    }

    @GetMapping("/receivedpredictionssubscriber")
    List<Subscription> getSubscribers(){
        return messageBrokerService.getSubscribers();
    }

    @GetMapping(path = "/predictionstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter getPerformance() {
        return messageBrokerService.getEmitter();
    }
}
