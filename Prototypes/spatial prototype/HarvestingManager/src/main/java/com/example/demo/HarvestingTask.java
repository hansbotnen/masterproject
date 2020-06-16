package com.example.demo;

import Utilities.GlobalVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


//ikke i bruk atm
@Component
public class HarvestingTask implements Runnable {

    private String externalServicesURL = "http://external-services";
    private String riskPredictionURL = "http://risk-prediction";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HarvestingService harvestingService;

    @Override
    public void run() {
        List<HarvestingLocation> locations = harvestingService.getAllHarvestingLocations();
        for(HarvestingLocation location : locations){
            double primaryX = location.getLongitude();
            double primaryY = location.getLatitude();
            double stationX = location.getNearestLong();
            double stationY = location.getNearestLat();

            String forecastRequest = externalServicesURL
                    +"/forecasts"
                    +"/"+ (GlobalVariables.CURRENTDAY+1)
                    +"/"+(GlobalVariables.CURRENTDAY+1+GlobalVariables.WEATHERWINDOW_FUTURE)
                    +"/"+primaryX
                    +"/"+primaryY;
            ResponseEntity<?> forecastResponse = restTemplate.getForEntity(forecastRequest,Object.class);
            String forecastStorageURL = riskPredictionURL+"/multipleforecasts";
            restTemplate.postForEntity(forecastStorageURL, forecastResponse.getBody(),Object.class);

            String measurementRequest = externalServicesURL
                    +"/measurements"
                    +"/"+(GlobalVariables.CURRENTDAY-GlobalVariables.WEATHERWINDOW_PAST)
                    +"/"+GlobalVariables.CURRENTDAY
                    +"/"+stationX
                    +"/"+stationY;
            ResponseEntity<?> measurementResponse = restTemplate.getForEntity(measurementRequest,Object.class);
            String measurementStorageURL = riskPredictionURL+"/multiplemeasurements";
            restTemplate.postForEntity(measurementStorageURL,measurementResponse.getBody(),Object.class);
        }

    }
}
