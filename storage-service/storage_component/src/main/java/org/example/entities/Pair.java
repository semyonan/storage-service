package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Pair {
    @Setter
    private String key;
    private String value;

    public Pair() {}
    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
