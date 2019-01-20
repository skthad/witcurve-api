package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="institute", uniqueConstraints = {
    @UniqueConstraint(name = "institute_name_UK",
        columnNames = {"name"})
})
public class Institute extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instituteIdSeq")
    @SequenceGenerator(name = "instituteIdSeq", sequenceName="instititute_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institute)) return false;
        Institute institute = (Institute) o;
        return Objects.equals(getId(), institute.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Institute{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
