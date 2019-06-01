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

    private static float cursorX_LEFT = 20f;
    private static float cursorX_RIGHT = 25f;
    private static float cursorY_TOP = 25f;
    private static float cursorY_BOTTOM = 15f;
    private static float cursor_CENTER = 0f;
    private static PDFont font = PDType1Font.HELVETICA_BOLD;
    private static int fontSize = 12;

    public static void main(String[] args) throws IOException {

        // input parameter
        SchoolInfoDTO schoolInfo = new SchoolInfoDTO();

        PayrollDetailsDTO payrollDetailsDTO = new PayrollDetailsDTO();
        payrollDetailsDTO.setBasicSalary(12000d);
        payrollDetailsDTO.setHouseRentAllowance(6000d);
        payrollDetailsDTO.setConveyanceAllowance(1000d);
        payrollDetailsDTO.setMedicalAllowance(6000d);
        payrollDetailsDTO.setManagerialAllowance(4000d);
        payrollDetailsDTO.setLeaveTravelAllowance(2300d);

        payrollDetailsDTO.setProvidentFund(997d);
        payrollDetailsDTO.setProfessionalTax(1300d);

        PayrollDTO payrollDTO = new PayrollDTO();

        payrollDTO.setPaidDays(21d);
        payrollDTO.setPayableDays(22d);
        payrollDTO.setIncomeTax(2200d);
        payrollDTO.setBonus(11000d);
        payrollDTO.setPayrollDetails(payrollDetailsDTO);

        createPDF(schoolInfo, payrollDTO);
    }

    public static void createPDF(SchoolInfoDTO schoolInfo, PayrollDTO payrollDTO) throws IOException {
        final PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        PDRectangle mediaBox = page.getMediaBox();
        cursorX_RIGHT = mediaBox.getWidth() - cursorX_RIGHT;
        cursorY_TOP = mediaBox.getHeight() - cursorY_TOP;
        cursor_CENTER = (cursorX_LEFT + cursorX_RIGHT) / 2;

        PDPageContentStream stream = new PDPageContentStream(doc, page);

        // createGrid(stream, mediaBox);
        createHeader(stream, mediaBox, doc, schoolInfo);
        createFooter(stream, mediaBox);
        createContent(stream, payrollDTO);
        createNote(stream);

        stream.close();
        doc.save("testPDF.pdf");

    }

    private static void createHeader(PDPageContentStream stream, PDRectangle mediaBox, PDDocument doc, SchoolInfoDTO schoolInfo) throws IOException {

        createSchoolInfo(stream, schoolInfo);

        float imageSize = 50f;
        stream.drawImage(PDImageXObject.createFromFile("dpis.jpg", doc), cursorX_RIGHT - imageSize, cursorY_TOP, imageSize, imageSize);

        createTitle(stream, mediaBox);
    }

    private static void createFooter(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {

        int fSize = fontSize;
        String text = "Powered by WitCurve" + String.valueOf(Character.toChars(169));
        PDFont font = PDType1Font.COURIER;

        float titleWidth = font.getStringWidth(text) / 1000 * fSize;

        writeToStream(stream, font, fSize, cursor_CENTER - titleWidth / 2, cursorY_BOTTOM, text);
        cursorY_BOTTOM += 20;

        stream.addRect(0, cursorY_BOTTOM, mediaBox.getWidth(), 2);
        stream.fill();
    }

    private static void createContent(PDPageContentStream stream, PayrollDTO payrollDTO) throws IOException {

        int fSize = fontSize;

        cursorX_LEFT += 30;
        cursorY_TOP -= 20;
        createEmployeeInfoRight(stream, payrollDTO);
        createEmployeeInfoLeft(stream);

        cursorX_LEFT += 20;

        cursorY_TOP -= 20;
        float totalEarnings = createEarnings(stream, payrollDTO);

        cursorY_TOP -= 20;
        float totalDeductions = createDeductions(stream, payrollDTO);

        stream.addRect(cursorX_LEFT - 15, cursorY_TOP - 10, 400, 1);
        stream.fill();

        double earnings = ((double) payrollDTO.getPaidDays() / payrollDTO.getPayableDays()) * totalEarnings;
        double netSalary = earnings - totalDeductions;

        cursorY_TOP -= 30;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Net Salary: ");
        writeToStream(stream, font, fSize, cursor_CENTER + 50, cursorY_TOP,
            "    Rs. " + Math.round(netSalary * 100.0)/100.0);
    }

    private static void createNote(PDPageContentStream stream) throws IOException {
        int fSize = fontSize - 3;
        cursorY_TOP -= 150;
        cursorY_BOTTOM += 20;
        cursorX_LEFT += 20;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_BOTTOM, "3. Only non-zero figures are shown above. ");
        cursorY_BOTTOM += 15;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_BOTTOM, "2. You can get the excel version... ");
        cursorY_BOTTOM += 15;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_BOTTOM, "1. Net Salary is calculated as ... ");
        cursorY_BOTTOM += 15;

        cursorX_LEFT -= 20;

        stream.addRect(cursorX_LEFT - 5, cursorY_BOTTOM, 75, 1);
        stream.fill();

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_BOTTOM + 5, "PLEASE NOTE: ");
        cursorY_BOTTOM += 15;
    }

    private static void createSchoolInfo(PDPageContentStream stream, SchoolInfoDTO schoolInfo) throws IOException {
        String text = "Delhi Public International School"; //schoolInfo.getSchool().getName();
        int fSize = fontSize + 8;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        cursorY_TOP = cursorY_TOP - titleHeight;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, text);
        fSize = fontSize - 3;
        titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;
        cursorY_TOP = cursorY_TOP - titleHeight - 5;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Address 1, (Address 2)");

        cursorY_TOP = cursorY_TOP - titleHeight - 5;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "City, State, Zip etc.");
    }

    private static void createTitle(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {

        cursorY_TOP = cursorY_TOP - 15;

        String text = "Salary Slip - December 2018";
        int fSize = fontSize + 4;

        float titleWidth = font.getStringWidth(text) / 1000 * fSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        stream.addRect(0, cursorY_TOP, mediaBox.getWidth(), 2);
        stream.fill();

        cursorY_TOP -= 10 + titleHeight;

        writeToStream(stream, font, fSize, cursor_CENTER - titleWidth/2, cursorY_TOP, text);
    }

    private static void createEmployeeInfoRight(PDPageContentStream stream, PayrollDTO payrollDTO) throws IOException {
        int fSize = fontSize - 2;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        float startY = cursorY_TOP - titleHeight;

        writeToStream(stream, font, fSize, cursor_CENTER, startY, "Joined On: XX/XX/XXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fSize, cursor_CENTER, startY, "Location: XX, XXXXXX XXXX");

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fSize, cursor_CENTER, startY, "Total Days: " + payrollDTO.getPayableDays());

        startY = startY - titleHeight - 5;
        writeToStream(stream, font, fSize, cursor_CENTER, startY, "Paid Days: " + payrollDTO.getPaidDays());

    }

    private static void createEmployeeInfoLeft(PDPageContentStream stream) throws IOException {
        int fSize = fontSize - 2;

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        cursorY_TOP -= titleHeight;

        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Employee ID: XXXXXXXXX");

        cursorY_TOP -= titleHeight + 5;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Name: XX XXXXX XXX");

        cursorY_TOP -= titleHeight + 5;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Designation: XXXXX");

        cursorY_TOP -= titleHeight + 5;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "D.O.B.: XX/XX/XXXX");
    }

    private static float createEarnings(PDPageContentStream stream, PayrollDTO payrollDTO) throws IOException {

        float totalEarnings = 0.0f;
        int fSize = fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        cursorY_TOP -= titleHeight;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "EARNINGS");

        cursorX_LEFT += 20;
        fSize = fontSize - 2;
        titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        cursorY_TOP -= titleHeight + 5;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Basic Salary: ");
        writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getBasicSalary());
        totalEarnings += payrollDTO.getPayrollDetails().getBasicSalary();

        if (payrollDTO.getPayrollDetails().getHouseRentAllowance() != null && payrollDTO.getPayrollDetails().getHouseRentAllowance() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "House Rent Allowance: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getHouseRentAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getHouseRentAllowance();
        }

        if (payrollDTO.getPayrollDetails().getConveyanceAllowance() != null && payrollDTO.getPayrollDetails().getConveyanceAllowance() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Conveyance Allowance: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getConveyanceAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getConveyanceAllowance();
        }

        if (payrollDTO.getPayrollDetails().getMedicalAllowance() != null && payrollDTO.getPayrollDetails().getMedicalAllowance() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Medical Allowance: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getMedicalAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getMedicalAllowance();
        }

        if (payrollDTO.getPayrollDetails().getManagerialAllowance() != null && payrollDTO.getPayrollDetails().getManagerialAllowance() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Basic Salary: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getManagerialAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getManagerialAllowance();
        }

        if (payrollDTO.getPayrollDetails().getLeaveTravelAllowance() != null && payrollDTO.getPayrollDetails().getLeaveTravelAllowance() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Leave Travel Allowance: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getLeaveTravelAllowance());
            totalEarnings += payrollDTO.getPayrollDetails().getLeaveTravelAllowance();
        }

        if (payrollDTO.getBonus() != null && payrollDTO.getBonus() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Bonus: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getBonus() );
            totalEarnings += payrollDTO.getBonus();
        }

        if (payrollDTO.getMiscEarnings() != null && payrollDTO.getMiscEarnings() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Misc. Earnings: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getMiscEarnings());
            totalEarnings += payrollDTO.getMiscEarnings();
        }

        fSize += 1;
        cursorY_TOP -= titleHeight + 15;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Total Earnings: ");
        writeToStream(stream, font, fSize, cursor_CENTER + 50, cursorY_TOP, "(+) Rs. " + totalEarnings);

        cursorX_LEFT -= 20;

        return totalEarnings;
    }

    private static float createDeductions(PDPageContentStream stream, PayrollDTO payrollDTO) throws IOException {

        float totalDeductions = 0.0f;
        int fSize = fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        cursorY_TOP -= titleHeight;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "DEDUCTIONS");

        cursorX_LEFT += 20;
        fSize = fontSize - 2;
        titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fSize;

        if (payrollDTO.getPayrollDetails().getProvidentFund() != null && payrollDTO.getPayrollDetails().getProvidentFund() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Provident Fund: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getProvidentFund());
            totalDeductions += payrollDTO.getPayrollDetails().getProvidentFund();
        }

        if (payrollDTO.getPayrollDetails().getProfessionalTax() != null && payrollDTO.getPayrollDetails().getProfessionalTax() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Professional Tax: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getPayrollDetails().getProfessionalTax());
            totalDeductions += payrollDTO.getPayrollDetails().getProfessionalTax();
        }

        if (payrollDTO.getIncomeTax() != null && payrollDTO.getIncomeTax() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Income Tax: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getIncomeTax());
            totalDeductions += payrollDTO.getIncomeTax();
        }

        if (payrollDTO.getLateDays() != null && payrollDTO.getLateDays() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Late Entry Deductions: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getLateDays());
            totalDeductions += payrollDTO.getLateDays();
        }

        if (payrollDTO.getMiscDeductions() != null && payrollDTO.getMiscDeductions() > 0) {
            cursorY_TOP -= titleHeight + 5;
            writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Misc. Deductions: ");
            writeToStream(stream, font, fSize, cursor_CENTER, cursorY_TOP, "Rs. " + payrollDTO.getMiscDeductions());
            totalDeductions += payrollDTO.getMiscDeductions();
        }

        fSize += 1;
        cursorY_TOP -= titleHeight + 15;
        writeToStream(stream, font, fSize, cursorX_LEFT, cursorY_TOP, "Total Deductions: ");
        writeToStream(stream, font, fSize, cursor_CENTER + 50, cursorY_TOP, "(-) Rs. " + totalDeductions);

        cursorX_LEFT -= 20;
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
