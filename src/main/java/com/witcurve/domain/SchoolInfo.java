package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="school_info", uniqueConstraints = {
    @UniqueConstraint(name = "board_medium_school_UK",
        columnNames = {"board", "medium", "school_id"})
})
public class SchoolInfo extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schoolInfoIdSeq")
    @SequenceGenerator(name = "schoolInfoIdSeq", sequenceName="school_info_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(length = 50, nullable = false)
    private String board;

    @NotNull
    @Column(length = 50, nullable = false)
    private String medium;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private School school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolInfo)) return false;
        SchoolInfo that = (SchoolInfo) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SchoolInfo{" +
            "id=" + id +
            ", board='" + board + '\'' +
            ", medium='" + medium + '\'' +
            ", school=" + school +
            '}';
    }
}
