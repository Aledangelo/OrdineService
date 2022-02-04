package com.greenteam.ordineservice.repository;

import com.greenteam.ordineservice.entity.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface riderRepository extends MongoRepository<Rider, String> {
}
