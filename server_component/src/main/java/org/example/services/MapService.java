package org.example.services;

import org.example.dbProcessing.InMemoryDb;
import org.example.entities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    @Autowired
    InMemoryDb repository;

    public Pair<String, String> addPair(String key, String value) {
        repository.set(key, value);
        return new Pair<String, String>(key, value);
    }

    public Pair<String, String> getPair(String key) {
        var value = repository.get(key);
        return new Pair<String, String>(key, value);
    }
}
