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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guardianIdSeq")
    @SequenceGenerator(name = "guardianIdSeq", sequenceName="guardian_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "type", length = 50, nullable = false)
    private String type;

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

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
            ", firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", type='" + type + '\'' +
            ", qualification='" + qualification + '\'' +
            ", occupation='" + occupation + '\'' +
            ", annualIncome=" + annualIncome +
            ", mobileNo='" + mobileNo + '\'' +
            ", emailId='" + emailId + '\'' +
            ", residenceAddress1='" + residenceAddress1 + '\'' +
            ", residenceAddress2='" + residenceAddress2 + '\'' +
            ", residenceCity='" + residenceCity + '\'' +
            ", residenceState='" + residenceState + '\'' +
            ", residencePincode=" + residencePincode +
            ", officeAddress1='" + officeAddress1 + '\'' +
            ", officeAddress2='" + officeAddress2 + '\'' +
            ", officeCity='" + officeCity + '\'' +
            ", officeState='" + officeState + '\'' +
            ", officePincode=" + officePincode +
            ", rationCardNo='" + rationCardNo + '\'' +
            ", student=" + student +
            '}';
    }
}
