package org.example.config;

import org.example.config.conditions.PairRepositoryLsmCondition;
import org.example.config.conditions.PairRepositoryRedisCondition;
import org.example.repositories.PairRepository;
import org.example.repositories.PairRepositoryLsmImpl;
import org.example.repositories.PairRepositoryRedisImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    @Conditional(PairRepositoryLsmCondition.class)
    public PairRepository getPairRepositoryLsmCondition() {
        return new PairRepositoryLsmImpl();
    }

    @Bean
    @Conditional(PairRepositoryRedisCondition.class)
    public PairRepository getPairRepositoryRedisCondition() {
        return new PairRepositoryRedisImpl();
    }
}
