package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class LoadDatabase {

    @Autowired
    SubscriptionService subscriptionService;

    @Bean
    CommandLineRunner initDatabase(SubscriptionRepository repository){
        return args -> {
           /* List<Subscription> subscriptions = Arrays.asList(
                    new Subscription("Tor", 0,0),
                    new Subscription("Tor", 10,10),
                    new Subscription("Marius", 2,4),
                    new Subscription("Marius", 10,10),
                    new Subscription("Nils", 10,10),
                    new Subscription("Nils", 0,0),
                    new Subscription("Nils", 3,5),
                    new Subscription("Benjamin", 0,0)
            );

            subscriptions.forEach(s->{
                log.info("Preloading " + subscriptionService.newSubscriptions(s));
            });*/
        };
    }
}
