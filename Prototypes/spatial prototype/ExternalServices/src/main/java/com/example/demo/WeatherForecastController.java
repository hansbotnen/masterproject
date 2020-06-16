package com.example.demo;

import model.WeatherForecastList;
import model.WeatherMeasurementList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    public WeatherForecastController(WeatherForecastRepository weatherForecastRepository){
        this.weatherForecastService = new WeatherForecastService(weatherForecastRepository);
    }

    @GetMapping("/forecasts/{date}/{x}/{y}")
    public ResponseEntity<WeatherForecast> getForecast(@PathVariable int date, @PathVariable double x, @PathVariable double y){
        return new ResponseEntity<>(weatherForecastService.getForecast(date, x,y), HttpStatus.OK);
    }

    @GetMapping("/forecasts/{fromDate}/{toDate}/{x}/{y}")
    public ResponseEntity<WeatherForecastList> getForecastsFromDateToDate(
            @PathVariable int fromDate, @PathVariable int toDate, @PathVariable double x, @PathVariable double y){
        WeatherForecastList forecasts = weatherForecastService.getForecastsFromDateToDate(fromDate,toDate,x,y);
        return new ResponseEntity<>(forecasts,HttpStatus.OK);
    }
}
