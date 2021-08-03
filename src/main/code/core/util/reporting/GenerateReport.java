package core.util.reporting;

import core.util.platform.host.file.FileHelper;
import core.util.platform.host.os.OsHelper;
import net.masterthought.cucumber.*;
import net.masterthought.cucumber.json.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerateReport {
    protected static final Logger LOGGER = LoggerFactory.getLogger(GenerateReport.class);

    private static Configuration createConfiguration(File reportOutputDirectory, String projectName) {
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        String buildNumber = "1";
        boolean runWithJenkins = false;
        boolean parallelTesting = false;

        // optional configuration
        configuration.setParallelTesting(parallelTesting);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);

        // addidtional metadata presented on main page
        configuration.addClassifications("Platform", OsHelper.getOsFullName());
        configuration.addClassifications("Branch", "release/1.0");
        configuration.addClassifications("Created by", "NAB Framework");

        return configuration;
    }

    public static Reportable generateMasterthoughtReport() {
        Reportable result = null;
        try {
            LOGGER.info("START");
            File reportOutputDirectory = new File("reports/html");
            List<String> jsonFiles = new ArrayList<>();
            jsonFiles.add("./target/test-reports/cucumber/cucumber.json");
            String projectName = "NAB Automation Testing";
            Configuration configuration = createConfiguration(reportOutputDirectory, projectName);
            ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
            result = reportBuilder.generateReports();
            LOGGER.info("DONE REPORT");
            // and here validate 'result' to decide what to do
            // if report has failed features, undefined steps etc
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ReportResult createReportDetail() {
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("./target/test-reports/cucumber/cucumber.json");
        File reportOutputDirectory = new File(FileHelper.convertDirectory("reports/html"));
        String projectName = "NAB Automation Testing";
        Configuration configuration = createConfiguration(reportOutputDirectory, projectName);
        ReportParser reportParser = new ReportParser(configuration);
        List<Feature> features = reportParser.parseJsonFiles(jsonFiles);
        ReportResult reportResult = new ReportResult(features, configuration.getSortingMethod());
        return reportResult;
    }
}
