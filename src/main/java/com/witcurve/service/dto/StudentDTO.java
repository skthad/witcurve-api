package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.BloodGroup;
import com.witcurve.domain.enumeration.Category;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.StudentType;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class StudentDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private SchoolInfoDTO schoolInfo;

    private Long userId;

    private String userName;

    private Boolean active;

    private Boolean hasPassword;

    @NotNull
    private String admissionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate admissionDate;

    private String rollNo;

    @NotNull
    private Gender gender;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @NotNull
    private String address1;

    private String address2;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String state;

    @NotNull
    private String country;

    @NotNull
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    private String pincode;

    private BloodGroup bloodGroup;

    @NotNull
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String registeredMobileNumber;

    private List<String> alternateMobileNumbers;

    @Pattern(regexp = "[0-9]{12}")
    @Size(min = 12, max = 12)
    private String aadhaarNo;

    @NotNull
    private String nationality;

    private String religion;

    private String motherTongue;

    private String caste;

    private String subCaste;

    @NotNull
    private Category category = Category.NA;

    private String subCategory;

    @NotNull
    private StudentType type = StudentType.DAY_SCHOLAR;

    private String house;

    private String birthPlace;

    private String identificationMark1;

    private String identificationMark2;

    private String previousSchoolName;

    private String previousSchoolAddress;

    private String previousSchoolStandard;

    @Min(0)
    @Max(100)
    private Integer previousSchoolPercentage;

    private Double feeAmount = 0.00;

    private Double adjustment = 0.00;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolInfoDTO getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfoDTO schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public StudentType getType() {
        return type;
    }

    public void setType(StudentType type) {
        this.type = type;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
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

    public String getPreviousSchoolStandard() {
        return previousSchoolStandard;
    }

    public void setPreviousSchoolStandard(String previousSchoolStandard) {
        this.previousSchoolStandard = previousSchoolStandard;
    }

    public Integer getPreviousSchoolPercentage() {
        return previousSchoolPercentage;
    }

    public void setPreviousSchoolPercentage(Integer previousSchoolPercentage) {
        this.previousSchoolPercentage = previousSchoolPercentage;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        if (feeAmount == null) {
            feeAmount = 0.00;
        }
        BigDecimal bd = new BigDecimal(feeAmount).setScale(2, RoundingMode.FLOOR);
        this.feeAmount = bd.doubleValue();
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        if (adjustment == null) {
            adjustment = 0.00;
        }
        BigDecimal bd = new BigDecimal(adjustment).setScale(2, RoundingMode.FLOOR);
        this.adjustment = bd.doubleValue();
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
            '}';
    }
}
