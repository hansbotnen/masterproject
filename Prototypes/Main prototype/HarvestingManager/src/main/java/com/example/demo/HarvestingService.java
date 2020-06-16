package com.example.demo;

import Utilities.GlobalVariables;
import com.netflix.discovery.converters.Auto;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class HarvestingService {

    @Autowired
    RestTemplate restTemplate;

    private final HarvestingLocationRepository repository;

    public HarvestingService(HarvestingLocationRepository repository){
        this.repository=repository;
    }
    public List<HarvestingLocation> getAllHarvestingLocations() {
        return repository.findAll();
    }

    //Increases count if location is already saved, if not saves it
    public HarvestingLocation addLocation(double px, double py, double nx, double ny) {
        HarvestingLocation hl;
        Optional<HarvestingLocation> existinghl = findExistingHarvestingLocation(px,py,nx,ny);
        if(existinghl.isPresent()) {
            hl = existinghl.get();
            hl.increaseCount();
        }else{
            hl = new HarvestingLocation(px,py,nx,ny);
            hl.setCount(1);
        }
        return repository.save(hl);
    }

    //finds harvesting location, decreases count and removes it if count is 0.
    public HarvestingLocation removeLocation(double px, double py, double nx, double ny){
        Optional<HarvestingLocation> existinghl = findExistingHarvestingLocation(px,py,nx,ny);
        if(existinghl.isPresent()){
            HarvestingLocation hl = existinghl.get();
            System.out.println(hl);
            if(hl.getCount()>1) {
                hl.decreaseCount();
                return repository.save(hl);
            }else{
                repository.delete(hl);
                hl.decreaseCount();
                return hl;
            }
        }
        return null;
    }

    private Optional<HarvestingLocation> findExistingHarvestingLocation(double px, double py, double nx, double ny) {
        HarvestingLocation hl = new HarvestingLocation();
        hl.setLongitude(px);
        hl.setLatitude(py);
        hl.setNearestLong(nx);
        hl.setNearestLat(ny);

        Example<HarvestingLocation> example = Example.of(hl, ExampleMatcher.matchingAll().withIgnorePaths("count"));
        return repository.findOne(example);
    }

    public void performHarvesting(int day){
        List<HarvestingLocation> locations = getAllHarvestingLocations();
        for(HarvestingLocation location : locations) {
            double primaryX = location.getLongitude();
            double primaryY = location.getLatitude();
            double stationX = location.getNearestLong();
            double stationY = location.getNearestLat();

            String forecastRequest = GlobalVariables.EXTERNAL_SERVICES_URL
                    + "/forecasts"
                    + "/" + (day)
                    + "/" + (day + GlobalVariables.WEATHERWINDOW_FUTURE)
                    + "/" + primaryX
                    + "/" + primaryY;
            ResponseEntity<WeatherForecastList> forecastResponse = restTemplate.getForEntity(forecastRequest, WeatherForecastList.class);
            String forecastStorageURL = GlobalVariables.RISK_PREDICTION_URL + "/multipleforecasts";
            restTemplate.postForEntity(forecastStorageURL, forecastResponse.getBody(), ResponseEntity.class);

            String measurementRequest = GlobalVariables.EXTERNAL_SERVICES_URL
                    + "/measurements"
                    + "/" + (day - GlobalVariables.WEATHERWINDOW_PAST)
                    + "/" + day
                    + "/" + stationX
                    + "/" + stationY;
            ResponseEntity<WeatherMeasurementList> measurementResponse;
            measurementResponse = restTemplate.getForEntity(measurementRequest, WeatherMeasurementList.class);
            String measurementStorageURL = GlobalVariables.RISK_PREDICTION_URL + "/multiplemeasurements";
            restTemplate.postForEntity(measurementStorageURL, measurementResponse.getBody(), ResponseEntity.class);
        }
    }


    public HarvestingLocationList getHarvestingLocationList() {
        return new HarvestingLocationList(getAllHarvestingLocations());
    }
}
