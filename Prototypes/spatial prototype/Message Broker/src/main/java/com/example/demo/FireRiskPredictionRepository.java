package com.example.demo;

import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FireRiskPredictionRepository extends MongoRepository<FireRiskPrediction, ObjectId> {
}
