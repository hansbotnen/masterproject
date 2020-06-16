package com.example.demo;


import model.SubscriberList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    RestTemplate restTemplate;

    public SubscriptionController() {
    }

    @GetMapping("/subscriptions")
    ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(subscriptionService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/subscriptions")
    ResponseEntity<Object> newSubscriptions(@RequestBody Subscription subscription){
        try{
            return new ResponseEntity<>(subscriptionService.newSubscriptions(subscription), HttpStatus.OK);
        }catch(SubscriptionNotFoundException e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/subscriptions/{id}")
    ResponseEntity<Object> getSubscription(@PathVariable ObjectId id){
        Subscription sub = subscriptionService.getSubscription(id);
        return new ResponseEntity<>(sub,HttpStatus.OK);
    }

    @PutMapping("/subscriptions/{id}")
    ResponseEntity<Object> updateSubscription(@PathVariable ObjectId id, @RequestBody Subscription newSub){
        return new ResponseEntity<>(subscriptionService.updateSubscription(id,newSub), HttpStatus.OK);
    }

    @DeleteMapping("/subscriptions/{id}")
    ResponseEntity<Object> deleteSubscription(@PathVariable ObjectId id){
        subscriptionService.deleteSubscription(id);
        return new ResponseEntity<>("Deleted subscription " + id, HttpStatus.OK);
    }

    @DeleteMapping("/subscriptions")
    void removeSubscription(@RequestBody Subscription subscription){
        subscriptionService.removeSubscription(subscription);
    }

    @GetMapping("/subscriptions/{x1}/{y1}/{x2}/{y2}")
    ResponseEntity<SubscriberList> getSubscribersToLocation(@PathVariable double x1, @PathVariable double y1, @PathVariable double x2, @PathVariable double y2){
        return new ResponseEntity<>(subscriptionService.getSubscribersToLocation(x1,y1,x2,y2),HttpStatus.OK);
    }
}
