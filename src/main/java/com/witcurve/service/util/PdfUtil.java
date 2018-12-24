package com.witcurve.service.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PdfUtil {

    public static void main(String[] args) throws IOException {

        createPDF();
    }

    public static void createPDF() throws IOException {
        final PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        doc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(doc, page);

        createTitle(stream, mediaBox);

        createFooter(stream, mediaBox);

        stream.close();
        doc.save("testPDF.pdf");

    }

    private static void createText(PDPageContentStream stream, PDRectangle mediaBox, String text, PDFont font) throws IOException {
        if (font == null) {
            font = PDType1Font.HELVETICA_BOLD;
        }
        int marginTop = 30;
        int fontSize = 16;

        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = (mediaBox.getWidth() - titleWidth) / 2;
        float startY = mediaBox.getHeight() - marginTop - titleHeight;

        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(startX, startY);
        stream.showText(text);
        stream.endText();
    }

    private static void createTitle(PDPageContentStream stream, PDRectangle mediaBox) throws IOException {
        String text = "Monthly Salary Slip";
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int marginTop = 30;
        int fontSize = 16;

        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        float startX = (mediaBox.getWidth() - titleWidth) / 2;
        float startY = mediaBox.getHeight() - marginTop - titleHeight;

        writeToStream(stream, font, fontSize, startX, startY, text);
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

    private static void writeToStream(PDPageContentStream stream, PDFont font, int fontSize, float startX, float startY, String text) throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(startX, startY);
        stream.showText(text);
        stream.endText();
    }
}
