package com.example.demo;

import Utilities.GlobalVariables;
import model.MetLocation;
import model.SubscriberList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    RestTemplate restTemplate;

    public SubscriptionService(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository;
    }



    public List<Subscription> getAll(){
        return subscriptionRepository.findAll();
    }

    Subscription newSubscriptions(Subscription subscription){
        double latitude=subscription.getLatitude();
        double longitude=subscription.getLongitude();

        //find nearest station and update coordinates
        MetLocation nearestStation = restTemplate.getForObject(
                GlobalVariables.EXTERNAL_SERVICES_URL+"/neareststations/"+longitude+"/"+latitude, MetLocation.class);
        subscription.setNearestLong(nearestStation.getLongitude());
        subscription.setNearestLat(nearestStation.getLatitude());

        //notify harvesting manager about location
        String requestURL=createHarvestingLocationRequestURL(subscription);
        ResponseEntity<String> response = restTemplate.postForEntity(requestURL,null,String.class);

        return subscriptionRepository.save(subscription);
    }

    private String createHarvestingLocationRequestURL(Subscription subscription) {
        return GlobalVariables.HARVESTING_MANAGER_URL+"/harvestinglocations"+
                "/"+subscription.getLongitude()+
                "/"+subscription.getLatitude()+
                "/"+subscription.getNearestLong()+
                "/"+subscription.getNearestLat();
    }

    Subscription getSubscription(ObjectId id) throws SubscriptionNotFoundException{
        Optional<Subscription> sub = subscriptionRepository.findById(id);
        if(sub.isPresent())
            return sub.get();
        else
            throw new SubscriptionNotFoundException(id);

    }

    //does not get nearest station or notify harvesting manager
    Subscription updateSubscription(ObjectId id, Subscription newSub){
        Optional<Subscription> sub = subscriptionRepository.findById(id);
        if(sub.isPresent()) {
            Subscription subscription = sub.get();
            subscription.updateSubscription(newSub);
            return subscriptionRepository.save(subscription);
        }
        else{
            newSub.setId(id);
            return subscriptionRepository.save(newSub);
        }

    }

    void deleteSubscription(ObjectId id){
        Optional<Subscription> subOptional= subscriptionRepository.findById(id);
        if(subOptional.isPresent()) {
            //notify harvesting manager about location
            Subscription subscription= subOptional.get();
            String requestURL = createHarvestingLocationRequestURL(subscription);
            restTemplate.delete(requestURL);

            //delete from repository
            subscriptionRepository.deleteById(id);
        }
    }


    public void removeSubscription(Subscription subscription) {
        System.out.println("attempting to delete "+ subscription);
        Example<Subscription> example = Example.of(subscription,
                ExampleMatcher.matching().withIgnorePaths("id","nearestLong","nearestLat"));
        Optional<Subscription> subOptional = subscriptionRepository.findOne(example);
        if(subOptional.isPresent()){
            Subscription sub= subOptional.get();
            String requestURL = createHarvestingLocationRequestURL(sub);
            restTemplate.delete(requestURL);

            //delete from repository
            subscriptionRepository.delete(sub);
        }
    }

    public SubscriberList getSubscribersToLocation(double x1, double y1, double x2, double y2) {
        Subscription exampleSubscription = new Subscription();
        exampleSubscription.setLongitude(x1);
        exampleSubscription.setLatitude(y1);
        exampleSubscription.setNearestLong(x2);
        exampleSubscription.setNearestLat(y2);
        Example<Subscription> example = Example.of(exampleSubscription, ExampleMatcher.matching().withIgnorePaths("id", "username"));
        List<Subscription> subscriptions = subscriptionRepository.findAll(example);
        return new SubscriberList(subscriptions);
    }
}
