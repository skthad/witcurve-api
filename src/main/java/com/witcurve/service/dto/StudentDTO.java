package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.Gender;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class StudentDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String middleName;

    private UserDTO user;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate dateOfBirth;

    private String bloodGroup;

    @NotNull
    private String nationality;

    private String religion;

    private String motherTongue;

    private String caste;

    private String subCaste;

    private String category;

    @NotNull
    private String address1;

    private String address2;

    private String city;

    private String district;

    private String state;

    private String country;

    private String pincode;

    private String birthPlace;

    private String adhaarNo;

    private String identificationMark1;

    private String identificationMark2;

    private String previousSchoolName;

    private String previousSchoolAddress;

    private String previousSchoolClass;

    @NotNull
    private Long schoolId;

    @NotNull
    private Long classId;

    private String registeredMobileNumber;

    private List<String> alternateMobileNumbers;

    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate admissionDate;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getSubCaste() {
        return subCaste;
    }

    public void setSubCaste(String subCaste) {
        this.subCaste = subCaste;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getAdhaarNo() {
        return adhaarNo;
    }

    public void setAdhaarNo(String adhaarNo) {
        this.adhaarNo = adhaarNo;
    }

    public String getIdentificationMark1() {
        return identificationMark1;
    }

    public void setIdentificationMark1(String identificationMark1) {
        this.identificationMark1 = identificationMark1;
    }

    public String getIdentificationMark2() {
        return identificationMark2;
    }

    public void setIdentificationMark2(String identificationMark2) {
        this.identificationMark2 = identificationMark2;
    }

    public String getPreviousSchoolName() {
        return previousSchoolName;
    }

    public void setPreviousSchoolName(String previousSchoolName) {
        this.previousSchoolName = previousSchoolName;
    }

    public String getPreviousSchoolAddress() {
        return previousSchoolAddress;
    }

    public void setPreviousSchoolAddress(String previousSchoolAddress) {
        this.previousSchoolAddress = previousSchoolAddress;
    }

    public String getPreviousSchoolClass() {
        return previousSchoolClass;
    }

    public void setPreviousSchoolClass(String previousSchoolClass) {
        this.previousSchoolClass = previousSchoolClass;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getRegisteredMobileNumber() {
        return registeredMobileNumber;
    }

    public void setRegisteredMobileNumber(String registeredMobileNumber) {
        this.registeredMobileNumber = registeredMobileNumber;
    }

    public List<String> getAlternateMobileNumbers() {
        return alternateMobileNumbers;
    }

    public void setAlternateMobileNumbers(List<String> alternateMobileNumbers) {
        this.alternateMobileNumbers = alternateMobileNumbers;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentDTO)) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", bloodGroup='" + bloodGroup + '\'' +
            ", nationality='" + nationality + '\'' +
            ", religion='" + religion + '\'' +
            ", motherTongue='" + motherTongue + '\'' +
            ", caste='" + caste + '\'' +
            ", subCaste='" + subCaste + '\'' +
            ", category='" + category + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", city='" + city + '\'' +
            ", district='" + district + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            ", pincode='" + pincode + '\'' +
            ", birthPlace='" + birthPlace + '\'' +
            ", adhaarNo='" + adhaarNo + '\'' +
            ", identificationMark1='" + identificationMark1 + '\'' +
            ", identificationMark2='" + identificationMark2 + '\'' +
            ", previousSchoolName='" + previousSchoolName + '\'' +
            ", previousSchoolAddress='" + previousSchoolAddress + '\'' +
            ", previousSchoolClass='" + previousSchoolClass + '\'' +
            ", schoolId=" + schoolId +
            ", classId=" + classId +
            ", registeredMobileNumber='" + registeredMobileNumber + '\'' +
            ", gender=" + gender +
            ", admissionDate=" + admissionDate +
            '}';
    }
}
