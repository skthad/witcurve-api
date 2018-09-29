package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SchoolDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address1;

    private String address2;

    private String city;

    private String district;

    private String state;

    private String country;

    @NotNull
    private String affiliationId;

    @NotNull
    private String primaryPhone;

    private String secondaryPhone;

    private String fax;

    private String pincode;

    @NotNull
    private String primaryEmail;

    private String secondaryEmail;

    @NotNull
    private Long instituteId;

    public SchoolDTO() {
    }

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

    public String getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) {
        this.instituteId = instituteId;
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
            ", name='" + name + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", city='" + city + '\'' +
            ", district='" + district + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            ", affiliationId='" + affiliationId + '\'' +
            ", primaryPhone='" + primaryPhone + '\'' +
            ", secondaryPhone='" + secondaryPhone + '\'' +
            ", fax='" + fax + '\'' +
            ", pincode='" + pincode + '\'' +
            ", primaryEmail='" + primaryEmail + '\'' +
            ", secondaryEmail='" + secondaryEmail + '\'' +
            ", instituteId='" + instituteId + '\'' +
            '}';
    }
}
