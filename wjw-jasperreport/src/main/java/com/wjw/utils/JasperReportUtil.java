package com.wjw.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;

@Slf4j
public class JasperReportUtil {

    final static String jasperDir = "jaspers";
    final static String suffix = ".jasper";
    final static String suffix_jasper = ".jasper";
    final static String suffix_jrxml = ".jrxml";
    
    public enum ReportType {
        HTML,
        PDF,
        XLS,
        XLSX,
        XML,
        RTF,
        CSV,
        DOC
    }

    public static String getJasperFileDir(String fileName) {
        return jasperDir + File.separator + fileName + suffix;
    }

    private static String getContentType(ReportType type) {
        String contentType;
        switch (type) {
            case HTML:
                contentType = "text/html;charset=utf-8";
                break;
            case PDF:
                contentType = "application/pdf";
                break;
            case XLS:
                contentType = "application/vnd.ms-excel";
                break;
            case XLSX:
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case XML:
                contentType = "text/xml";
                break;
            case RTF:
                contentType = "application/rtf";
                break;
            case CSV:
                contentType = "text/plain";
                break;
            case DOC:
                contentType = "application/msword";
                break;
            default:
                contentType = "text/html;charset=utf-8";
        }
        return contentType;
    }

    static JasperPrint getJasperPrint(InputStream jasperStream, Map<String, Object> parameters, List<?> list) throws JRException, IOException {
        JRDataSource dataSource = null;
        if (null == list || list.size() == 0) {
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRBeanCollectionDataSource(list);
        }
        
        JasperPrint jasperPrint = null;
        if (suffix.equals(suffix_jasper)) {
            jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, dataSource);
        } else if (suffix.equals(suffix_jrxml)) {
            JasperDesign jasperDesign = JRXmlLoader.load(jasperStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperCompileManager.compileReportToFile(jasperDesign, "jaspers/export.jasper");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        }
        File directory = new ClassPathResource("").getFile();
        File reportFile = File.createTempFile("exportReport", ".pdf", directory);
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportFile.getAbsolutePath());
        System.out.println(reportFile.getPath());
        Files.delete(reportFile.toPath());
        return jasperPrint;
    }

    public static void exportToPdf(String jasperPath, Map<String, Object> parameters, List<?> list, HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            response.setContentType(getContentType(ReportType.PDF));
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (Exception e) {
            log.error("读取报表异常", e);
            outputStream.write("读取报表异常".getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }


    public static void exportToXml(String jasperPath, Map<String, Object> parameters, List<?> list, HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            response.setContentType(getContentType(ReportType.XML));
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);
            JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
        } catch (Exception e) {
            log.error("读取报表异常", e);
            outputStream.write("读取报表异常".getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    public static void exportToHtml(String jasperPath, Map<String, Object> parameters, List<?> list, HttpServletResponse response) throws Exception {
        response.setHeader("Content-type", "text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType(getContentType(ReportType.HTML));
        PrintWriter out = response.getWriter();
        HtmlExporter exporter = new HtmlExporter();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
            exporter.setConfiguration(configuration);
            HtmlExporterOutput outPut = new SimpleHtmlExporterOutput(out);
            exporter.setExporterOutput(outPut);
            exporter.exportReport();
        } catch (Exception e) {
            log.error("读取报表异常", e);
            out.write("读取报表异常");
        } finally {
            out.flush();
            out.close();
        }
    }
    
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    public static final SimpleDateFormat datetTimeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss z", Locale.US);
    
    public static String dateFormat(Date date) {
        return dateFormat.format(date);
    }
    
    public static String dateTimeFormat(Date date) {
        return dateTimeFormat(date);
    }

}
