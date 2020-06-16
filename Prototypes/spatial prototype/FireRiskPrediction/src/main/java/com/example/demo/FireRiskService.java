package com.example.demo;

import Utilities.GlobalVariables;
import model.HarvestingLocation;
import model.HarvestingLocationList;
import model.WeatherForecastList;
import model.WeatherMeasurementList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FireRiskService {

    @Autowired
    RestTemplate restTemplate;

    private final WeatherMeasurementRepository measurementRepository;
    private final WeatherForecastRepository forecastRepository;
    private final FireRiskPredictionRepository riskRepository;

    public FireRiskService(WeatherMeasurementRepository measurementRepository, WeatherForecastRepository forecastRepository, FireRiskPredictionRepository fireRiskPredictionRepository) {
        this.measurementRepository = measurementRepository;
        this.forecastRepository= forecastRepository;
        this.riskRepository = fireRiskPredictionRepository;
    }

    public WeatherMeasurement storeMeasurement(WeatherMeasurement measurement) {
        return measurementRepository.save(measurement);
    }

    public WeatherForecast storeForecast(WeatherForecast forecast) {
        return forecastRepository.save(forecast);
    }

    public List<WeatherMeasurement> getMeasurements() {
        return measurementRepository.findAll();
    }

    public List<WeatherForecast> getForecasts() {
        return forecastRepository.findAll();
    }


    //bør få de til å svare..
    public Object storeMeasurements(WeatherMeasurementList body) {
        body.getMeasurements().forEach(wm ->{
            Optional<WeatherMeasurement> existing = measurementRepository.findOne(
                    Example.of(wm,ExampleMatcher.matching().withIgnorePaths("id","value")));
            if(!existing.isPresent()){
                measurementRepository.save(wm);
            }
        });

        return "";
    }
    public Object storeForecasts(WeatherForecastList body) {
        body.getForecasts().forEach(wf -> {
            Optional<WeatherForecast> existing = forecastRepository.findOne(
                    Example.of(wf, ExampleMatcher.matching().withIgnorePaths("id","value")));
            if(!existing.isPresent()){
                forecastRepository.save(wf);
            }
        });
        return "";
    }

    public void createPredictions(int day) {
        List<HarvestingLocation> locationList = restTemplate.getForObject(
                GlobalVariables.HARVESTING_MANAGER_URL+"/harvestinglocations",
                    HarvestingLocationList.class).getLocations();

        for(HarvestingLocation location:locationList){
            //first see if there already is a prediction for this location on this day
            FireRiskPrediction examplePrediction = new FireRiskPrediction();
            examplePrediction.setDay(day);
            examplePrediction.setPrimaryY(location.getLatitude());
            examplePrediction.setPrimaryX(location.getLongitude());
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "risk", "stationX", "stationY");
            Example<FireRiskPrediction> example = Example.of(examplePrediction,matcher);
            if(riskRepository.exists(example)) continue; //continue to the next location if the prediction exists

            //Then try to create the prediction and send to message broker
            List<WeatherMeasurement> measurements = findMeasurementsForPrediction(day,location);
            List<WeatherForecast> forecasts = findForecastsForPrediction(day,location);
            if (measurements ==null || forecasts == null){
                System.out.println("Could not create prediction for harvesting location " + location + "because of missing weather data");
            }else{
                FireRiskPrediction prediction = createFireRiskPrediction(day,location,measurements,forecasts);
                System.out.println("Created prediction " + prediction);
                riskRepository.save(prediction);

                restTemplate.postForEntity(GlobalVariables.MESSAGE_BROKER_URL+"/predictions",prediction,String.class);

            }
        }
    }

    private FireRiskPrediction createFireRiskPrediction(int day, HarvestingLocation location, List<WeatherMeasurement> measurements, List<WeatherForecast> forecasts) {
        double measurementsValue = measurements.stream().mapToDouble(m->m.getValue()).average().getAsDouble();
        double forecastsValue = forecasts.stream().mapToDouble(f->f.getValue()).average().getAsDouble();
        double risk = (measurementsValue+forecastsValue)/2; //risk er bare gjennomsnittet av measurements og forecasts
        return new FireRiskPrediction(
                day,
                risk,
                location.getLongitude(),
                location.getLatitude(),
                location.getNearestLong(),
                location.getNearestLat()
        );
    }

    private List<WeatherMeasurement> findMeasurementsForPrediction(int date, HarvestingLocation location) {
        List<WeatherMeasurement> weatherMeasurements = new ArrayList<>();
        WeatherMeasurement exampleMeasurement = new WeatherMeasurement();
        exampleMeasurement.setLongitude(location.getNearestLong());
        exampleMeasurement.setLatitude(location.getNearestLat());

        for(int i=date-GlobalVariables.WEATHERWINDOW_PAST;i<date;i++){
            exampleMeasurement.setDate(i);
            Example<WeatherMeasurement> example = Example.of(
                    exampleMeasurement,
                    ExampleMatcher.matching().withIgnorePaths("id","value"));
            Optional<WeatherMeasurement> measurement = measurementRepository.findOne(example);
            if(measurement.isPresent()){
                weatherMeasurements.add(measurement.get());
            }else{
                return null;
            }
        }
        return weatherMeasurements;
    }

    private List<WeatherForecast> findForecastsForPrediction(int date, HarvestingLocation location){
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        WeatherForecast exampleForecast = new WeatherForecast();
        exampleForecast.setLongitude(location.getLongitude());
        exampleForecast.setLatitude(location.getLatitude());

        for(int i=date+1;i<date+GlobalVariables.WEATHERWINDOW_FUTURE;i++){
            exampleForecast.setDate(i);
            Example<WeatherForecast> example = Example.of(
                    exampleForecast,
                    ExampleMatcher.matching().withIgnorePaths("id","value"));
            Optional<WeatherForecast> forecast = forecastRepository.findOne(example);
            if(forecast.isPresent()){
                weatherForecasts.add(forecast.get());
            }else {
                return null;
            }

        }
        return weatherForecasts;
    }


}
