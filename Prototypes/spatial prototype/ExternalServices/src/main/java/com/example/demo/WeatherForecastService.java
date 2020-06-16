package com.example.demo;

import model.WeatherForecastList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherForecastService {
    private final WeatherForecastRepository weatherForecastRepository;

    public WeatherForecastService(WeatherForecastRepository weatherForecastRepository) {
        this.weatherForecastRepository = weatherForecastRepository;
    }

    public WeatherForecast getForecast(int date, double longitude, double latitude) {
        WeatherForecast exampleForecast = new WeatherForecast();
        exampleForecast.setDate(date);
        exampleForecast.setLatitude(latitude);
        exampleForecast.setLongitude(longitude);
        Example<WeatherForecast> example = Example.of(exampleForecast,
                ExampleMatcher.matchingAll().withIgnorePaths("id","value"));

        Optional<WeatherForecast> forecast = weatherForecastRepository.findOne(example);
        if(forecast.isPresent()){
            return forecast.get();
        }else {
            WeatherForecast newForecast = WeatherDataFactory.createForecast(date,longitude,latitude);
            return weatherForecastRepository.save(newForecast);
        }
    }

    public WeatherForecastList getForecastsFromDateToDate(int fromDate, int toDate, double longitude, double latitude){
        List<WeatherForecast> forecasts = new ArrayList<>();

        for(int i =fromDate;i<toDate;i++){
            forecasts.add(getForecast(i,longitude,latitude));
        }
        return new WeatherForecastList(forecasts);
    }
}
