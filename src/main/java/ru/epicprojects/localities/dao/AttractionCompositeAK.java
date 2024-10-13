package ru.epicprojects.localities.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
public class AttractionCompositeAK implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(name = "locality_id", nullable = false)
    private Long localityId;

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof AttractionCompositeAK)) return false;
        AttractionCompositeAK that = (AttractionCompositeAK)obj;
        return Objects.equals(that.localityId, this.localityId) && Objects.equals(that.getName(), this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, localityId);
    }
}
