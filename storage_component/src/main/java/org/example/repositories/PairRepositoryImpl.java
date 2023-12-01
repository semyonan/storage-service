package org.example.repositories;

import jakarta.annotation.PostConstruct;
import org.example.entities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PairRepositoryImpl implements PairRepository{

    private static final String KEY = "Pair";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    @Autowired
    public PairRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    @Override
    public Pair save(Pair pair) {
        hashOperations.put(KEY, pair.getKey(), pair.getValue());
        return pair;
    }

    @Override
    public Pair getReferenceById(String id) {
        return new Pair(id, hashOperations.get(KEY, id).toString());
    }
}
