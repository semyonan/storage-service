package org.example.repositories;

import org.example.entities.Pair;
import org.springframework.stereotype.Repository;

public interface PairRepository {
    void save(Pair<String, String> pair);
    Pair getReferenceById(String id);
}
