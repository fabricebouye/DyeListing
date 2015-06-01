package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Graphes sur les résultats des tests.
 * @author Fabrice Bouyé
 */
public class DisplayResultChart extends Application {

    private final String[] categoryNames = {"Temps total (ms)", "Requête Id (ms)", "Moyen requête teinture (ms)", "Max requête teinture (ms)", "Nombre de requêtes"};
    private final String[] strategyNames = {"Strategie #1", "Strategie #2", "Stratégie #3 (lot de 10)", "Stratégie #3 (lot de 20)", "Stratégie #3 (lot de 50)", "Stratégie #3 (lot de 100)"};

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Map<String, Number> data = new HashMap();
        data.put("0_0", 161617);
        data.put("0_1", 2263);
        data.put("0_2", 336);
        data.put("0_3", 3515);
        data.put("0_4", 474);
        data.put("1_0", 3740);
        data.put("1_1", 0);
        data.put("1_2", 3423);
        data.put("1_3", 3423);
        data.put("1_4", 1);
        data.put("2_0", 20032);
        data.put("2_1", 4348);
        data.put("2_2", 33);
        data.put("2_3", 693);
        data.put("2_4", 48);
        data.put("3_0", 11265);
        data.put("3_1", 2008);
        data.put("3_2", 20);
        data.put("3_3", 648);
        data.put("3_4", 24);
        data.put("4_0", 6276);
        data.put("4_1", 1985);
        data.put("4_2", 9);
        data.put("4_3", 794);
        data.put("4_4", 10);
        data.put("5_0", 4497);
        data.put("5_1", 1924);
        data.put("5_2", 5);
        data.put("5_3", 877);
        data.put("5_4", 5);
        final VBox chartVBox = new VBox();
        chartVBox.setFillWidth(true);
        for (int chartIndex = 0; chartIndex < categoryNames.length; chartIndex++) {
            final String category = categoryNames[chartIndex];
            final List<BarChart.Series> seriesList = new ArrayList(strategyNames.length);
            double maxY = 0;
            for (int strategyIndex = 0; strategyIndex < strategyNames.length; strategyIndex++) {
                final String seriesName = strategyNames[strategyIndex];
                final BarChart.Series series = new BarChart.Series();
                series.setName(seriesName);
                seriesList.add(series);
                final String key = String.format("%d_%d", strategyIndex, chartIndex);
                final Number value = data.get(key);
                series.getData().add(new BarChart.Data(category, value));
                maxY = Math.max(maxY, value.doubleValue());
            }
            final CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.getCategories().add(category);
            categoryAxis.setVisible(false);
            double base = Math.pow(10, (int) Math.log10(maxY));
            double ceiling = Math.ceil(maxY / base) * base;
            final NumberAxis yAxis = new NumberAxis(0, ceiling, ceiling / 10);
            final BarChart barChart = new BarChart(categoryAxis, yAxis);
            barChart.setTitle(category);
            barChart.setLegendSide(Side.RIGHT);
            barChart.getData().setAll(seriesList);
            barChart.setPrefHeight(250);
            chartVBox.getChildren().add(barChart);
        }
        final ScrollPane root = new ScrollPane(chartVBox);
        root.setFitToWidth(true);
        final Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... ags) {
        Application.launch(ags);
    }

}
