package com.example.demo;

import Model.SubscriberList;
import Utilities.GlobalVariables;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MessageBrokerService {

    private final SseEmitters emitters = new SseEmitters();

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ReceivedPredictionRepository receivedPredictionRepository;

    @Autowired
    FireRiskPredictionRepository fireRiskPredictionRepository;

    @Autowired
    SubscriptionRepository subscriberRepository;

    public MessageBrokerService(){
    }

    public Subscription addSubscription(Subscription subscription) {
        Subscription exchange =
                restTemplate.postForObject(
                        GlobalVariables.SUBSCRIPTION_MANAGER_URL+"/subscriptions",
                        subscription,
                        Subscription.class);

        //find all previously received predictions for location
        List<ReceivedPrediction> receivedPredictions = receivedPredictionRepository.findByPredictionLocation(
                        subscription.getLongitude(),
                        subscription.getLatitude());

        //find the newest received prediction for location
        Optional<ReceivedPrediction> newestReceivedPrediction = receivedPredictions.stream()
                .max(Comparator.comparing(rp -> rp.getPrediction().getDay()));
        //if it exists check whether subscriber has received it before
        if(newestReceivedPrediction.isPresent()){
            ReceivedPrediction receivedPrediction = newestReceivedPrediction.get();
            boolean containsSubscriber =receivedPrediction.getSubscribers()
                    .stream().anyMatch(s->s.getUsername().equals(exchange.getUsername()));

            //if not, send the prediction to the subscriber and save as having received it
            if(!containsSubscriber){
                PredictionMSG msg = new PredictionMSG(
                        exchange.getUsername(),
                        receivedPrediction.getPrediction().getDay(),
                        receivedPrediction.getPrediction().getRisk(),
                        exchange.getLongitude(),
                        exchange.getLatitude());
                emitters.send(msg);
                receivedPrediction.addSubscriber(exchange);
                receivedPredictionRepository.save(receivedPrediction);
            }
        }
        return exchange;
    }

    public void removeSubscription(ObjectId id) {
        restTemplate.delete(GlobalVariables.SUBSCRIPTION_MANAGER_URL+"/subscriptions"
                +"/"+id);
    }


    public void unsubscribe(Subscription subscription) {
        System.out.println("attempting to delete " + subscription);
        restTemplate.exchange(GlobalVariables.SUBSCRIPTION_MANAGER_URL+"/subscriptions",
                HttpMethod.DELETE,
                new HttpEntity<Subscription>(subscription),
                void.class);
    }


    @Transactional
    public void receivePrediction(FireRiskPrediction prediction) {
        List<Subscription> subscriberList = restTemplate.getForObject(
                GlobalVariables.SUBSCRIPTION_MANAGER_URL+
                        "/subscriptions"+
                        "/"+prediction.getPrimaryX()+
                        "/"+prediction.getPrimaryY()+
                        "/"+prediction.getStationX()+
                        "/"+prediction.getStationY(), SubscriberList.class).getSubscribers();

        ReceivedPrediction receivedPrediction = new ReceivedPrediction();
        for(Subscription sub:subscriberList){
            Example<Subscription> example = Example.of(sub, ExampleMatcher.matchingAll().withIgnorePaths("id"));
            Optional<Subscription> existing = subscriberRepository.findOne(example);
            if (existing.isPresent()) {
                receivedPrediction.addSubscriber(existing.get());
            }else{
                receivedPrediction.addSubscriber(subscriberRepository.save(sub));
            }
        }

        receivedPrediction.setPrediction(prediction);
        receivedPredictionRepository.save(receivedPrediction);

        //send messages to the subscribers
        for(Subscription sub: receivedPrediction.getSubscribers()){
            PredictionMSG msg = new PredictionMSG(
                    sub.getUsername(),
                    prediction.getDay(),
                    prediction.getRisk(),
                    sub.getLongitude(),
                    sub.getLatitude());
            emitters.send(msg);
        }

        System.out.print("Received prediction " + receivedPrediction.getPrediction() + " for subscribers: ");
        receivedPrediction.getSubscribers().forEach(s-> System.out.print(s.getUsername()+ ", "));
        System.out.println();
    }

    public List<ReceivedPrediction> getReceivedPredictions() {
        return receivedPredictionRepository.findAll();
    }

    public List<Subscription> getSubscribers() {
        return subscriberRepository.findAll();
    }

    public SseEmitter getEmitter() {
        return emitters.add(new SseEmitter(-1L));
    }
}
