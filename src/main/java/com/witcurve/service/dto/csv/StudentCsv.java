package com.witcurve.service.dto.csv;


import java.util.HashMap;
import java.util.Map;

public class StudentCsv {

    private String firstName;

    private String middleName;

    private String lastName;

    private String motherName;

    private String fatherName;

    private String guardianName;

    private String admissionId;

    private String admissionDate;

    private String gender;

    private String dateOfBirth;

    private String registeredMobileNumber;

    private String alternateMobileNumbers;

    private String email;

    private String address1;

    private String address2;

    private String city;

    private String district;

    private String state;

    private String country;

    private String pincode;

    private String bloodGroup;

    private String aadhaarNo;

    private String nationality;

    private String religion;

    private String motherTongue;

    private String caste;

    private String subCaste;

    private String category;

    private String subCategory;

    private String type;

    private String birthPlace;

    private String identificationMark1;

    private String identificationMark2;

    private String grade;

    private String section;

    private String rollNo;

    private String errorMessage;

    public final static Map<String, String> ALIAS_MAP = new HashMap<>();

    static {

        ALIAS_MAP.put("* First Name","firstName");

        ALIAS_MAP.put("Middle Name","middleName");

        ALIAS_MAP.put("* Last Name","lastName");

        ALIAS_MAP.put("Father Name","fatherName");

        ALIAS_MAP.put("Mother Name","motherName");

        ALIAS_MAP.put("Guardian Name","guardianName");

        ALIAS_MAP.put("* Admission ID","admissionId");

        ALIAS_MAP.put("* Admission Date","admissionDate");

        ALIAS_MAP.put("* Gender","gender");

        ALIAS_MAP.put("* Date of Birth","dateOfBirth");

        ALIAS_MAP.put("* Registered Mobile Number","registeredMobileNumber");

        ALIAS_MAP.put("Alternate Mobile Numbers","alternateMobileNumbers");

        ALIAS_MAP.put("Email Address","email");

        ALIAS_MAP.put("* Address 1","address1");

        ALIAS_MAP.put("Address 2","address2");

        ALIAS_MAP.put("* City","city");

        ALIAS_MAP.put("* District","district");

        ALIAS_MAP.put("* State","state");

        ALIAS_MAP.put("* Country","country");

        ALIAS_MAP.put("* Pin Code","pincode");

        ALIAS_MAP.put("* Nationality","nationality");

        ALIAS_MAP.put("Birth Place","birthPlace");

        ALIAS_MAP.put("Blood Group","bloodGroup");

        ALIAS_MAP.put("Mother Tongue","motherTongue");

        ALIAS_MAP.put("Religion","religion");

        ALIAS_MAP.put("Category","category");

        ALIAS_MAP.put("Sub Category","subCategory");

        ALIAS_MAP.put("Caste","caste");

        ALIAS_MAP.put("Sub Caste","subCaste");

        ALIAS_MAP.put("Aadhaar Number","aadhaarNo");

        ALIAS_MAP.put("Identification Marks 1","identificationMark1");

        ALIAS_MAP.put("Identification Marks 2","identificationMark1");

        ALIAS_MAP.put("Type","type");

        ALIAS_MAP.put("Grade","grade");

        ALIAS_MAP.put("Section","section");

        ALIAS_MAP.put("Roll Number","rollNo");

        ALIAS_MAP.put("Error Message","errorMessage");

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

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredMobileNumber() {
        return registeredMobileNumber;
    }

    public void setRegisteredMobileNumber(String registeredMobileNumber) {
        this.registeredMobileNumber = registeredMobileNumber;
    }

    public String getAlternateMobileNumbers() {
        return alternateMobileNumbers;
    }

    public void setAlternateMobileNumbers(String alternateMobileNumbers) {
        this.alternateMobileNumbers = alternateMobileNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
