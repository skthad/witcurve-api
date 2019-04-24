package com.witcurve.service.util;

import com.witcurve.service.dto.csv.StudentCsv;
import com.witcurve.web.rest.errors.WitcurveException;
import org.simpleflatmapper.csv.CsvWriter;
import org.simpleflatmapper.map.MapperBuildingException;
import org.simpleflatmapper.map.property.RenameProperty;
import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

public class StudentCsvWriter {

    private final Logger log = LoggerFactory.getLogger(StudentCsvWriter.class);

    CsvWriter.CsvWriterDSL<StudentCsv> studentCsvWriterDSL =
        CsvWriter.from(StudentCsv.class)
            .column("* Admission Id", new RenameProperty("admissionId"))
            .column("* Admission Date", new RenameProperty("admissionDate"))
            .column("* First Name", new RenameProperty("firstName"))
            .column("Middle Name", new RenameProperty("middleName"))
            .column("* Last Name", new RenameProperty("lastName"))
            .column("* Gender", new RenameProperty("gender"))
            .column("* Date of Birth", new RenameProperty("dateOfBirth"))
            .column("* Registered Mobile Number", new RenameProperty("registeredMobileNumber"))
            .column("Alternate Mobile Numbers", new RenameProperty("alternateMobileNumbers"))
            .column("* Address 1", new RenameProperty("address1"))
            .column("Address 2", new RenameProperty("address2"))
            .column("* City", new RenameProperty("city"))
            .column("* District", new RenameProperty("district"))
            .column("* State", new RenameProperty("state"))
            .column("* Country", new RenameProperty("country"))
            .column("* Pin Code", new RenameProperty("pinCode"))
            .column("* Nationality", new RenameProperty("nationality"))
            .column("Birth Place", new RenameProperty("birthPlace"))
            .column("Blood Group", new RenameProperty("bloodGroup"))
            .column("Mother Tongue", new RenameProperty("birthPlace"))
            .column("Religion", new RenameProperty("religion"))
            .column("Category", new RenameProperty("category"))
            .column("Sub Category", new RenameProperty("subCategory"))
            .column("Caste", new RenameProperty("caste"))
            .column("Sub Caste", new RenameProperty("subCaste"))
            .column("Aadhaar Number", new RenameProperty("aadhaarNo"))
            .column("Identification Marks 1", new RenameProperty("identificationMark1"))
            .column("Identification Marks 2", new RenameProperty("identificationMark2"))
            .column("Type", new RenameProperty("type"))
            .column("Grade", new RenameProperty("grade"))
            .column("Section", new RenameProperty("section"))
            .column("Roll Number", new RenameProperty("rollNo"))
            .column("Error Message", new RenameProperty("errorMessage"));

    public byte[] generateStudentCSV(List<StudentCsv> errorList) throws WitcurveException {
        String directoryPath = System.getProperty("java.io.tmpdir")+ File.separator+ Instant.now();
        File file = new File(directoryPath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            CsvWriter<StudentCsv> writer =
                studentCsvWriterDSL.to(fileWriter);
            errorList.forEach(CheckedConsumer.toConsumer(writer::append));
        } catch (IOException e) {
            log.debug("Error while writing student csv file : {}",e.getMessage());
            throw new WitcurveException("Error while writing the generating error file");
        } catch (MapperBuildingException e) {
            log.debug("Error while reading file : {}",e.getMessage());
            throw new WitcurveException("Error while reading the file, please check if the file has correct headers");
        }
        try {
            return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            log.debug("Error while writing staff csv file : {}",e.getMessage());
            throw new WitcurveException("Error while writing the generating error file");
        }

    }
}
