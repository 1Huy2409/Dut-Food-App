package Controller.Admin.Dashboard;

import DAO.Dao_Food;
import Model.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private HBox bestSellerBox;
    @FXML
    private PieChart pieChart;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        iniLineChart();
        iniPieChart();
        renderBestSeller();
    }
    private void iniLineChart()
    {
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("Monday",8));
        series.getData().add(new XYChart.Data("Tuesday",12));
        series.getData().add(new XYChart.Data("Wednesday",10));
        series.getData().add(new XYChart.Data("Thursday",15));
        series.getData().add(new XYChart.Data("Friday",12));
        series.getData().add(new XYChart.Data("Saturday",8));
        series.getData().add(new XYChart.Data("Sunday",5));
        lineChart.getData().add(series);
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        series.getNode().setStyle("-fx-stroke: #FFD6DC");
    }

    private void iniPieChart()
    {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Mon chinh", 30),
                new PieChart.Data("Mon trang mien", 15),
                new PieChart.Data("Nuoc ngot", 20),
                new PieChart.Data("Tra sua", 25)
        );
        pieChart.setData(pieChartData);
    }
    public void renderBestSeller()
    {
        List<FoodItem> foodItems = Dao_Food.getInstance().selectByCondition("bestseller");
        for (FoodItem item: foodItems)
        {
            VBox itemBox = new VBox();
            itemBox.setPrefWidth(399);
            itemBox.setPrefHeight(210);
            itemBox.setAlignment(Pos.CENTER);
            ImageView imageView = new ImageView();
            String imageUrl = item.getImageUrl();
            System.out.println("Loading image: " + imageUrl);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Lấy đường dẫn từ resources
                URL imageUrlPath = getClass().getResource("/" + imageUrl);
                if (imageUrlPath != null) {
                    Image image = new Image(imageUrlPath.toExternalForm());
                    imageView.setImage(image);
                } else {
                    System.err.println("This image file does not exist in resources: " + imageUrl);
                }
            } else {
                System.err.println("Error file path for: " + item.getFoodName());
            }
            imageView.setFitHeight(140);
            imageView.setFitWidth(250);
            Label nameLabel = new Label(item.getFoodName());
            itemBox.getChildren().addAll(imageView, nameLabel);
            bestSellerBox.getChildren().add(itemBox);
            System.out.println(item.getFoodName());
        }
    }
}
