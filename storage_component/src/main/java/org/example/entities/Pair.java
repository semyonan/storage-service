package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@EqualsAndHashCode
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Pair<K, V> implements Map.Entry<K, V> {
    @Setter
    private K key;
    private V value;

    public Pair() {}
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }
}
