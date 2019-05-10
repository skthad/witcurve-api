package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.witcurve.domain.enumeration.BloodGroup;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="staff", uniqueConstraints = {
    @UniqueConstraint(name = "employee_school_info_id_UK",
        columnNames = {"employee_id", "school_info_id"})
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull
    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column
    private String highestEducationalQualification;

    @NotNull
    @Column(name = "joining_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate joiningDate;

    @NotNull
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private StaffType type;

    @Column
    private String designation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotNull
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(nullable = false)
    private String address1;

    @Column
    private String address2;

    @NotNull
    @Column(nullable = false, length = 50)
    private String city;

    @NotNull
    @Column(nullable = false, length = 50)
    private String district;

    @NotNull
    @Column(nullable = false, length = 50)
    private String state;

    @NotNull
    @Column(nullable = false, length = 50)
    private String country;

    @Pattern(regexp = "^[1-9][0-9]{5}$")
    @Column(nullable = false, length = 50)
    private String pincode;

    @Column(name = "blood_group", length = 50)
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @NotNull
    @Column(length = 50, nullable = false)
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String primaryPhone;

    @Column(length = 50)
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String secondaryPhone;

    @NotNull
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, nullable = false)
    private String email;

    @Pattern(regexp = "[0-9]{12}")
    @Column(name = "aadhaar_no", length = 12)
    private String aadhaarNo;

    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}")
    @Column(name = "pan_no", length = 10)
    private String panNo;

    @Column
    private String bankName;

    @Column
    private String accountName;

    @Column
    private String accountNumber;

    @Column
    private String ifscCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getHighestEducationalQualification() {
        return highestEducationalQualification;
    }

    public void setHighestEducationalQualification(String highestEducationalQualification) {
        this.highestEducationalQualification = highestEducationalQualification;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public StaffType getType() {
        return type;
    }

    public void setType(StaffType type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff teacher = (Staff) o;
        return Objects.equals(id, teacher.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Staff{" +
            "id=" + id +
            '}';
    }
}
