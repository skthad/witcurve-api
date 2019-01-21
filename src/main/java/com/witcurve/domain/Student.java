package com.witcurve.domain;

import com.witcurve.domain.enumeration.BloodGroup;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.service.util.ListToStringConverter;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="student", uniqueConstraints = {
    @UniqueConstraint(name = "admission_school_info_id_UK",
        columnNames = {"admission_id", "school_info_id"})
})
public class Student extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "studentIdSeq")
    @SequenceGenerator(name = "studentIdSeq", sequenceName="student_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "admission_id", nullable = false)
    private String admissionId;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateOfBirth;

    @Column(name = "blood_group", length = 50)
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @NotNull
    @Column(name = "nationality", length = 50, nullable = false)
    private String nationality;

    @Column(name = "religion", length = 50)
    private String religion;

    @Column(name = "mother_tongue", length = 50)
    private String motherTongue;

    @Column(name = "caste", length = 50)
    private String caste;

    @Column(name = "sub_caste", length = 50)
    private String subCaste;

    @Column(name = "category", length = 50)
    private String category;

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

    @Column(length = 50)
    private String pincode;

    @Column(name = "birth_place", length = 50)
    private String birthPlace;

    @Column(name = "adhaar_no", length = 50)
    private String adhaarNo;

    @Column(name = "identification_mark1")
    private String identificationMark1;

    @Column(name = "identification_mark2")
    private String identificationMark2;

    @Column(name = "previous_school_name")
    private String previousSchoolName;

    @Column(name = "previous_school_address")
    private String previousSchoolAddress;

    @Column(name = "previous_school_standard", length = 50)
    private String previousSchoolStandard;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @Column(length = 50, nullable = false)
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String registeredMobileNumber;

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<@Pattern(regexp="^[6-9]\\d{9}$")String> alternateMobileNumbers;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "admission_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate admissionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
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

    public String getPreviousSchoolStandard() {
        return previousSchoolStandard;
    }

    public void setPreviousSchoolStandard(String previousSchoolStandard) {
        this.previousSchoolStandard = previousSchoolStandard;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
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
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", admissionId='" + admissionId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", user=" + user +
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
            ", previousSchoolStandard='" + previousSchoolStandard + '\'' +
            ", schoolInfo=" + schoolInfo +
            ", registeredMobileNumber='" + registeredMobileNumber + '\'' +
            ", alternateMobileNumbers=" + alternateMobileNumbers +
            ", gender=" + gender +
            ", admissionDate=" + admissionDate +
            '}';
    }
}
