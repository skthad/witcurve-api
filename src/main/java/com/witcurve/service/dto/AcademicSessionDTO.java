package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class AcademicSessionDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate startDate;

    @NotNull
    private SchoolInfoDTO schoolInfo;

    @NotNull
    private Boolean active = true;

    private List<TermDTO> termsInSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public SchoolInfoDTO getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfoDTO schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<TermDTO> getTermsInSession() {
        return termsInSession;
    }

    public void setTermsInSession(List<TermDTO> termsInSession) {
        this.termsInSession = termsInSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcademicSessionDTO)) return false;
        AcademicSessionDTO that = (AcademicSessionDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "AcademicSessionDTO{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", schoolInfoId=" + schoolInfo.getId() +
            '}';
    }
}
