package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SchoolDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private InstituteDTO institute;

    @NotNull
    private String name;

    @NotNull
    private String affiliationId;

    @NotNull
    private String address1;

    private String address2;

    private String city;

    private String district;

    private String state;

    private String country;

    private String pincode;

    @NotNull
    private String primaryPhone;

    private String secondaryPhone;

    @NotNull
    private String primaryEmail;

    private String secondaryEmail;

    private String fax;

    private Set<SchoolInfoDTO> schoolInfos;

    @NotNull
    private Boolean primaryBranch = false;

    public SchoolDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstituteDTO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteDTO institute) {
        this.institute = institute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Set<SchoolInfoDTO> getSchoolInfos() {
        return schoolInfos;
    }

    public void setSchoolInfos(Set<SchoolInfoDTO> schoolInfos) {
        this.schoolInfos = schoolInfos;
    }

    public Boolean getPrimaryBranch() { return primaryBranch; }

    public void setPrimaryBranch(Boolean primaryBranch) { this.primaryBranch = primaryBranch; }

    public Set<SchoolInfoDTO> addSchoolInfo(SchoolInfoDTO schoolInfo) {
        if (schoolInfos == null) {
            schoolInfos = new HashSet<>();
        }
        schoolInfos.add(schoolInfo);
        return schoolInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolDTO)) return false;
        SchoolDTO schoolDTO = (SchoolDTO) o;
        return Objects.equals(getId(), schoolDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SchoolDTO{" +
            "id=" + id +
            '}';
    }
}
