package core.util.reporting;

import core.util.platform.host.file.FileHelper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChartGeneration {
    public static void getFeaturesChart(int iPassCount, int iFailCount, int iSkippedCount){
        getPieChart(iPassCount,  iFailCount, iSkippedCount, "Features");
    }

    public static void getScenariosChart(int iPassCount, int iFailCount, int iSkippedCount){
        getPieChart(iPassCount,  iFailCount, iSkippedCount, "Scenarios");
    }
    private static void getPieChart(int iPassCount, int iFailCount, int iSkippedCount, String nameChart) {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("FAIL", new Integer(iFailCount));
        //pieDataset.setValue("SKIP", new Integer(iSkippedCount));
        pieDataset.setValue("PASS", new Integer(iPassCount));

        JFreeChart piechart = ChartFactory.createPieChart(nameChart, pieDataset, true, true, false);
        PiePlot plot = (PiePlot) piechart.getPlot();

        plot.setSectionPaint("FAIL", Color.RED);
        // plot.setSectionPaint("SKIP", Color.ORANGE);
        plot.setSectionPaint("PASS", new Color(192 * 85 + 192 * 104 + 192 * 47));
        plot.setBackgroundPaint(new Color(192 * 65536 + 192 * 256 + 192));
        plot.setExplodePercent("FAIL", 0.05);
        plot.setSimpleLabels(true);
        plot.setSectionOutlinesVisible(true);

        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        try {
            String imageDr = System.getProperty("user.dir")
                    + FileHelper.convertDirectory("/reports/images/" + nameChart + "Chart.png");
            ChartUtilities
                    .saveChartAsJPEG(new File(imageDr), piechart, 400, 400);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void getBarChart(int iPassCount, int iFailCount, int iSkippedCount) {
        final String series1 = "First";
        final String series2 = "Second";
        final String series3 = "Third";
        final String category1 = "Status";

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.addValue(iPassCount, series1, "Status");
        dataSet.addValue(iFailCount, series2, "Status");
        dataSet.addValue(iSkippedCount, series3, "Status");

        JFreeChart chart =
                ChartFactory.createBarChart("Bar Graph", "Execution Status", "Testcases", dataSet, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot barplot = chart.getCategoryPlot();
        // barplot.setBackgroundPaint(paint);
        barplot.setBackgroundPaint(Color.white);
        barplot.setBackgroundPaint(new Color(192 * 65536 + 192 * 256 + 192));

        barplot.setDomainGridlinePaint(Color.white);

        NumberAxis rangeAxis = (NumberAxis) barplot.getRangeAxis();
        rangeAxis.setRange(0.0, 70.0);
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        final BarRenderer renderer = (BarRenderer) barplot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.20);

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, new Color(192 * 85 + 192 * 104 + 192 * 47), 0.0f, 0.0f, Color.lightGray);
        final GradientPaint gp1 = new GradientPaint(

                0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.lightGray);

        final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.orange, 0.0f, 0.0f, Color.lightGray);
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        try {
            String imageDir = System.getProperty("user.dir")
                    + FileHelper.convertDirectory("/reports/images/BarChart.png");
            ChartUtilities.saveChartAsJPEG(new File(imageDir), chart, 400, 400);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
