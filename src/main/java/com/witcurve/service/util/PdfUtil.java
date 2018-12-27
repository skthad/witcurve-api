package com.witcurve.service.util;

import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

public class PdfUtil {

    public static void main(String[] args) throws IOException {

        // input parameter
        SchoolInfoDTO schoolInfo = new SchoolInfoDTO();

        PayrollDetailsDTO payrollDetailsDTO = new PayrollDetailsDTO();
        payrollDetailsDTO.setBasicSalary(12000f);
        payrollDetailsDTO.setHouseRentAllowance(6000f);
        payrollDetailsDTO.setConveyanceAllowance(1000f);
        payrollDetailsDTO.setMedicalAllowance(6000f);
        payrollDetailsDTO.setManagerialAllowance(4000f);
        payrollDetailsDTO.setLeaveTravelAllowance(2300f);

        payrollDetailsDTO.setProvidentFund(997f);
        payrollDetailsDTO.setProfessionalTax(1300f);
        payrollDetailsDTO.setIncomeTax(2200f);
        payrollDetailsDTO.setLateEntryDeductions(0.0f);

        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setPayrollDetails(payrollDetailsDTO);
        payrollDTO.setBonus(11000f);

        createPDF(schoolInfo, payrollDTO);
    }

    public static void createPDF(SchoolInfoDTO schoolInfo, PayrollDTO payrollDTO) throws IOException {
        final PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        doc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(doc, page);

        // createGrid(stream, mediaBox);
        createHeader(stream, mediaBox, doc, schoolInfo);
        createContent(stream, mediaBox, payrollDTO);
        createFooter(stream, mediaBox);

        stream.close();
        doc.save("testPDF.pdf");

    }

    private static void createHeader(PDPageContentStream stream, PDRectangle mediaBox, PDDocument doc, SchoolInfoDTO schoolInfo) throws IOException {
        stream.drawImage(PDImageXObject.createFromFile("dpis.jpg", doc), mediaBox.getWidth() - 75, mediaBox.getHeight() - 75, 50, 50);
        createSchoolInfo(stream, mediaBox, schoolInfo);
        createTitle(stream, mediaBox);
    }

