package ru.epicprojects.localities.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Embeddable
public class LocalityCompositeAK implements Serializable {

    @Column(nullable = false)
    private String locality;

    @Column(nullable = false)
    private String region;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalityCompositeAK)) return false;
        LocalityCompositeAK that = (LocalityCompositeAK) o;
        return Objects.equals(locality, that.locality) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locality, region);
    }

}
