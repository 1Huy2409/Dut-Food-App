package Controller.Admin.Dashboard;

import DAO.Dao_Food;
import DAO.Dao_Orders;
import Model.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private HBox bestSellerBox;
    @FXML
    private PieChart pieChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        iniLineChart();
        iniPieChart();
        renderBestSeller();
    }
    private void iniLineChart()
    {
        Map <String, Double> revenueData = Dao_Orders.getInstance().getRevenueByWeek();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
        revenueData.forEach((day, total) -> {
            series.getData().add(new XYChart.Data<>(day, total));
        });
        lineChart.getData().add(series);
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        series.getNode().setStyle("-fx-stroke: #FFD6DC");
    }

    private void iniPieChart() {
        Map<String, Integer> categoryRevenue = Dao_Food.getInstance().getCategoryRevenue();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : categoryRevenue.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieChart.setData(pieChartData);
    }
    public void renderBestSeller()
    {

        List<FoodItem> foodItems = Dao_Food.getInstance().selectByCondition("bestseller");
        for (FoodItem item: foodItems)
        {
            VBox itemBox = new VBox();
            itemBox.setPrefWidth(300);
            itemBox.setPrefHeight(300);
            itemBox.setAlignment(Pos.TOP_CENTER);
            itemBox.setPadding(new Insets(10));
            ImageView img = new ImageView();
            String imageUrl = item.getImageUrl();
            System.out.println(imageUrl);
            File file = new File("src/main/resources/" + imageUrl);
            Image image = new Image(file.toURI().toString());
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(150);
            Rectangle clip = new Rectangle(img.getFitWidth(), img.getFitHeight());
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            img.setClip(clip);
            Label nameLabel = new Label(item.getFoodName());
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setTextAlignment(TextAlignment.CENTER);
            itemBox.getChildren().addAll(img, nameLabel);
            bestSellerBox.getChildren().add(itemBox);
        }
        bestSellerBox.setAlignment(Pos.CENTER);
        bestSellerBox.setSpacing(150);
        //bestSellerBox.setPadding(new Insets(20, 0, 20, 0));

    }
}
