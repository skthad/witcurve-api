package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class GuardianDTO extends AbstractAuditingDTO{

    private Long id;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    @NotNull
    private String type;

    private String qualification;

    private String occupation;

    private Long annualIncome;

    @NotNull
    private String mobileNo;

    private String emailId;

    private String residenceAddress1;

    private String residenceAddress2;

    private String residenceCity;

    private String residenceState;

    private String residencePincode;

    private String officeAddress1;

    private String officeAddress2;

    private String officeCity;

    private String officeState;

    private String officePincode;

    private String rationCardNo;

    private Long studentId;

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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuardianDTO)) return false;
        GuardianDTO that = (GuardianDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "GuardianDTO{" +
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
            ", residencePincode='" + residencePincode + '\'' +
            ", officeAddress1='" + officeAddress1 + '\'' +
            ", officeAddress2='" + officeAddress2 + '\'' +
            ", officeCity='" + officeCity + '\'' +
            ", officeState='" + officeState + '\'' +
            ", officePincode='" + officePincode + '\'' +
            ", rationCardNo='" + rationCardNo + '\'' +
            ", studentId=" + studentId +
            '}';
    }
}
