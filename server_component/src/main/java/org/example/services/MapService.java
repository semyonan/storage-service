package org.example.services;

import org.example.entities.Pair;
import org.example.repositories.PairRepository;
import org.example.repositories.PairRepositoryLsmImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    @Autowired
    PairRepository repository;

    public Pair<String, String> addPair(String key, String value) {
        Pair<String, String> pair =  new Pair<>(key, value);
        repository.save(pair);
        return pair;
    }

    public Pair<String, String> getPair(String key) {
        var pair = repository.getReferenceById(key);
        return pair;
    }
}
