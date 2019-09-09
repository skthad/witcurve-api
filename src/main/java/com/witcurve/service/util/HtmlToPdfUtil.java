package com.witcurve.service.util;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.witcurve.web.rest.errors.WitcurveException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class HtmlToPdfUtil {

    private final Logger log = LoggerFactory.getLogger(HtmlToPdfUtil.class);

    public File htmlToPdf(File htmlFile) {
        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(true);
            webClient.waitForBackgroundJavaScript(1 * 1000);
            String url = Paths.get(htmlFile.getAbsolutePath()).toUri().toURL().toString();
            HtmlPage page = webClient.getPage(url);
            String xml = page.asXml();
            String script = StringUtils.substringBetween(xml, "<script type=\"text/javascript\">", "</script>");
            script = script.replace("<script type=\"text/javascript\"></script>", "");
            xml = xml.replace(script, "");
            File inputFile = WitcurveUtil.createTempFile("input.html");
            FileWriter fw=new FileWriter(inputFile);
            fw.write(xml);
            fw.close();
            File output = WitcurveUtil.createTempFile("output.pdf");
            PdfWriter writer = new PdfWriter(output);
            HtmlConverter.convertToPdf(xml, writer);
            return output;
        } catch (IOException e) {
            log.debug("There was problem while reading while converting template, : {}", e.getMessage());
            throw new WitcurveException("There was problem while reading while converting template : " + e.getMessage());
        } catch (ScriptException | IllegalStateException e) {
            log.debug("There was problem while parsing html file : {}", e.getMessage());
            throw new WitcurveException("There was problem while parsing html file : " + e.getMessage());
        }
    }

    public File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}
