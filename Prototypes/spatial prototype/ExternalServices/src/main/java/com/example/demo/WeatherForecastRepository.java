package com.example.demo;

import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeatherForecastRepository extends MongoRepository<WeatherForecast, ObjectId> {
}
