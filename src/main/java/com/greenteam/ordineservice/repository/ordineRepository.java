package com.greenteam.ordineservice.repository;

import com.greenteam.ordineservice.entity.Ordine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ordineRepository extends MongoRepository<Ordine, String> {
}
