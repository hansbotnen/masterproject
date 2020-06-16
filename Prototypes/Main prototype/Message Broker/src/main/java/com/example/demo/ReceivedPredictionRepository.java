package com.example.demo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReceivedPredictionRepository extends MongoRepository<ReceivedPrediction, ObjectId>{
    @Query("{'prediction.primaryX':?0, 'prediction.primaryY':?1}")
    public List<ReceivedPrediction> findByPredictionLocation(double x, double y);
}
