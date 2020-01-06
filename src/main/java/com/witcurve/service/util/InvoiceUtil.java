package com.witcurve.service.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;
import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.InvoiceVM;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InvoiceUtil {

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentRepository studentRepository;

    Student student;


    public File generateInvoice(InvoiceVM invoiceVM) {
        //student = invoiceVM.getStudent();
        student = studentRepository.findById(invoiceVM.getStudent().getId()).get();
        try {

            HeaderTable event = new HeaderTable();
            Document document = new Document(PageSize.A4, 36, 36, 20 + event.headerTableHeight, 20+event.footerTableHeight);
            File file = WitcurveUtil.createTempFile("studentInvoice-"+ invoiceVM.getInvoiceNo() + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            writer.setPageEvent(event);
            document.open();
            addContent(document, student, invoiceVM.getInvoiceNo(), invoiceVM.getFeeDescriptions());
            document.close();
            return file;
        } catch (FileNotFoundException e) {
            throw new WitcurveException("Given url does not exists");
        } catch (DocumentException e) {
            throw new WitcurveException("Given file is already open");
        }

    }

    private void addContent(Document document, Student student, String invoiceNo, Map<String, Double> map) {
        createTable(document, invoiceNo, student);
        addEmptyLine(document, 1);
        addFeeDescriptionTable(document, map);

    }

    private void createTable(Document document, String invoiceNo, Student student) {
        List<StudentStandard> studentStandard = studentStandardRepository.getByStudentId(student.getId());
        if (studentStandard.size() == 0) {
            throw new WitcurveException("Given student is no more active");
        }
        try {
            float[] pointColumnWidths = {150F, 150F};
            BaseFont baseBold = BaseFont.createFont("src/main/resources/font/Poppins-Bold.otf", PdfEncodings.PDF_DOC_ENCODING, true);
            Font poppinsFont = new Font(baseBold, 16f, Font.BOLD, WebColors.getRGBColor("#000000"));
            Font poppinsSmallBold = new Font(baseBold, 12f, Font.BOLD, WebColors.getRGBColor("#000000"));
            Font poppinsVerySmallBold = new Font(baseBold, 10f, Font.BOLD, WebColors.getRGBColor("#000000"));
            Font greyFont = new Font(baseBold, 12f, Font.BOLD, WebColors.getRGBColor("#888383"));

            BaseFont baseNormal = BaseFont.createFont("src/main/resources/font/Poppins-Regular.ttf", PdfEncodings.IDENTITY_H, true);
            Font poppinsNormalFont = new Font(baseNormal, 10f, Font.NORMAL, WebColors.getRGBColor("#000000"));

            PdfPTable table = new PdfPTable(pointColumnWidths);
            table.setWidthPercentage(100);

            PdfPCell c1 = new PdfPCell(new Phrase("Fee Receipt", poppinsFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Invoice No  : " + invoiceNo, poppinsSmallBold));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //c1.setPaddingRight(47f);
            c1.setPaddingLeft(12f);
            c1.setPaddingTop(5f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            String date = WitcurveUtil.dateFormatter(LocalDate.now().toString(), WitCurveConstants.DEFAULT_DATE_FORMAT, WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
            c1 = new PdfPCell(new Phrase("Date Of Issue : " + date, poppinsVerySmallBold));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setPaddingRight(5f);
            c1.setPaddingLeft(12f);
            c1.setPaddingBottom(5f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            addEmptyCells(table, 3);

            c1 = new PdfPCell(new Phrase("Student Details", greyFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(""));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            addEmptyCells(table, 3);


            c1 = new PdfPCell(new Phrase("Name               : " +WitcurveUtil.getStudentName(student.getFirstName(),student.getMiddleName(),student.getLastName()), poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(15f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            if (student.getMotherName() == null) {
                c1 = new PdfPCell(new Phrase("Mother Name    : N/A", poppinsNormalFont));
            } else {
                c1 = new PdfPCell(new Phrase("Mother Name    : " + student.getMotherName(), poppinsNormalFont));
            }
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingLeft(40f);
            c1.setPaddingTop(15f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Admission Id  : " + student.getAdmissionId(), poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            if (student.getFatherName() == null) {
                c1 = new PdfPCell(new Phrase("Father Name     : N/A", poppinsNormalFont));
            } else {
                c1 = new PdfPCell(new Phrase("Father Name     : " + student.getFatherName(), poppinsNormalFont));
            }
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingLeft(40f);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Class                : " + studentStandard.get(0).getStandard().getGrade() + " " + studentStandard.get(0).getStandard().getSection(), poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            String dob = WitcurveUtil.dateFormatter(student.getDateOfBirth().toString(), WitCurveConstants.DEFAULT_DATE_FORMAT, WitCurveConstants.DEFAULT_IMPORT_DATE_FORMAT);
            c1 = new PdfPCell(new Phrase("Date Of Birth      : " + dob, poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingLeft(40f);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Roll No              : " + studentStandard.get(0).getRollNo(), poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" ", poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingLeft(40f);
            c1.setPaddingTop(10f);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            addEmptyCells(table, 3);

            document.add(table);
        } catch (DocumentException e) {
            throw new WitcurveException("Problem occured during adding table to document");
        } catch (IOException e) {
            throw new WitcurveException("Problem occured during adding font");

        }
    }

    private static void addEmptyLine(Document document, int number) {
        Paragraph paragraph = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        try {
            document.add(paragraph);
        } catch (DocumentException d) {
            throw new WitcurveException("Some problem occured while adding paragraph to document");
        }
    }


    private void addFeeDescriptionTable(Document document, Map<String, Double> feeDescriptions) {
        try {
            BaseFont baseNormal = BaseFont.createFont("src/main/resources/font/Poppins-Regular.ttf", PdfEncodings.IDENTITY_H, true);
            Font poppinsNormalFont = new Font(baseNormal, 10f, Font.NORMAL, WebColors.getRGBColor("#000000"));

            float[] pointColumnWidths = {150F, 150F};
            PdfPTable table = new PdfPTable(pointColumnWidths);
            table.setWidthPercentage(100);

            PdfPCell c1 = new PdfPCell(new Phrase("Fee Description", poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(Rectangle.BOTTOM);
            c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
            c1.setBorderColor(WebColors.getRGBColor("#800000"));
            c1.setPaddingRight(90f);
            c1.setPaddingTop(5f);
            c1.setPaddingBottom(10f);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Amount", poppinsNormalFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(Rectangle.BOTTOM);
            c1.setBorderColor(WebColors.getRGBColor("#800000"));
            c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
            c1.setPaddingTop(5f);
            c1.setPaddingBottom(10f);
            table.addCell(c1);

            Double totalAmt = 0.0;

            List<String> keys = feeDescriptions.keySet().stream().collect(Collectors.toList());
            for (int i = 0; i < keys.size(); i++) {
                c1 = new PdfPCell(new Phrase(keys.get(i), poppinsNormalFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(Rectangle.NO_BORDER);
                c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
                c1.setPaddingRight(90f);
                c1.setPaddingBottom(10f);
                table.addCell(c1);

                Double amt = feeDescriptions.get(keys.get(i));
                c1 = new PdfPCell(new Phrase(amt.toString(), poppinsNormalFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setBorder(Rectangle.NO_BORDER);
                c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
                c1.setPaddingBottom(10f);
                table.addCell(c1);
                totalAmt = totalAmt + amt;
            }
            int no = 10 - feeDescriptions.size();
            PdfPTable table1 = addEmptyCell(table, no);
            table1.completeRow();

            document.add(table1);
            addEmptyLine(document, 1);
            addTotalTable(document, totalAmt);
        } catch (IOException e) {
            throw new WitcurveException("Some problem occured while adding Font");
        } catch (DocumentException e) {
            throw new WitcurveException("\"Some problem occured while adding table to document\"");
        }
    }

    private String getImageUrl() {
        String subDomainName = student.getSchoolInfo().getSchool().getInstitute().getSubDomainName();
        Long id = student.getSchoolInfo().getSchool().getInstitute().getId();
        String headerUrl = "https://" + subDomainName + ".witcurve-app.com/assets/images/header-logo/" + 1000 + "-header-logo.png";
        return headerUrl;
    }

    private void addTotalTable(Document document, Double totalAmt) {
        try {
            BaseFont baseBold = BaseFont.createFont("src/main/resources/font/Poppins-Bold.otf", PdfEncodings.PDF_DOC_ENCODING, true);
            Font poppinsFont = new Font(baseBold, 16f);
            Font poppinsWhiteFont = new Font(baseBold, 16f, Font.BOLD, WebColors.getRGBColor("#ffffff"));

            float[] pointColumnWidths = {50F, 20F};
            PdfPTable table = new PdfPTable(pointColumnWidths);
            table.setWidthPercentage(80);

            PdfPCell c1 = new PdfPCell(new Phrase("Total", poppinsFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setPaddingLeft(200f);
            c1.setFixedHeight(40f);
            c1.setVerticalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(10f);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("\u20B9" + "  " + totalAmt.toString(), poppinsWhiteFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setCellEvent(new RoundedBorder());
            c1.setPaddingRight(10f);
            c1.setPaddingTop(10f);
            c1.setFixedHeight(40f);
            table.addCell(c1);

            document.add(table);
        } catch (DocumentException e) {
            throw new WitcurveException("Some problem occured while adding total table to document");
        } catch (IOException e) {
            throw new WitcurveException("Some problem occured while adding Font");
        }
    }

    private PdfPTable addEmptyCell(PdfPTable table, int no) {
        PdfPCell c1;
        for (int i = 0; i < no; i++) {
            c1 = new PdfPCell(new Phrase(""));
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
            c1.setPaddingTop(5f);
            c1.setPaddingBottom(10f);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(""));
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
            c1.setPaddingTop(5f);
            c1.setPaddingBottom(10f);
            table.addCell(c1);
        }
        return table;
    }

    private PdfPTable addEmptyCells(PdfPTable table, int no) {
        PdfPCell c1;
        for (int i = 0; i < no; i++) {
            c1 = new PdfPCell(new Phrase(""));
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(""));
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);
        }
        return table;
    }


    class RoundedBorder implements PdfPCellEvent {

        @Override
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f,
                rect.getBottom() + 1.5f,
                rect.getWidth() - 3,
                rect.getHeight() - 3, 4
            );
            cb.setColorFill(WebColors.getRGBColor("#71796f"));
            cb.fill();
        }
    }

      class HeaderTable extends PdfPageEventHelper {
          protected PdfPTable headertable;
          protected float headerTableHeight;
          protected PdfPTable footertable;
          protected float footerTableHeight;

          public HeaderTable() {
              headertable = new PdfPTable(1);
              headertable.setTotalWidth(523);
              headertable.getDefaultCell().setBorder(0);
              headertable.setLockedWidth(true);
              footertable = new PdfPTable(1);
              footertable.setTotalWidth(523);
              footertable.getDefaultCell().setBorder(0);
              footertable.setLockedWidth(true);

              try {
                  Image img = Image.getInstance(InvoiceUtil.this.getImageUrl());
                  img.scaleToFit(140, 100);
                  img.setAbsolutePosition(420, 755);
                  headertable.addCell(img);

                  headerTableHeight = headertable.getTotalHeight();

                  BaseFont baseNormal = BaseFont.createFont("src/main/resources/font/Poppins-Regular.ttf", PdfEncodings.IDENTITY_H, true);
                  Font poppinsNormalFont = new Font(baseNormal, 10f, Font.NORMAL, WebColors.getRGBColor("#000000"));
                  PdfPCell c1 = new PdfPCell(new Phrase("Note : This is system generated receipt. For further detail please contact school administration",poppinsNormalFont));
                  c1.setBorder(Rectangle.NO_BORDER);
                  c1.setBackgroundColor(WebColors.getRGBColor("F0F6F6"));
                  c1.setFixedHeight(30f);
                  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                  c1.setPaddingTop(10f);

                  footertable.addCell(c1);
                  footerTableHeight = footertable.getTotalHeight();


              } catch (BadElementException e) {
                  throw new WitcurveException("Image is bad", e);
              } catch (IOException e) {
                  throw new WitcurveException("Image not found", e);
              } catch (DocumentException e) {
                  throw new WitcurveException("Error occured while adding font", e);
              }
          }

          public float getHeaderTableHeight() {
              return headerTableHeight;
          }

          public float getFooterTableHeight(){ return footerTableHeight;}

          public void onEndPage(PdfWriter writer, Document document) {
              headertable.writeSelectedRows(0, -1,
                  document.left(),
                  document.top() + ((document.topMargin() + headerTableHeight) / 2),
                  writer.getDirectContent());

              footertable.writeSelectedRows(0,-1, document.left(),
                  document.bottom()-10,
                  writer.getDirectContent());


          }
      }
}


