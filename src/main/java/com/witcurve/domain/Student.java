package com.witcurve.domain;

import com.witcurve.domain.enumeration.BloodGroup;
import com.witcurve.domain.enumeration.Category;
import com.witcurve.domain.enumeration.Gender;
import com.witcurve.service.util.ListToStringConverter;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="student", uniqueConstraints = {
    @UniqueConstraint(name = "admission_school_info_id_UK",
        columnNames = {"admission_id", "school_info_id"})
})
public class Student extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "admission_id", nullable = false)
    private String admissionId;

    @NotNull
    @Column(name = "admission_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate admissionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
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

    @NotNull
    @Column(nullable = false, length = 50)
    private String pincode;

    @Column(name = "blood_group", length = 50)
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @NotNull
    @Column(length = 50, nullable = false)
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String registeredMobileNumber;

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<@Pattern(regexp="^[6-9]\\d{9}$")String> alternateMobileNumbers;

    @Pattern(regexp = "[0-9]{16}")
    @Column(name = "aadhaar_no", length = 16)
    private String aadhaarNo;

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

    @NotNull
    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) default 'NA'")
    @Enumerated(EnumType.STRING)
    private Category category = Category.NA;

    @NotNull
    @Column(name = "day_scholar", nullable = false, columnDefinition = "boolean default true")
    private Boolean dayScholar = true;

    @Column(length = 50)
    private Category house;

    @Column(name = "sub_category", length = 50)
    private String subCategory;

    @Column(name = "birth_place", length = 50)
    private String birthPlace;

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

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="student_id", insertable = false, updatable = false)
    @Where(clause = "active=true")
    private Set<StudentStandard> studentStandards;

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

    public Boolean getDayScholar() {
        return dayScholar;
    }

    public void setDayScholar(Boolean dayScholar) {
        this.dayScholar = dayScholar;
    }

    public Category getHouse() {
        return house;
    }

    public void setHouse(Category house) {
        this.house = house;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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

    public Set<StudentStandard> getStudentStandards() {
        return studentStandards;
    }

    public void setStudentStandards(Set<StudentStandard> studentStandards) {
        this.studentStandards = studentStandards;
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
            '}';
    }
}
