package com.example.demo;

import model.WeatherMeasurementList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WeatherMeasurementsController {

    private final WeatherMeasurementsService weatherMeasurementService;

    public WeatherMeasurementsController(MetLocationRepository repository, WeatherMeasurementStoredRepository measurementRepository){
        this.weatherMeasurementService=new WeatherMeasurementsService(repository, measurementRepository);
    }

    @GetMapping("/neareststations/{x}/{y}")
    public ResponseEntity<MetLocation> findNearestStation(@PathVariable double x, @PathVariable double y){
        return new ResponseEntity<>(weatherMeasurementService.findNearestMeasurementStation(x,y), HttpStatus.OK);
    }

    @GetMapping("/neareststations")
    public ResponseEntity<List<MetLocation>> allNearestStations(){
        return new ResponseEntity<>(weatherMeasurementService.getAllMeasurementStations(), HttpStatus.OK);
    }

    @GetMapping("/measurements/{date}/{x}/{y}")
    public ResponseEntity<WeatherMeasurement> getMeasurement(@PathVariable int date, @PathVariable double x, @PathVariable double y){
        return new ResponseEntity<>(weatherMeasurementService.getMeasurement(date,x,y), HttpStatus.OK);
    }

    @GetMapping("/measurements/{fromDate}/{toDate}/{x}/{y}")
    public ResponseEntity<WeatherMeasurementList> getMeasurementsFromDateToDate(
            @PathVariable int fromDate, @PathVariable int toDate, @PathVariable double x, @PathVariable double y){
        WeatherMeasurementList measurements;
        measurements = weatherMeasurementService.getMeasurementsFromDateToDate(fromDate,toDate,x,y);
        return new ResponseEntity<>(measurements,HttpStatus.OK);
    }

}
