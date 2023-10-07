package org.example.repositories;

import org.example.entities.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairRepository extends JpaRepository<Pair, String> {
}
