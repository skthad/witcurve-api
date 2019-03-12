package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="guardian")
public class Guardian extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "guardianIdSeq", sequenceName="guardian_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "relation_with_student", length = 50, nullable = false)
    private String relationWithStudent;

    @Column(name = "qualification", length = 50)
    private String qualification;

    @Column(name = "occupation", length = 50)
    private String occupation;

    @Column(name = "annual_income")
    private Long annualIncome;

    @NotNull
    @Column(name = "mobile_no", length = 50, nullable = false)
    private String mobileNo;

    @Column(name = "email_id", length = 50)
    private String emailId;

    @Column(name = "residence_address1")
    private String residenceAddress1;

    @Column(name = "residence_address2")
    private String residenceAddress2;

    @Column(name = "residence_city", length = 50)
    private String residenceCity;

    @Column(name = "residence_state", length = 50)
    private String residenceState;

    @Column(name = "residence_pincode", length = 50)
    private String residencePincode;

    @Column(name = "office_address1")
    private String officeAddress1;

    @Column(name = "office_address2")
    private String officeAddress2;

    @Column(name = "office_city", length = 50)
    private String officeCity;

    @Column(name = "office_state", length = 50)
    private String officeState;

    @Column(name = "office_pincode", length = 50)
    private String officePincode;

    @Column(name = "ration_card_no", length = 50)
    private String rationCardNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRelationWithStudent() {
        return relationWithStudent;
    }

    public void setRelationWithStudent(String relationWithStudent) {
        this.relationWithStudent = relationWithStudent;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Long getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Long annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getResidenceAddress1() {
        return residenceAddress1;
    }

    public void setResidenceAddress1(String residenceAddress1) {
        this.residenceAddress1 = residenceAddress1;
    }

    public String getResidenceAddress2() {
        return residenceAddress2;
    }

    public void setResidenceAddress2(String residenceAddress2) {
        this.residenceAddress2 = residenceAddress2;
    }

    public String getResidenceCity() {
        return residenceCity;
    }

    public void setResidenceCity(String residenceCity) {
        this.residenceCity = residenceCity;
    }

    public String getResidenceState() {
        return residenceState;
    }

    public void setResidenceState(String residenceState) {
        this.residenceState = residenceState;
    }

    public String getResidencePincode() {
        return residencePincode;
    }

    public void setResidencePincode(String residencePincode) {
        this.residencePincode = residencePincode;
    }

    public String getOfficeAddress1() {
        return officeAddress1;
    }

    public void setOfficeAddress1(String officeAddress1) {
        this.officeAddress1 = officeAddress1;
    }

    public String getOfficeAddress2() {
        return officeAddress2;
    }

    public void setOfficeAddress2(String officeAddress2) {
        this.officeAddress2 = officeAddress2;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeState() {
        return officeState;
    }

    public void setOfficeState(String officeState) {
        this.officeState = officeState;
    }

    public String getOfficePincode() {
        return officePincode;
    }

    public void setOfficePincode(String officePincode) {
        this.officePincode = officePincode;
    }

    public String getRationCardNo() {
        return rationCardNo;
    }

    public void setRationCardNo(String rationCardNo) {
        this.rationCardNo = rationCardNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guardian guardian = (Guardian) o;
        return Objects.equals(id, guardian.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Guardian{" +
            "id=" + id +
            '}';
    }
}
