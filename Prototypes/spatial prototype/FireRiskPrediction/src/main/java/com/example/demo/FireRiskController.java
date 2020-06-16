package com.example.demo;

import model.WeatherForecastList;
import model.WeatherMeasurementList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FireRiskController {

    @Autowired
    FireRiskService fireRiskService;

    public FireRiskController(){
    }

    @PostMapping("/multiplemeasurements")
    public ResponseEntity<Object> storeMeasurement(@RequestBody WeatherMeasurementList measurements){
        return new ResponseEntity<>(fireRiskService.storeMeasurements(measurements), HttpStatus.OK);
    }

    @PostMapping("/multipleforecasts")
    public ResponseEntity<Object> storeForecast(@RequestBody WeatherForecastList forecasts){
        return new ResponseEntity<>(fireRiskService.storeForecasts(forecasts),HttpStatus.OK);
    }

    @GetMapping("/measurements")
    public ResponseEntity<Object> getMeasurements(){
        return new ResponseEntity<>(fireRiskService.getMeasurements(),HttpStatus.OK);
    }

    @GetMapping("/forecasts")
    public ResponseEntity<Object> getForecasts(){
        return new ResponseEntity<>(fireRiskService.getForecasts(),HttpStatus.OK);
    }

    @PostMapping("/tasks/createpredictions/{day}")
    public void createPredictions(@PathVariable int day){
        fireRiskService.createPredictions(day);
    }
}
