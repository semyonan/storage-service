package org.example.entities;

import lombok.Getter;

import java.util.Map;

@Getter
public class Pair<K, V> implements Map.Entry<K, V> {

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