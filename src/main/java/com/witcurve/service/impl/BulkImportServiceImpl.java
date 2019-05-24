package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.*;
import com.witcurve.service.dto.*;
import com.witcurve.service.dto.csv.ImportResponse;
import com.witcurve.service.dto.csv.StaffCsv;
import com.witcurve.service.dto.csv.StudentCsv;
import com.witcurve.service.util.StaffCsvWriter;
import com.witcurve.service.util.StudentCsvWriter;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.apache.commons.lang3.StringUtils;
import org.simpleflatmapper.csv.CsvMapperFactory;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.map.MapperBuildingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class BulkImportServiceImpl implements BulkImportService {

    private final Logger log = LoggerFactory.getLogger(BulkImportServiceImpl.class);

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffService staffService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    StandardService standardService;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentStandardService studentStandardService;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Override
    @Transactional
    public ImportResponse bulkStaffImport(MultipartFile file, Long schoolInfoId) throws WitcurveException {
        List<StaffCsv> errorStaffCsvs = new ArrayList<>();
        Integer rowNo =2, createCount=0, errorCount=0;
        Map<Integer, String> errorMessageMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(WitcurveUtil.getFile(file).getAbsolutePath()));
            CsvMapperFactory factory = CsvMapperFactory.newInstance()
                .addAliases(StaffCsv.ALIAS_MAP);

            Iterator<StaffCsv> csvIterator = CsvParser.mapWith(factory.newMapper(StaffCsv.class)).iterator(reader);
            while(csvIterator.hasNext()) {
                StaffCsv staffCsv = csvIterator.next();
                try {
                    if(!WitcurveUtil.isObjectEmpty(staffCsv)) {
                        processAndSaveStaffCsv(staffCsv, schoolInfoId);
                        createCount++;
                        log.debug("Staff Details : {} \n \n", staffCsv.toString());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.debug("Error while check empty csv object : {}",e.getMessage());
                    throw new WitcurveException("Error while reading the file, please check if the file is in correct format.");
                } catch (WitcurveException e) {
                    log.debug("Error while converting and saving the staff csv : {}", e.getMessage());
                    errorCount++;
                    errorMessageMap.put(rowNo, e.getMessage());
                    staffCsv.setErrorMessage(e.getMessage());
                    errorStaffCsvs.add(staffCsv);
                }
                rowNo++;
            }

        } catch (MapperBuildingException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file, please check if the file has correct headers");
        } catch (IOException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file due to reason : "+e.getMessage());
        }

        if(errorStaffCsvs.size()!=0) {
            StaffCsvWriter staffCsvWriter = new StaffCsvWriter();
            byte[] errorFileBytes = staffCsvWriter.generateStaffCSV(errorStaffCsvs);

            return new ImportResponse(createCount, errorCount, errorMessageMap, new String(Base64.getEncoder().encode(errorFileBytes)));
        } else {
            return new ImportResponse(createCount, errorCount, null, null);
        }

    }


    @Override
    @Transactional
    public ImportResponse bulkStudentImport(MultipartFile file, Long schoolInfoId) throws WitcurveException {
        List<StudentCsv> errorStudentCsvs = new ArrayList<>();
        Integer rowNo =2, createCount=0, errorCount=0;
        AcademicSession academicSession = academicSessionRepository.nearestActiveSessionToDate(schoolInfoId, LocalDate.now());
        if(academicSession == null) {
            throw new WitcurveException("There is not active current session for this board");
        }
        Map<Integer, String> errorMessageMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(WitcurveUtil.getFile(file).getAbsolutePath()));
            CsvMapperFactory factory = CsvMapperFactory.newInstance()
                .addAliases(StudentCsv.ALIAS_MAP);

            Iterator<StudentCsv> csvIterator = CsvParser.mapWith(factory.newMapper(StudentCsv.class)).iterator(reader);
            while(csvIterator.hasNext()) {
                StudentCsv studentCsv = csvIterator.next();
                try {
                    if(!WitcurveUtil.isObjectEmpty(studentCsv)) {
                        processAndSaveStudentCsv(studentCsv, schoolInfoId);
                        createCount++;
                        log.debug("Staff Details : {} \n \n", studentCsv.toString());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.debug("Error while check empty csv object : {}",e.getMessage());
                    throw new WitcurveException("Error while reading the file, please check if the file is in correct format.");
                } catch (WitcurveException e) {
                    log.debug("Error while converting and saving the student csv : {}", e.getMessage());
                    errorCount++;
                    errorMessageMap.put(rowNo, e.getMessage());
                    studentCsv.setErrorMessage(e.getMessage());
                    errorStudentCsvs.add(studentCsv);
                }
                rowNo++;
            }

        } catch (MapperBuildingException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file, please check if the file has correct headers");
        } catch (IOException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file due to reason : "+e.getMessage());
        }

        if(errorStudentCsvs.size()!=0) {
            StudentCsvWriter studentCsvWriter = new StudentCsvWriter();
            byte[] errorFileBytes = studentCsvWriter.generateStudentCSV(errorStudentCsvs);

            return new ImportResponse(createCount, errorCount, errorMessageMap, new String(Base64.getEncoder().encode(errorFileBytes)));
        } else {
            return new ImportResponse(createCount, errorCount, null, null);
        }
    }

    private void processAndSaveStaffCsv(StaffCsv staffCsv, Long schoolInfoId) throws WitcurveException {

        StaffDTO staffDTO = new StaffDTO();
        SchoolInfoDTO schoolInfo = new SchoolInfoDTO();
        schoolInfo.setId(schoolInfoId);
        staffDTO.setSchoolInfo(schoolInfo);

        //converting each csv field to dto after checking if entered values are valid

        if(staffCsv.getFirstName() != null && !StringUtils.isBlank(staffCsv.getFirstName())) {
            staffDTO.setFirstName(staffCsv.getFirstName());
        } else {
            throw new WitcurveException("First Name value is empty");
        }

        if(staffCsv.getMiddleName() != null && !StringUtils.isBlank(staffCsv.getMiddleName())) {
            staffDTO.setMiddleName(staffCsv.getMiddleName());
        }

        if(staffCsv.getLastName() != null && !StringUtils.isBlank(staffCsv.getLastName())) {
            staffDTO.setLastName(staffCsv.getLastName());
        } else {
            throw new WitcurveException("Last Name value is empty");
        }

        if(staffCsv.getEmployeeId() != null && !StringUtils.isBlank(staffCsv.getEmployeeId())) {
            Staff existingStaff = staffRepository.findBySchoolInfoIdAndStaffId(schoolInfoId, staffCsv.getEmployeeId());
            if(existingStaff != null) {
                throw new WitcurveException("There already exists a staff with given Employee Id value");
            }
            staffDTO.setEmployeeId(staffCsv.getEmployeeId());
        } else {
            throw new WitcurveException("Employee Id value is empty");
        }

        if(staffCsv.getJoiningDate() != null && !StringUtils.isBlank(staffCsv.getJoiningDate())) {
            try {
                LocalDate joiningDate = WitcurveUtil.getLocalDate(staffCsv.getJoiningDate(), WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
                staffDTO.setJoiningDate(joiningDate);
            } catch (Exception e) {
                throw new WitcurveException("Joining Date value should be a valid date in format dd/MM/yyyy");
            }
        } else {
            throw new WitcurveException("Joining Date value is empty");
        }

        if(staffCsv.getGender() != null && !StringUtils.isBlank(staffCsv.getGender())) {
            Gender gender = Gender.getGender(staffCsv.getGender().toUpperCase());
            if(gender != null) {
                staffDTO.setGender(gender);
            } else {
                throw new WitcurveException("Gender value is invalid, please enter only one these values : Male, Female, Other");
            }
        } else {
            throw new WitcurveException("Gender value is empty");
        }

        if(staffCsv.getPrimaryPhone() != null && !StringUtils.isBlank(staffCsv.getPrimaryPhone())) {
            if(staffCsv.getPrimaryPhone().matches("^[6-9]\\d{9}$")) {
                staffDTO.setPrimaryPhone(staffCsv.getPrimaryPhone());
            } else {
                throw new WitcurveException("Primary Phone value is invalid, please enter 10 digit mobile number");
            }
        } else {
            throw new WitcurveException("Primary Phone value is required");
        }

        if(staffCsv.getSecondaryPhone() != null && !StringUtils.isBlank(staffCsv.getSecondaryPhone())) {
            if(staffCsv.getSecondaryPhone().matches("^[6-9]\\d{9}$")) {
                staffDTO.setSecondaryPhone(staffCsv.getSecondaryPhone());
            } else {
                throw new WitcurveException("Secondary Phone value is invalid, please enter 10 digit mobile number");
            }
        }

        if(staffCsv.getEmail() != null && !StringUtils.isBlank(staffCsv.getEmail())) {
            if(staffCsv.getEmail().replaceAll("\\s","").matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                staffDTO.setEmail(staffCsv.getEmail());
            } else {
                throw new WitcurveException("Email Address is invalid, please enter correct email address.");
            }
        } else {
            throw new WitcurveException("Email Address is empty");
        }


        if(staffCsv.getDateOfBirth() != null && !StringUtils.isBlank(staffCsv.getDateOfBirth())) {
            try {
                LocalDate dob = WitcurveUtil.getLocalDate(staffCsv.getDateOfBirth(), WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
                staffDTO.setDateOfBirth(dob);
            } catch (Exception e) {
                throw new WitcurveException("Date of Birth value should be a valid date in format dd/MM/yyyy");
            }
        } else {
            throw new WitcurveException("Date of Birth value is required");
        }

        if(staffCsv.getBloodGroup() != null && !StringUtils.isBlank(staffCsv.getBloodGroup())) {
            BloodGroup bloodGroup = BloodGroup.getBloodGroup(staffCsv.getBloodGroup().replaceAll("\\s","").toUpperCase());
            if(bloodGroup != null) {
                staffDTO.setBloodGroup(bloodGroup);
            } else {
                throw new WitcurveException("Blood Group value is invalid, please enter only one these values : A+, A-, B+, B-, AB+, AB-, O+, O-");
            }
        }

        if(staffCsv.getType() != null && !StringUtils.isBlank(staffCsv.getType())) {
            StaffType staffType = StaffType.getStaffType(staffCsv.getType().trim().toUpperCase());
            if(staffType != null) {
                staffDTO.setType(staffType);
            } else {
                throw new WitcurveException("Staff Type value is invalid, please enter only one these values : Teaching, Non Teaching");
            }
        } else {
            throw new WitcurveException("Staff Type value is empty");
        }

        if(staffCsv.getAddress1() != null && !StringUtils.isBlank(staffCsv.getAddress1())) {
            staffDTO.setAddress1(staffCsv.getAddress1());
        } else {
            throw new WitcurveException("Address 1 value is empty");
        }

        if(staffCsv.getAddress2() != null && !StringUtils.isBlank(staffCsv.getAddress2())) {
            staffDTO.setAddress2(staffCsv.getAddress2());
        }

        if(staffCsv.getDistrict() != null && !StringUtils.isBlank(staffCsv.getDistrict())) {
            staffDTO.setDistrict(staffCsv.getDistrict());
        } else {
            throw new WitcurveException("District value is empty");
        }

        if(staffCsv.getCity() != null && !StringUtils.isBlank(staffCsv.getCity())) {
            staffDTO.setCity(staffCsv.getCity());
        } else {
            throw new WitcurveException("City value is empty");
        }

        if(staffCsv.getState() != null && !StringUtils.isBlank(staffCsv.getState())) {
            staffDTO.setState(staffCsv.getState());
        } else {
            throw new WitcurveException("State value is empty");
        }

        if(staffCsv.getCountry() != null && !StringUtils.isBlank(staffCsv.getCountry())) {
            staffDTO.setCountry(staffCsv.getCountry());
        } else {
            throw new WitcurveException("Country value is empty");
        }

        if(staffCsv.getPincode() != null && !StringUtils.isBlank(staffCsv.getPincode())) {
            if(staffCsv.getPincode().matches("^[1-9][0-9]{5}$")) {
                staffDTO.setPincode(staffCsv.getPincode());
            } else {
                throw new WitcurveException("Incorrect Pincode value");
            }

        } else {
            throw new WitcurveException("Pincode value is empty");
        }

        if(staffCsv.getAadhaarNo() != null && !StringUtils.isBlank(staffCsv.getAadhaarNo())) {
            if(staffCsv.getAadhaarNo().matches("[0-9]{12}")) {
                staffDTO.setAadhaarNo(staffCsv.getAadhaarNo());
            } else {
                throw new WitcurveException("Incorrect Aadhaar Number value, it should be a 12 digit number");
            }
        }

        if(staffCsv.getPan() != null && !StringUtils.isBlank(staffCsv.getPan())) {
            if(staffCsv.getPan().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
                staffDTO.setPanNo(staffCsv.getPan());
            } else {
                throw new WitcurveException("Incorrect Pan Number input");
            }
        }

        if(staffCsv.getBankAccountNumber() != null && !StringUtils.isBlank(staffCsv.getBankAccountNumber())) {
            staffDTO.setAccountNumber(staffCsv.getBankAccountNumber());
        }

        if(staffCsv.getBankIfscCode() != null && !StringUtils.isBlank(staffCsv.getBankIfscCode())) {
            staffDTO.setIfscCode(staffCsv.getBankIfscCode());
        }

        staffService.create(staffDTO);

    }

    private void processAndSaveStudentCsv(StudentCsv studentCsv, Long schoolInfoId) {

        StudentDTO studentDTO = new StudentDTO();
        StandardDTO standardDTO = new StandardDTO();
        StudentStandardDTO studentStandardDTO = new StudentStandardDTO();
        SchoolInfoDTO schoolInfo = new SchoolInfoDTO();
        schoolInfo.setId(schoolInfoId);
        studentDTO.setSchoolInfo(schoolInfo);
        Long sessionId= academicSessionRepository.nearestActiveSessionToDate(schoolInfoId, LocalDate.now()).getId();

        Grade grade = null;
        Boolean doesGradeExist=false, doesSectionExist=false, doesRollNumberExist=false;
        Boolean doesCasteExist=false, doesCategoryExist=false;

        //converting each csv field to dto after check if entered values are valid

        if(studentCsv.getFirstName() != null && !StringUtils.isBlank(studentCsv.getFirstName())) {
            studentDTO.setFirstName(studentCsv.getFirstName());
        } else {
            throw new WitcurveException("First Name value is empty");
        }

        if(studentCsv.getMiddleName() != null && !StringUtils.isBlank(studentCsv.getMiddleName())) {
            studentDTO.setMiddleName(studentCsv.getMiddleName());
        }

        if(studentCsv.getLastName() != null && !StringUtils.isBlank(studentCsv.getLastName())) {
            studentDTO.setLastName(studentCsv.getLastName());
        } else {
            throw new WitcurveException("Last Name value is empty");
        }

        if(studentCsv.getFatherName() != null && !StringUtils.isBlank(studentCsv.getFatherName())) {
            studentDTO.setFatherName(studentCsv.getFatherName());
        }

        if(studentCsv.getMotherName() != null && !StringUtils.isBlank(studentCsv.getMotherName())) {
            studentDTO.setMotherName(studentCsv.getMotherName());
        }

        if(studentCsv.getGuardianName() != null && !StringUtils.isBlank(studentCsv.getGuardianName())) {
            studentDTO.setGuardianName(studentCsv.getGuardianName());
        }

        if(studentCsv.getAdmissionId() != null && !StringUtils.isBlank(studentCsv.getAdmissionId())) {
            Student existingStudent = studentRepository.findBySchoolInfoIdAndAdmissionId(schoolInfoId, studentCsv.getAdmissionId());
            if(existingStudent != null) {
                throw new WitcurveException("There already exists a student with given Admission Id value");
            }
            studentDTO.setAdmissionId(studentCsv.getAdmissionId());
        } else {
            throw new WitcurveException("Admission Id value is empty");
        }

        if(studentCsv.getAdmissionDate() != null && !StringUtils.isBlank(studentCsv.getAdmissionDate())) {
            try {
                LocalDate admissionDate = WitcurveUtil.getLocalDate(studentCsv.getAdmissionDate(), WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
                studentDTO.setAdmissionDate(admissionDate);
            } catch (Exception e) {
                throw new WitcurveException("Admission Date value should be a valid date in format dd/MM/yyyy");
            }
        } else {
            throw new WitcurveException("Admission Date value is empty");
        }

        if(studentCsv.getGender() != null && !StringUtils.isBlank(studentCsv.getGender())) {
            Gender gender = Gender.getGender(studentCsv.getGender().toUpperCase());
            if(gender != null) {
                studentDTO.setGender(gender);
            } else {
                throw new WitcurveException("Gender value is invalid, please enter only one these values : Male, Female, Other");
            }
        } else {
            throw new WitcurveException("Gender value is empty");
        }


        if(studentCsv.getDateOfBirth() != null && !StringUtils.isBlank(studentCsv.getDateOfBirth())) {
            try {
                LocalDate dob = WitcurveUtil.getLocalDate(studentCsv.getDateOfBirth(), WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
                studentDTO.setDateOfBirth(dob);
            } catch (Exception e) {
                throw new WitcurveException("Date of Birth value should be a valid date in format dd/MM/yyyy");
            }
        } else {
            throw new WitcurveException("Date of Birth value is required");
        }

        if(studentCsv.getRegisteredMobileNumber() != null && !StringUtils.isBlank(studentCsv.getRegisteredMobileNumber())) {
            if(studentCsv.getRegisteredMobileNumber().replaceAll("\\s","").matches("^[6-9]\\d{9}$")) {
                studentDTO.setRegisteredMobileNumber(studentCsv.getRegisteredMobileNumber());
            } else {
                throw new WitcurveException("Registered Mobile Number is invalid, please enter 10 digit mobile number");
            }
        } else {
            throw new WitcurveException("Registered Mobile Number is empty");
        }

        if(studentCsv.getAlternateMobileNumbers() != null && !StringUtils.isBlank(studentCsv.getAlternateMobileNumbers())) {
            List<String> alternateNumbersList = new ArrayList<>();
            String[] alternateNumbers = studentCsv.getAlternateMobileNumbers().replaceAll("\\s","").split(",");
            for(int i=0; i<alternateNumbers.length; i++) {
                if(alternateNumbers[i].matches("^[6-9]\\d{9}$")) {
                    alternateNumbersList.add(alternateNumbers[i]);
                } else {
                    throw new WitcurveException("Please make sure alternate number are 10 digit phone numbers separated by comma. Ex : 9876543210, 9876543210");
                }
            }
            studentDTO.setAlternateMobileNumbers(alternateNumbersList);
        }

        if(studentCsv.getEmail() != null && !StringUtils.isBlank(studentCsv.getEmail())) {
            if(studentCsv.getEmail().replaceAll("\\s","").matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                studentDTO.setEmail(studentCsv.getEmail());
            } else {
                throw new WitcurveException("Email Address is invalid, please enter correct email address.");
            }
        }


        if(studentCsv.getAddress1() != null && !StringUtils.isBlank(studentCsv.getAddress1())) {
            studentDTO.setAddress1(studentCsv.getAddress1());
        } else {
            throw new WitcurveException("Address 1 value is empty");
        }

        if(studentCsv.getAddress2() != null && !StringUtils.isBlank(studentCsv.getAddress2())) {
            studentDTO.setAddress2(studentCsv.getAddress2());
        }

        if(studentCsv.getDistrict() != null && !StringUtils.isBlank(studentCsv.getDistrict())) {
            studentDTO.setDistrict(studentCsv.getDistrict());
        } else {
            throw new WitcurveException("District value is empty");
        }

        if(studentCsv.getCity() != null && !StringUtils.isBlank(studentCsv.getCity())) {
            studentDTO.setCity(studentCsv.getCity());
        } else {
            throw new WitcurveException("City value is empty");
        }

        if(studentCsv.getState() != null && !StringUtils.isBlank(studentCsv.getState())) {
            studentDTO.setState(studentCsv.getState());
        } else {
            throw new WitcurveException("State value is empty");
        }

        if(studentCsv.getCountry() != null && !StringUtils.isBlank(studentCsv.getCountry())) {
            studentDTO.setCountry(studentCsv.getCountry());
        } else {
            throw new WitcurveException("Country value is empty");
        }

        if(studentCsv.getPincode() != null && !StringUtils.isBlank(studentCsv.getPincode())) {
            if(studentCsv.getPincode().matches("^[1-9][0-9]{5}$")) {
                studentDTO.setPincode(studentCsv.getPincode());
            } else {
                throw new WitcurveException("Incorrect Pincode value");
            }

        } else {
            throw new WitcurveException("Pincode value is empty");
        }



        if(studentCsv.getBloodGroup() != null && !StringUtils.isBlank(studentCsv.getBloodGroup())) {
            BloodGroup bloodGroup = BloodGroup.getBloodGroup(studentCsv.getBloodGroup().replaceAll("\\s","").toUpperCase());
            if(bloodGroup != null) {
                studentDTO.setBloodGroup(bloodGroup);
            } else {
                throw new WitcurveException("Blood Group value is invalid, please enter only one these values : A+, A-, B+, B-, AB+, AB-, O+, O-");
            }
        }

        if(studentCsv.getNationality() != null && !StringUtils.isBlank(studentCsv.getNationality())) {
            studentDTO.setNationality(studentCsv.getNationality());
        } else {
            throw new WitcurveException("Nationality value is empty");
        }

        if(studentCsv.getAadhaarNo() != null && !StringUtils.isBlank(studentCsv.getAadhaarNo())) {
            if(studentCsv.getAadhaarNo().matches("[0-9]{12}")) {
                studentDTO.setAadhaarNo(studentCsv.getAadhaarNo());
            } else {
                throw new WitcurveException("Incorrect Aadhaar Number value");
            }
        }

        if(studentCsv.getReligion() != null && !StringUtils.isBlank(studentCsv.getReligion())) {
            studentDTO.setReligion(studentCsv.getReligion());
        }

        if(studentCsv.getMotherTongue() != null && !StringUtils.isBlank(studentCsv.getMotherTongue())) {
            studentDTO.setMotherTongue(studentCsv.getMotherTongue());
        }

        if(studentCsv.getCaste() != null && !StringUtils.isBlank(studentCsv.getCaste())) {
            doesCasteExist = true;
            studentDTO.setCaste(studentCsv.getCaste());
        }

        if(studentCsv.getSubCaste() != null && !StringUtils.isBlank(studentCsv.getSubCaste())) {
            if(!doesCasteExist) {
                studentCsv.setSubCaste(null);
            }
            studentDTO.setSubCaste(studentCsv.getSubCaste());
        }

        if(studentCsv.getCategory() != null && !StringUtils.isBlank(studentCsv.getCategory())) {
            Category category = Category.getCategory(studentCsv.getCategory().toUpperCase());
            if(category == null) {
                throw new WitcurveException("Category value is invalid, please enter only one these values : General, SC, ST, OBC, N/A, Other");
            }
            doesCategoryExist=true;
            studentDTO.setCategory(category);
        }

        if(studentCsv.getSubCategory() != null && !StringUtils.isBlank(studentCsv.getSubCategory())) {
            if(!doesCategoryExist) {
                studentCsv.setSubCategory(null);
            } else {
                if(studentDTO.getCategory().equals(Category.GENERAL)) {
                    studentCsv.setSubCategory(null);
                }
            }
            studentDTO.setSubCategory(studentCsv.getSubCategory());
        }

        if(studentCsv.getType() != null && !StringUtils.isBlank(studentCsv.getType())) {
            StudentType type = StudentType.getStudentType(studentCsv.getType().toUpperCase());
            if(type == null) {
                throw new WitcurveException("Type value is invalid, please enter only one these values : Day Scholar, Residential, Semi-Residential");
            }
            studentDTO.setType(type);
        }

        if(studentCsv.getBirthPlace() != null && !StringUtils.isBlank(studentCsv.getBirthPlace())) {
            studentDTO.setBirthPlace(studentCsv.getBirthPlace());
        }

        if(studentCsv.getIdentificationMark1() != null && !StringUtils.isBlank(studentCsv.getIdentificationMark1())) {
            studentDTO.setIdentificationMark1(studentCsv.getIdentificationMark1());
        }

        if(studentCsv.getIdentificationMark2() != null && !StringUtils.isBlank(studentCsv.getIdentificationMark2())) {
            studentDTO.setIdentificationMark2(studentCsv.getIdentificationMark2());
        }

        if(studentCsv.getGrade() != null && !StringUtils.isBlank(studentCsv.getGender())) {
            grade = Grade.getGrade(studentCsv.getGrade().toUpperCase());
            if(grade == null) {
                throw new WitcurveException("Grade value is invalid, please enter only one of these values : Nursery, I, II, III, IV, V, VI, VII, VIII, IX, XI, XII");
            }
            doesGradeExist=true;
        }

        if(studentCsv.getSection() != null && !StringUtils.isBlank(studentCsv.getSection())) {
            Standard standard = standardRepository.findByGradeAndSectionAndSchoolInfoId(grade, studentCsv.getSection().toUpperCase(), schoolInfoId);
            if(standard == null) {
                throw new WitcurveException("Standard does not exist for given grade and section");
            }
            standardDTO.setId(standard.getId());
            doesSectionExist=true;
        }

        if(studentCsv.getRollNo() != null && !StringUtils.isBlank(studentCsv.getRollNo())) {
            List<String> rollNos = new ArrayList<>();
            rollNos.add(studentCsv.getRollNo());
            List<StudentStandard> existingRollNos = studentStandardRepository.getByStandardIdAndRollNos(standardDTO.getId(), rollNos);
            if(existingRollNos.size() !=0) {
                throw new WitcurveException("There's already exists a student in given standard with entered roll no.");
            }
            doesRollNumberExist=true;
        }

        if( (doesGradeExist || doesSectionExist || doesRollNumberExist) && !(doesSectionExist && doesGradeExist && doesRollNumberExist)) {
            throw new WitcurveException("Grade, Section and Roll Number has to be entered together");
        }

        //save student
        studentDTO = studentService.create(studentDTO);

        //save student standard
        if(doesGradeExist && doesSectionExist && doesRollNumberExist) {
            studentStandardDTO.setStudent(studentDTO);
            studentStandardDTO.setStandard(standardDTO);
            studentStandardDTO.setSessionId(sessionId);
            studentStandardDTO.setRollNo(studentCsv.getRollNo());
            studentStandardDTO.setActive(true);
            studentStandardService.save(studentStandardDTO);
        }
    }
}
