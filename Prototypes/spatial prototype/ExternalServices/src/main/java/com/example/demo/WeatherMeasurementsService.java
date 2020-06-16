package com.example.demo;

import model.WeatherMeasurementList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherMeasurementsService {

    private final MetLocationRepository stationRepository;
    private final WeatherMeasurementStoredRepository measurementRepository;

    public WeatherMeasurementsService(MetLocationRepository stationRepository, WeatherMeasurementStoredRepository measurementRepository){
        this.stationRepository=stationRepository;
        this.measurementRepository = measurementRepository;
    }

    public WeatherMeasurementList getMeasurementsFromDateToDate(int fromDate, int toDate, double longitude, double latitude){
        List<WeatherMeasurement> measurements = new ArrayList<>();
        for(int i = fromDate;i<toDate;i++)
            measurements.add(getMeasurement(i,longitude,latitude));
        return new WeatherMeasurementList(measurements);
    }

    public WeatherMeasurement getMeasurement(int date, double longitude, double latitude){
        MetLocation location = findNearestMeasurementStation(longitude,latitude);
        WeatherMeasurementStored exampleMeasurement = new WeatherMeasurementStored();
        exampleMeasurement.setStation(location);
        exampleMeasurement.setDate(date);
        Example<WeatherMeasurementStored> example = Example.of(exampleMeasurement,
                ExampleMatcher.matching().withIgnorePaths("id","value"));
        Optional<WeatherMeasurementStored> measurement= measurementRepository.findOne(example);
        if(measurement.isPresent()){
            return measurement.get().getMeasurement();
        }else{
            WeatherMeasurementStored newMeasurement = WeatherDataFactory.createMeasurement(date,location);
            return measurementRepository.save(newMeasurement).getMeasurement();
        }

    }

    public MetLocation findNearestMeasurementStation(double x, double y){
        List<MetLocation> stations = getAllMeasurementStations();

        MetLocation nearestStation = stations.get(0);
        double distance = findDistance(nearestStation, x,y);
        for(MetLocation ms : stations){
            if(findDistance(ms,x,y)<distance)
                nearestStation=ms;
        }
        System.out.println("Found nearest station for ("+x+", " + y + "): "+ nearestStation);
        return nearestStation;
    }

    private double findDistance(MetLocation nearestStation, double x, double y) {
        return Point2D.distance(x,y,nearestStation.getLongitude(),nearestStation.getLatitude());
    }


    public List<MetLocation> getAllMeasurementStations(){
        return stationRepository.findAll();
    }
}
