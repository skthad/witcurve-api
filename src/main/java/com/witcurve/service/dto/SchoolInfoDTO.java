package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SchoolInfoDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private SchoolDTO school;

    @NotNull
    private String board;

    @NotNull
    private String medium;

    private Long mainSchoolInfoUserId;

    public SchoolInfoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolDTO school) {
        this.school = school;
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

    public Long getMainSchoolInfoUserId() {
        return mainSchoolInfoUserId;
    }

    public void setMainSchoolInfoUserId(Long mainSchoolInfoUserId) {
        this.mainSchoolInfoUserId = mainSchoolInfoUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolInfoDTO)) return false;
        SchoolInfoDTO schoolDTO = (SchoolInfoDTO) o;
        return Objects.equals(getId(), schoolDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SchoolInfoDTO{" +
            "id=" + id +
            '}';
    }
}
