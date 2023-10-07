package org.example.services;

import org.example.entities.Pair;
import org.example.repositories.PairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    @Autowired
    PairRepository pairRepository;
    public Pair addPair(String key, String value) {
        Pair newPair = new Pair(key, value);
        return pairRepository.save(newPair);
    }

    public Pair getPair(String key) {
        return pairRepository.getReferenceById(key);
    }
}
