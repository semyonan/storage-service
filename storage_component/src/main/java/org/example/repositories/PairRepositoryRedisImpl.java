package org.example.repositories;

import jakarta.annotation.PostConstruct;
import org.example.entities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

public class PairRepositoryRedisImpl implements PairRepository{

    private static final String KEY = "Pair";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    @Override
    public void save(Pair<String, String> pair) {
        hashOperations.put(KEY, pair.getKey(), pair.getValue());
    }

    @Override
    public Pair<String, String> getReferenceById(String id) {
        return new Pair<>(id,hashOperations.get(KEY, id).toString());
    }
}
