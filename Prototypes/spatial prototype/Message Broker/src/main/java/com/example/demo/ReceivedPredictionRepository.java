package com.example.demo;

import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReceivedPredictionRepository extends MongoRepository<ReceivedPrediction, ObjectId>{
    @Query("{'prediction.primaryLocation.coordinates' : {$all : [?0]}}")
    public List<ReceivedPrediction> findByPredictionLocation(double[] coords);
}
