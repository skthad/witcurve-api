package com.witcurve.service.dto.csv;

import java.util.HashMap;
import java.util.Map;

public class StaffCsv {

    private String firstName;

    private String middleName;

    private String lastName;

    private String employeeId;

    private String gender;

    private String joiningDate;

    private String primaryPhone;

    private String secondaryPhone;

    private String dateOfBirth;

    private String bloodGroup;

    private String type;

    private String address1;

    private String address2;

    private String city;

    private String district;

    private String state;

    private String country;

    private String pincode;

    private String aadhaarNo;

    private String pan;

    private String bankAccountNumber;

    private String bankIfscCode;

    private String errorMessage;

    public final static Map<String, String> ALIAS_MAP = new HashMap<>();

    static {
        ALIAS_MAP.put("* First Name","firstName");

        ALIAS_MAP.put("Middle Name","middleName");

        ALIAS_MAP.put("* Last Name","lastName");

        ALIAS_MAP.put("* Employee Id","employeeId");

        ALIAS_MAP.put("* Joining Date","joiningDate");

        ALIAS_MAP.put("* Gender","gender");

        ALIAS_MAP.put("* Primary Mobile Number","primaryPhone");

        ALIAS_MAP.put("Secondary Mobile Number","secondaryPhone");

        ALIAS_MAP.put("* Date of Birth","dateOfBirth");

        ALIAS_MAP.put("Blood Group","bloodGroup");

        ALIAS_MAP.put("* Type","type");

        ALIAS_MAP.put("* Address 1","address1");

        ALIAS_MAP.put("Address 2","address2");

        ALIAS_MAP.put("* City","city");

        ALIAS_MAP.put("* District","district");

        ALIAS_MAP.put("* State","state");

        ALIAS_MAP.put("* Country","country");

        ALIAS_MAP.put("* Pin Code","pincode");

        ALIAS_MAP.put("Aadhaar Number","aadhaarNo");

        ALIAS_MAP.put("Pan","pan");

        ALIAS_MAP.put("* Bank Account Number","bankAccountNumber");

        ALIAS_MAP.put("* Bank IFSC Code","bankIfscCode");

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "StaffCsv{" +
            "firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", employeeId='" + employeeId + '\'' +
            ", gender='" + gender + '\'' +
            ", joiningDate='" + joiningDate + '\'' +
            ", primaryPhone='" + primaryPhone + '\'' +
            ", secondaryPhone='" + secondaryPhone + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", bloodGroup='" + bloodGroup + '\'' +
            ", type='" + type + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", city='" + city + '\'' +
            ", district='" + district + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            ", pincode='" + pincode + '\'' +
            ", aadhaarNo='" + aadhaarNo + '\'' +
            ", pan='" + pan + '\'' +
            ", bankAccountNumber='" + bankAccountNumber + '\'' +
            ", bankIfscCode='" + bankIfscCode + '\'' +
            ", errorMessage='" + errorMessage + '\'' +
            '}';
    }
}
