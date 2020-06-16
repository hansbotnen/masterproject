package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabases(MetLocationRepository repo1){
        return args -> {
            repo1.deleteAll(); //clear before starting
            log.info("Preloading "+ repo1.save(new MetLocation("Station1", 0,0)));
            log.info("Preloading "+ repo1.save(new MetLocation("Station2", 12,6)));
            log.info("Preloading "+ repo1.save(new MetLocation("Station3", -5,-15)));
            log.info("Preloading "+ repo1.save(new MetLocation("Station4", 20,-10)));
            log.info("Preloading "+ repo1.save(new MetLocation("Station5", 2,15)));
        };
    }
}
