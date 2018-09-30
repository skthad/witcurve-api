package com.witcurve.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="master_subject")
public class MasterSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterSubject that = (MasterSubject) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String   toString() {
        return "MasterSubject{" +
            ", name='" + name + '\'' +
            '}';
    }
}
