import javafx.application.Application;              //javaFX charts
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class histogram extends Application {
    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";

    @Override public void start(Stage stage) {
        //stage.setTitle("Bar Chart");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("n-grams");
        xAxis.setLabel("n-grams");
        yAxis.setLabel("occurrences");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("occurrences");
        series1.getData().add(new XYChart.Data(austria, 2));
        series1.getData().add(new XYChart.Data(brazil, 4));
        series1.getData().add(new XYChart.Data(france, 3));
        series1.getData().add(new XYChart.Data(italy, 12));
        series1.getData().add(new XYChart.Data(usa, 5));

        Scene scene  = new Scene(bc,500,700);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}