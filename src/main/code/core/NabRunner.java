package core;

import core.util.platform.environment.TestEnvironment;
import core.util.platform.host.file.FileHelper;
import core.util.reporting.ChartGeneration;
import core.util.reporting.GeneratePdf;
import core.util.reporting.GenerateReport;
import core.util.scripting.io.StringHelper;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.Reportable;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static core.RunnerConfiguration.Glue;
import static core.RunnerConfiguration.Plugin;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {Plugin.JSON, Plugin.PRETTY, Plugin.PLAIN_HTML},
        glue = {Glue.CORE, Glue.STEPS}
)
public class NabRunner {
    protected static final Logger LOGGER = LoggerFactory.getLogger(NabRunner.class);
    public static File sHtmlReports;
    public static File sPdfReports;
    public static File sImageReports;
    public static GeneratePdf pdf;

    @BeforeClass
    public static void repairReport() {
        LOGGER.info("***** Pikachu Report is staring the execution *****");
        try {
            initReportsFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void generateReport() {
        String environment = TestEnvironment.getRunningEnvironment().toString();
        String timeStamp = String.valueOf(StringHelper.getTimeStampValue("yyyy_MM_dd_HH_mm_ss_SSS"));
        Reportable result = GenerateReport.generateMasterthoughtReport();
        String dataReportDrir = System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/images/");
        File sDateReports = new File(dataReportDrir);
        try {
            if (!sDateReports.exists()) {
                FileUtils.forceMkdir(sDateReports);
            }
            sPdfReports = new File(sPdfReports + FileHelper.convertDirectory("/PDF Reports on Environment " + environment + ".pdf"));
            // Create Bar char

            ChartGeneration.getFeaturesChart(result.getPassedFeatures(), result.getFailedFeatures(), 0);
            // Create Pie char
            ChartGeneration.getScenariosChart(result.getPassedScenarios(), result.getFailedScenarios(), 0);
            // Create report result
            ReportResult reportResult = GenerateReport.createReportDetail();
            // Generate PDF File
            pdf.toExecute(sPdfReports, timeStamp, reportResult);

        } catch (Exception ex) {
            LOGGER.warn("There are some problem during exporting pdf report !!!!! " + ex.getMessage());
        }
    }

    private static void initReportsFolder() throws IOException {
        try {
            LOGGER.info("***** Start initialize Report folders *****");
            sHtmlReports = new File(System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/html"));
            sPdfReports = new File(System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/pdf"));
            sImageReports = new File(System.getProperty("user.dir") + FileHelper.convertDirectory("/reports/images"));
            if (!sHtmlReports.exists()) {
                FileUtils.forceMkdir(sHtmlReports);
            }
            else{
                FileUtils.deleteDirectory(sHtmlReports);
            }
            if (!sPdfReports.exists()) {
                FileUtils.forceMkdir(sPdfReports);
            }else{
                FileUtils.deleteDirectory(sPdfReports);
            }
            if (sImageReports.exists()){
                FileUtils.deleteDirectory(sImageReports);
            }
        } catch (Exception e) {
            LOGGER.info("***** Unable to create reportLB folders: " + e.getMessage());
        }
    }
}
