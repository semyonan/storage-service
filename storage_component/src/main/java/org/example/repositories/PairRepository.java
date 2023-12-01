package org.example.repositories;

import org.example.entities.Pair;

public interface PairRepository {
    Pair save(Pair pair);
    Pair getReferenceById(String id);
}