    private static void createContent(PDPageContentStream stream, PDRectangle mediaBox, PayrollDTO payrollDTO) throws IOException {

        int totalDays = 22;
        int paidDays = 21;

        PDType1Font font = PDType1Font.HELVETICA_BOLD;
        int fontSize = 12;

        createEmployeeInfoLeft(stream, mediaBox);
        createEmployeeInfoRight(stream, mediaBox);

        float totalEarnings = createEarnings(stream, mediaBox, payrollDTO);
        float totalDeductions = createDeductions(stream, mediaBox, payrollDTO);

        stream.addRect(60, mediaBox.getHeight() - 600, 400, 1);
        stream.fill();

        double earnings = ((double) paidDays / totalDays) * totalEarnings;
        double netSalary = earnings - totalDeductions;
        writeToStream(stream, font, fontSize, 80, mediaBox.getHeight() - 625, "Net Salary: ");
        writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2 + 50, mediaBox.getHeight() - 625,
            "    Rs. " + Math.round(netSalary * 100.0)/100.0);
    }

    private static void createFooter(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {

        String text = "Powered by WitCurve" + String.valueOf(Character.toChars(169));
        PDFont font = PDType1Font.COURIER;
        int marginBottom = 30;
        int fontSize = 12;

        stream.addRect(0, 40, mediaBox.getWidth(), 1);
        stream.fill();

        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = (mediaBox.getWidth() - titleWidth) / 2;
        float startY = marginBottom - titleHeight;

        writeToStream(stream, font, fontSize, startX, startY, text);
    }

    private static void createSchoolInfo(PDPageContentStream stream, PDRectangle mediaBox, SchoolInfoDTO schoolInfo) throws IOException {
        String text = "Delhi Public International School"; //schoolInfo.getSchool().getName();
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int margin = 18;
        int fontSize = 20;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = margin;
        float startY = mediaBox.getHeight() - margin - titleHeight;

        writeToStream(stream, font, fontSize, startX, startY, text);
        fontSize = 9;
        titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
        startY = startY - titleHeight - 5;

        writeToStream(stream, font, fontSize, startX, startY, "Address 1, (Address 2)");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "City, State, Zip etc.");
    }

    private static void createTitle(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {
        String text = "Salary Slip - December 2018";
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int marginTop = 100;
        int fontSize = 16;

        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = (mediaBox.getWidth() - titleWidth) / 2;
        float startY = mediaBox.getHeight() - marginTop;

        stream.addRect(0, startY + 10, mediaBox.getWidth(), 2);
        stream.fill();

        startY -= titleHeight;

        writeToStream(stream, font, fontSize, startX, startY, text);
    }

    private static void createEmployeeInfoLeft(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int margin = 50;
        int fontSize = 10;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = margin;
        float startY = mediaBox.getHeight() - 150;

        writeToStream(stream, font, fontSize, startX, startY, "Employee ID: XXXXXXXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "Name: XX XXXXX XXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "Designation: XXXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "D.O.B.: XX/XX/XXXX");
    }

    private static void createEmployeeInfoRight(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int fontSize = 10;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = mediaBox.getWidth() / 2;
        float startY = mediaBox.getHeight() - 150;

        writeToStream(stream, font, fontSize, startX, startY, "Joined On: XX/XX/XXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "Location: XX, XXXXXX XXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "Total Days: XXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fontSize, startX, startY, "Paid Days: XX");
    }

    private static float createEarnings(PDPageContentStream stream, PDRectangle mediaBox, PayrollDTO payrollDTO) throws IOException {

        float totalEarnings = 0.0f;

        PDFont font = PDType1Font.HELVETICA_BOLD;
        int margin = 80;
        int fontSize = 12;

        float startX = margin;
        float startY = mediaBox.getHeight() - 250;

        writeToStream(stream, font, fontSize, startX, startY, "EARNINGS");

        startX = startX + 20;
        fontSize = 10;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        if (payrollDTO.getPayrollDetails().getBasicSalary() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Basic Salary: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getBasicSalary());
            totalEarnings += payrollDTO.getPayrollDetails().getBasicSalary();
        }

        if (payrollDTO.getPayrollDetails().getHouseRentAllowance() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "House Rent Allowance: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getHouseRentAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getHouseRentAllowance();
        }

        if (payrollDTO.getPayrollDetails().getConveyanceAllowance() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Conveyance Allowance: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getConveyanceAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getConveyanceAllowance();
        }

        if (payrollDTO.getPayrollDetails().getMedicalAllowance() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Medical Allowance: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getMedicalAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getMedicalAllowance();
        }

        if (payrollDTO.getPayrollDetails().getManagerialAllowance() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Basic Salary: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getManagerialAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getManagerialAllowance();
        }

        if (payrollDTO.getPayrollDetails().getLeaveTravelAllowance() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Leave Travel Allowance: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getLeaveTravelAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getLeaveTravelAllowance();
        }

        if (payrollDTO.getBonus() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Bonus: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getBonus() );
            totalEarnings += payrollDTO.getBonus();
        }

        if (payrollDTO.getPayrollDetails().getMiscEarnings() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Misc. Earnings: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getMiscEarnings());
            totalEarnings += payrollDTO.getPayrollDetails().getMiscEarnings();
        }

        startY = startY - titleHeight - 15;
        writeToStream(stream, font, fontSize + 1, startX, startY, "Total Earnings: ");
        writeToStream(stream, font, fontSize + 1, mediaBox.getWidth() / 2 + 50, startY, "(+) Rs. " + totalEarnings);

        return totalEarnings;
    }

    private static float createDeductions(PDPageContentStream stream, PDRectangle mediaBox, PayrollDTO payrollDTO) throws IOException {

        float totalDeductions = 0.0f;

        PDFont font = PDType1Font.HELVETICA_BOLD;
        int margin = 80;
        int fontSize = 12;

        float startX = margin;
        float startY = mediaBox.getHeight() - 450;

        writeToStream(stream, font, fontSize, startX, startY, "DEDUCTIONS");

        startX = startX + 20;
        fontSize = 10;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        if (payrollDTO.getPayrollDetails().getProvidentFund() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Provident Fund: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getProvidentFund());
            totalDeductions += payrollDTO.getPayrollDetails().getProvidentFund();
        }

        if (payrollDTO.getPayrollDetails().getProfessionalTax() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Professional Tax: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getProfessionalTax());
            totalDeductions += payrollDTO.getPayrollDetails().getProfessionalTax();
        }

        if (payrollDTO.getPayrollDetails().getIncomeTax() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Income Tax: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getIncomeTax());
            totalDeductions += payrollDTO.getPayrollDetails().getIncomeTax();
        }

        if (payrollDTO.getPayrollDetails().getLateEntryDeductions() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Late Entry Deductions: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getLateEntryDeductions());
            totalDeductions += payrollDTO.getPayrollDetails().getLateEntryDeductions();
        }

        if (payrollDTO.getLossOfPay() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Loss of Pay: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getLossOfPay());
            totalDeductions += payrollDTO.getLossOfPay();
        }

        if (payrollDTO.getPayrollDetails().getMiscDeductions() != null) {
            startY = startY - titleHeight - 5;
            writeToStream(stream, font, fontSize, startX, startY, "Misc. Deductions: ");
            writeToStream(stream, font, fontSize, mediaBox.getWidth() / 2, startY, "Rs. " + payrollDTO.getPayrollDetails().getMiscDeductions());
            totalDeductions += payrollDTO.getPayrollDetails().getMiscDeductions();
        }

        startY = startY - titleHeight - 15;
        writeToStream(stream, font, fontSize + 1, startX, startY, "Total Deductions: ");
        writeToStream(stream, font, fontSize + 1, mediaBox.getWidth() / 2 + 50, startY, "(-) Rs. " + totalDeductions);

        return totalDeductions;
    }

    private static void writeToStream(PDPageContentStream stream, PDFont font, int fontSize, float startX, float startY, String text) throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(startX, startY);
        stream.showText(text);
        stream.endText();
    }

    private static void createGrid(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {
        float startX = 0;
        float startY = 0;

        int hor = (int) mediaBox.getHeight() / 5;
        int ver = (int) mediaBox.getWidth() / 5;

        while (hor > 0) {
            stream.addRect(startX, mediaBox.getHeight() - startY, mediaBox.getWidth(), 1);
            stream.fill();
            writeToStream(stream, PDType1Font.HELVETICA_BOLD, 4, 2, startY + 3, String.valueOf(hor));
            startY = startY + 5;
            hor--;
        }
        for (int x = 1; x <= ver; x++) {
            stream.addRect(startX, 0, 1, mediaBox.getHeight());
            stream.fill();
            writeToStream(stream, PDType1Font.HELVETICA_BOLD, 4, startX + 3, 0, String.valueOf(x));
            startX = startX + 5;
        }
    }
}
