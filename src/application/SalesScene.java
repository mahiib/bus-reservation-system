package application;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SalesScene {
	static int displayMonth = LocalDate.now().getMonthValue();
    static int displayYear = LocalDate.now().getYear();
	static TableView<Sales> salesTable;
	public static Scene salesScene(Button viewInventoryButton,Stage mainStage) {  
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Sales Record:" + displayYear);
                          
        XYChart.Series series1 = new XYChart.Series();
//        series1.setName("Portfolio 1");
        
        try {
        	Connection con = DBConnector.getConnection();
        	PreparedStatement stmt = con.prepareStatement("select sum(sell_price) as sell_price,month(timestamp) as month from sales where year(timestamp) = year(now()) group by month(timestamp) order by month(timestamp) asc");
        	ResultSet rs = stmt.executeQuery();
        	
        	while(rs.next()) {
        		series1.getData().add(new XYChart.Data(rs.getString("month"), rs.getFloat("sell_price")));
        	}
        }catch(SQLException e) {
        	e.printStackTrace();
        }
        
        lineChart.getData().addAll(series1);
		
		Button salesTableButton = new Button("View Table");
		
		salesTableButton.setOnAction(e -> showSalesTable(mainStage));
		
		VBox topPanelLayout = new VBox();
		HBox searchBarLayout = new HBox();
		topPanelLayout.setAlignment(Pos.CENTER);
		topPanelLayout.setSpacing(20);
		searchBarLayout.setAlignment(Pos.CENTER);
		Button searchButton = new Button("Get By Year");
		TextField searchField = new TextField();
		searchBarLayout.getChildren().addAll(searchField,searchButton);
		
		searchButton.setOnAction(sr -> {
			series1.getData().clear();
			
			if(!searchField.getText().equals(""))
				displayYear = Integer.parseInt(searchField.getText());
			else
				displayYear = LocalDate.now().getYear();
			
	        lineChart.setTitle("Sales Record:" + displayYear);
						
	        try {
	        	Connection con = DBConnector.getConnection();
	        	PreparedStatement stmt = con.prepareStatement("select sum(sell_price) as sell_price,month(timestamp) as month from sales where year(timestamp) = '"+displayYear+"' group by month(timestamp) order by month(timestamp) asc");
	        	ResultSet rs = stmt.executeQuery();
	        	
	        	while(rs.next()) {
	        		series1.getData().add(new XYChart.Data(rs.getString("month"), rs.getFloat("sell_price")));
	        	}
	        }catch(SQLException e) {
	        	e.printStackTrace();
	        }
			
		});
		
		topPanelLayout.getChildren().addAll(viewInventoryButton,searchBarLayout);
		topPanelLayout.setMargin(viewInventoryButton, new Insets(20,0,0,0));
		
        BorderPane root = new BorderPane();
        BorderPane.setAlignment(topPanelLayout, Pos.CENTER);
        BorderPane.setAlignment(salesTableButton, Pos.CENTER);
        root.setTop(topPanelLayout);
        root.setBottom(salesTableButton);
        root.setMargin(salesTableButton, new Insets(0,0,20,0));
        root.setCenter(lineChart);
        Scene salesScene = new Scene(root,600,500);  
        
        return salesScene;
	}

	public static void showSalesTable(Stage mainStage) {
		salesTable = new TableView<>();

		TableColumn<Sales,String> prod_IdColumn = new TableColumn<>("Prod_id");
		prod_IdColumn.setMinWidth(100);
		prod_IdColumn.setCellValueFactory(new PropertyValueFactory<Sales,String>("prod_id"));
		
		TableColumn<Sales,String> prod_NameColumn = new TableColumn<>("Prod_name");
		prod_NameColumn.setMinWidth(100);
		prod_NameColumn.setCellValueFactory(new PropertyValueFactory<Sales,String>("prod_name"));
		
		TableColumn<Sales,String> qty_SoldColumn = new TableColumn<>("Qty_sold");
		qty_SoldColumn.setMinWidth(100);
		qty_SoldColumn.setCellValueFactory(new PropertyValueFactory<Sales,String>("qty_sold"));
		
		TableColumn<Sales,String> sell_PriceColumn = new TableColumn<>("Sale_price");
		sell_PriceColumn.setMinWidth(100);
		sell_PriceColumn.setCellValueFactory(new PropertyValueFactory<Sales,String>("sell_price"));
		
		salesTable.getColumns().addAll(prod_IdColumn,prod_NameColumn,qty_SoldColumn,sell_PriceColumn);
		salesTable.setItems(getMonthlySales(displayMonth));
		
		VBox salesTableLayout = new VBox();
		HBox searchBarLayout = new HBox();
		TextField searchField = new TextField();
		Button searchButton = new Button("Get By Month");

		Label monthLabel = new Label(theMonth(displayMonth));
		monthLabel.setFont(Font.font("Arial",20));
		
		searchBarLayout.setAlignment(Pos.CENTER);
		searchBarLayout.setSpacing(20);
		searchBarLayout.getChildren().addAll(searchField,searchButton,monthLabel);
		
		searchBarLayout.setMargin(searchField, new Insets(20,0,20,0));
		searchBarLayout.setMargin(searchButton, new Insets(20,0,20,0));
		searchBarLayout.setMargin(monthLabel, new Insets(20,0,20,0));

		
		searchButton.setOnAction(sr -> {
			salesTable.getItems().clear();

			if(!searchField.getText().equals(""))
				displayMonth = Integer.parseInt(searchField.getText());
			else
				displayMonth = LocalDate.now().getMonthValue();
			
			monthLabel.setText(theMonth(displayMonth));
			
			salesTable.setItems(getMonthlySales(displayMonth));
		});
		
		salesTableLayout.getChildren().addAll(searchBarLayout,salesTable);
		salesTableLayout.setMargin(salesTable, new Insets(0,10,0,10));
		
		Scene salesTableScene = new Scene(salesTableLayout,400,500);
		Stage salesTableStage = new Stage();
		salesTableStage.setTitle("Sales Table");
		salesTableStage.setScene(salesTableScene);
		mainStage.setX(200);
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\Vaibhav Jamwal\\Downloads\\inventoryLogo.png");
	        Image imageLogo = new Image(inputLogo);
	        salesTableStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		salesTableStage.setX(mainStage.getX() + 600);
		salesTableStage.setY(mainStage.getY());
		salesTableStage.show();
		
		salesTableStage.setOnCloseRequest(cl -> {
			mainStage.setX(400);
		});
		
	}
	
	public static String theMonth(int month){
	    String[] monthNames = {"","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[month];
	}
	
	public static ObservableList<Sales> getMonthlySales(int month){
		ObservableList<Sales> sales = FXCollections.observableArrayList();
		try {
			Connection con = DBConnector.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
//			if(month == 0)
//				rs = stmt.executeQuery("select prod_id,prod_name,sum(qty_sold) as qty_sold,sum(sell_price) as sell_price from sales where month(timestamp) = month(now()) and year(timestamp) = '"+displayYear+"' group by prod_id order by month(timestamp) asc; ");
//			else
				rs = stmt.executeQuery("select prod_id,prod_name,sum(qty_sold) as qty_sold,sum(sell_price) as sell_price from sales where month(timestamp) = '"+Integer.toString(month)+"' and year(timestamp) = '"+displayYear+"' group by prod_id order by month(timestamp) asc; ");

			while(rs.next())
			{
				Sales newSales = new Sales();
				newSales.setProd_name(rs.getString("prod_name"));
				newSales.setProd_id(rs.getInt("prod_id"));
				newSales.setQty_sold(rs.getInt("qty_sold"));
				newSales.setSell_price(rs.getFloat("sell_price"));
				sales.add(newSales);
			}
			rs.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return sales;
	}
}