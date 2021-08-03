package core.util.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import core.controller.Tag;
import core.util.platform.environment.TestEnvironment;
import core.util.platform.host.file.FileHelper;
import core.util.platform.host.file.YamlLoader;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class GeneratePdf {
    protected static final Logger LOGGER = LoggerFactory.getLogger(GeneratePdf.class);
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);
    private static Font failFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.RED);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font passFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 153, 76));
    private static Font skipFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(204, 102, 0));
    private static Font tableHeaderBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
    private static Font tableDetailHeaderBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);
    private static Font tableCellText = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
    private static Font tableCellValue = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);


    public static void toExecute(File sPdfReports, String timeStamp, ReportResult reportResult) {
        try {
            LOGGER.info("START GENERATE PDF");
            toExecuteCucumberReport(sPdfReports, timeStamp, reportResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaDataCucumberPDF(Document document) {
        document.addTitle("Cucumber Report PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Automation Team");
        document.addCreator("Automation Team");
        document.addHeader("Test", "Selenium");
    }

    //PDF for Cucumber Report
    public static void toExecuteCucumberReport(File pdfReports, String timeStamp, ReportResult reportResult) {
        PdfWriter writer = null;
        // String FILE = sTestngReports;
        try {
            Document document = new Document();
            writer = PdfWriter.getInstance(document, new FileOutputStream(pdfReports));
            document.open();
            addMetaDataCucumberPDF(document);
            addTitlePageCucumberPDF(document, timeStamp, reportResult);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static PdfPCell createImageCellCucumberPDF(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidth((float) 0.05);
        return cell;
    }

    private static PdfPCell createHeaderCell(String headerText, int colspan, BaseColor backgroundColor, Font fontHeader) {
        PdfPCell c1 = new PdfPCell(new Phrase(headerText, fontHeader));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(backgroundColor);
        if (colspan != 0) {
            c1.setColspan(colspan);

        }

        return c1;
    }


    private static void createDetailTable(Paragraph preface, ReportResult reportResult) throws DocumentException, IOException {
        String tagName = "";
        int totalTags = reportResult.getAllTags().size();
        String packageName = "";
        String env = TestEnvironment.getRunningEnvironment().toString();

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);
        float[] columnWidths = new float[11];
        columnWidths[0] = (float) 1.2;
        for (int i = 1; i < 11; i++) {
            columnWidths[i] = (float) 0.9;
        }
        table.setWidths(columnWidths);


        BaseColor backgroundHeader = new BaseColor(102, 204, 238);
        //Create header row
        table.addCell(createHeaderCell("", 0, backgroundHeader, tableHeaderBold));
        table.addCell(createHeaderCell("", 0, backgroundHeader, tableHeaderBold));
        table.addCell(createHeaderCell("Steps", 6, backgroundHeader, tableHeaderBold));
        table.addCell(createHeaderCell("Scenarios", 3, backgroundHeader, tableHeaderBold));
        table.setHeaderRows(1);
        table.addCell(createHeaderCell("Channel", 0, BaseColor.WHITE, tableDetailHeaderBold));
        table.addCell(createHeaderCell("Features", 0, BaseColor.WHITE, tableDetailHeaderBold));
        table.addCell(createHeaderCell("Passed", 0, new BaseColor(149, 191, 91), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Failed", 0, new BaseColor(209, 89, 32), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Skipped", 0, new BaseColor(136, 170, 255), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Pending", 0, new BaseColor(245, 242, 143), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Undefined", 0, new BaseColor(245, 185, 117), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Total", 0, new BaseColor(211, 211, 211), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Passed", 0, new BaseColor(149, 191, 91), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Failed", 0, new BaseColor(209, 89, 32), tableDetailHeaderBold));
        table.addCell(createHeaderCell("Total", 0, new BaseColor(211, 211, 211), tableDetailHeaderBold));


        long featurePassSteps = 0L;
        long featureFailSteps = 0L;
        long featureSkipSteps = 0L;
        long featurePendingSteps = 0L;
        long featureUndefineSteps = 0L;
        long featureTotalSteps = 0L;
        long scenarioPass = 0L;
        long scenarioFail = 0L;
        long scenarioTotal = 0L;

        //Create detail row
        int totalFeatures = reportResult.getAllFeatures().size();
        for (int i = 0; i < totalFeatures; i++) {

            Feature featureItem = reportResult.getAllFeatures().get(i);
            table.addCell(createHeaderCell(featureItem.getName(), 0, BaseColor.WHITE, tableCellText));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getPassedSteps()), 0, new BaseColor(149, 191, 91), tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getFailedSteps()), 0, new BaseColor(209, 89, 32), tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getSkippedSteps()), 0, BaseColor.WHITE, tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getPendingSteps()), 0, BaseColor.WHITE, tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getUndefinedSteps()), 0, BaseColor.WHITE, tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getSteps()), 0, new BaseColor(211, 211, 211), tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getPassedScenarios()), 0, new BaseColor(149, 191, 91), tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getFailedScenarios()), 0, new BaseColor(209, 89, 32), tableCellValue));
            table.addCell(createHeaderCell(String.valueOf(featureItem.getScenarios()), 0, new BaseColor(211, 211, 211), tableCellValue));

            featurePassSteps += featureItem.getPassedSteps();
            featureFailSteps += featureItem.getFailedSteps();
            featureSkipSteps += featureItem.getSkippedSteps();
            featurePendingSteps += featureItem.getPendingSteps();
            featureUndefineSteps += featureItem.getUndefinedSteps();
            featureTotalSteps += featureItem.getSteps();
            scenarioPass += featureItem.getPassedScenarios();
            scenarioFail += featureItem.getFailedScenarios();
            scenarioTotal += featureItem.getScenarios();
        }

        //Create total row
        table.addCell(createHeaderCell("", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(reportResult.getAllFeatures().size()), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featurePassSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featureFailSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featureSkipSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featurePendingSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featureUndefineSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(featureTotalSteps), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(scenarioPass), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(scenarioFail), 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.valueOf(scenarioTotal), 0, new BaseColor(211, 211, 211), tableCellValue));


        float fFeaturePassStep = (Float.parseFloat(String.valueOf(featurePassSteps)) / Float.parseFloat(String.valueOf(featureTotalSteps))) * 100;
        float fFeatureFailStep = (Float.parseFloat(String.valueOf(featureFailSteps)) / Float.parseFloat(String.valueOf(featureTotalSteps))) * 100;
        float fFeatureSkipStep = (Float.parseFloat(String.valueOf(featureSkipSteps)) / Float.parseFloat(String.valueOf(featureTotalSteps))) * 100;
        float fFeaturePendingStep = (Float.parseFloat(String.valueOf(featurePendingSteps)) / Float.parseFloat(String.valueOf(featureTotalSteps))) * 100;
        float fFeatureUndefineStep = (Float.parseFloat(String.valueOf(featureUndefineSteps)) / Float.parseFloat(String.valueOf(featureTotalSteps))) * 100;
        float fScenarioPass = (Float.parseFloat(String.valueOf(scenarioPass)) / Float.parseFloat(String.valueOf(scenarioTotal))) * 100;
        float fScenarioFail = (Float.parseFloat(String.valueOf(scenarioFail)) / Float.parseFloat(String.valueOf(scenarioTotal))) * 100;

        //Create percent row
        table.addCell(createHeaderCell("", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell("", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fFeaturePassStep) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fFeatureFailStep) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fFeatureSkipStep) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fFeaturePendingStep) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fFeatureUndefineStep) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell("", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fScenarioPass) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell(String.format("%.2f", fScenarioFail) + "%", 0, new BaseColor(211, 211, 211), tableCellValue));
        table.addCell(createHeaderCell("", 0, new BaseColor(211, 211, 211), tableCellValue));


        //Create step row

        preface.add(table);
    }

    private static void addTitlePageCucumberPDF(Document document, String timeStamp, ReportResult reportResult) throws DocumentException, IOException {
        PdfPTable tblheader = new PdfPTable(1);
        try {
            tblheader.setWidths(new int[]{24});
            tblheader.setTotalWidth(150);
            tblheader.setLockedWidth(true);
            tblheader.getDefaultCell().setFixedHeight(130);
            tblheader.getDefaultCell().setLeading(0, 1.1f);
            tblheader.getDefaultCell().setBorder(Rectangle.BOTTOM);
            tblheader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tblheader.addCell("Image");
            //tblheader.writeSelectedRows(0, -1, 10, tblheader.getTotalHeight() + 590, writer.getDirectContent());
        } catch (Exception e) {

        }

        Paragraph preface = new Paragraph();
        Image TymeBankheaderImg = Image.getInstance(System.getProperty("user.dir") + FileHelper.convertDirectory("/src/test/resources/data/TymeBank_Logo.jpg"));
        TymeBankheaderImg.scaleAbsolute(50f, 20f);
        TymeBankheaderImg.setAlignment(Element.ALIGN_TOP);

        preface.add(new Paragraph("                           Automation Test Report".toUpperCase(), catFont));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("   Report generated by: " + "PIKACHU Automation Team" + ", " + new Date(), smallBold));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("   This report demontrates the status of automation execution results in number, percentage of Passed," + "     Failed and Skipped Test Cases.", smallBold));
        addEmptyLine(preface, 1);
        document.add(preface);

        addEmptyLine(preface, 8);

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(75);
        table.setWidths(new int[]{1, 1});
        table.setSpacingAfter(10);

        table.addCell(createImageCellCucumberPDF(System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/images/FeaturesChart.png")));
        table.addCell(createImageCellCucumberPDF(System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/images/ScenariosChart.png")));
        document.add(table);

        Paragraph prefaceTwo = new Paragraph();
        prefaceTwo.setSpacingBefore(2);
        createDetailTable(prefaceTwo, reportResult);
        addEmptyLine(prefaceTwo, 2);
        document.add(prefaceTwo);

        document.newPage();
    }


}
