package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Pair {
    @Setter
    @Id
    private String key;
    private String value;

    public Pair() {}
    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
