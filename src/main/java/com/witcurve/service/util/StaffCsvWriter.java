package com.witcurve.service.util;

import com.witcurve.service.dto.csv.StaffCsv;
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
import java.util.List;

public class StaffCsvWriter {

    private final Logger log = LoggerFactory.getLogger(StaffCsvWriter.class);

    CsvWriter.CsvWriterDSL<StaffCsv> staffWriterDsl =
        CsvWriter.from(StaffCsv.class)
            .column("* Employee Id", new RenameProperty("employeeId"))
            .column("* Joining Date", new RenameProperty("joiningDate"))
            .column("* First Name", new RenameProperty("firstName"))
            .column("Middle Name", new RenameProperty("middleName"))
            .column("* Last Name", new RenameProperty("lastName"))
            .column("* Gender", new RenameProperty("gender"))
            .column("* Primary Mobile Number", new RenameProperty("primaryPhone"))
            .column("Secondary Mobile Number", new RenameProperty("secondaryPhone"))
            .column("* Email Address", new RenameProperty("email"))
            .column("* Date of Birth", new RenameProperty("dateOfBirth"))
            .column("Blood Group", new RenameProperty("bloodGroup"))
            .column("* Type", new RenameProperty("type"))
            .column("* Address 1", new RenameProperty("address1"))
            .column("Address 2", new RenameProperty("address2"))
            .column("* City", new RenameProperty("city"))
            .column("* District", new RenameProperty("district"))
            .column("* State", new RenameProperty("state"))
            .column("* Country", new RenameProperty("country"))
            .column("* Pin Code", new RenameProperty("pinCode"))
            .column("Aadhaar Number", new RenameProperty("aadhaarNo"))
            .column("Pan", new RenameProperty("pan"))
            .column("Bank Account Number", new RenameProperty("bankAccountNumber"))
            .column("Bank IFSC Code", new RenameProperty("bankIfscCode"))
            .column("Error Message", new RenameProperty("errorMessage"));

    public byte[] generateStaffCSV(List<StaffCsv> errorList) throws WitcurveException {
       File file = WitcurveUtil.createTempFile(null);
        try (FileWriter fileWriter = new FileWriter(file)) {
            CsvWriter<StaffCsv> writer =
                staffWriterDsl.to(fileWriter);
            errorList.forEach(CheckedConsumer.toConsumer(writer::append));
        } catch (IOException e) {
            log.debug("Error while writing staff csv file : {}",e.getMessage());
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
