package org.example.services;

import org.example.entities.Pair;
import org.example.repositories.PairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    @Autowired
    PairRepository repository;

    public Pair addPair(String key, String value) {
        repository.save(new Pair(key, value));
        return new Pair(key, value);
    }

    public Pair getPair(String key) {
        var pair = repository.getReferenceById(Long.parseLong(key));
        return pair;
    }
}
