package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class StudentFeeStructureDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long sessionId;

    @NotNull
    private Long studentId;

    @NotNull
    private Long selectedSessionId;

    private List<StudentFeeTypeDTO> studentFeeTypes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSelectedSessionId() {
        return selectedSessionId;
    }

    public void setSelectedSessionId(Long selectedSessionId) {
        this.selectedSessionId = selectedSessionId;
    }

    public List<StudentFeeTypeDTO> getStudentFeeTypes() {
        return studentFeeTypes;
    }

    public void setStudentFeeTypes(List<StudentFeeTypeDTO> studentFeeTypes) {
        this.studentFeeTypes = studentFeeTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeStructureDTO that = (StudentFeeStructureDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeStructureDTO{" +
            "id=" + id +
            '}';
    }
}
