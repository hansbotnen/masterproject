package model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeatherForecastList implements Serializable {
    List<WeatherForecast> forecasts;

    public WeatherForecastList(){}
    public WeatherForecastList(List<WeatherForecast> forecasts) {
        this.forecasts = forecasts;
    }
}
