package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="school")
public class School extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schoolIdSeq")
    @SequenceGenerator(name = "schoolIdSeq", sequenceName="school_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String address1;

    @Column
    private String address2;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    @Column(name="pincode", length = 50)
    private String pincode;

    @NotNull
    @Column(name="affiliation_id", length = 50, unique = true, nullable = false)
    private String affiliationId;

    @NotNull
    @Column(name="primary_phone", length = 50, nullable = false)
    private String primaryPhone;

    @Column(name="secondary_phone", length = 50)
    private String secondaryPhone;

    @NotNull
    @Column(name = "primary_email", nullable = false, length = 50)
    private String primaryEmail;

    @Column(name = "secondary_email", length = 50)
    private String secondaryEmail;

    @Column(name="fax", length = 50)
    private String fax;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Institute Institute;

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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public com.witcurve.domain.Institute getInstitute() {
        return Institute;
    }

    public void setInstitute(com.witcurve.domain.Institute institute) {
        Institute = institute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School)) return false;
        School school = (School) o;
        return Objects.equals(getId(), school.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "School{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", city='" + city + '\'' +
            ", district='" + district + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            ", pincode='" + pincode + '\'' +
            ", affiliationId='" + affiliationId + '\'' +
            ", primaryPhone='" + primaryPhone + '\'' +
            ", secondaryPhone='" + secondaryPhone + '\'' +
            ", primaryEmail='" + primaryEmail + '\'' +
            ", secondaryEmail='" + secondaryEmail + '\'' +
            ", fax='" + fax + '\'' +
            '}';
    }
}
