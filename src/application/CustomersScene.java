package application;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomersScene {
	static TableView<Customer> customerTable;
	static String phone = "";
	
	public static Scene customerScene(Button viewInventoryButton,Stage mainStage) {	        
		customerTable = new TableView<>();
		
		TableColumn<Customer,String> idColumn = new TableColumn<>("Id");
		idColumn.setMinWidth(100);
		idColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("id"));

		TableColumn<Customer,String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(100);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
		
		TableColumn<Customer,String> phoneColumn = new TableColumn<>("Phone");
		phoneColumn.setMinWidth(100);
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
		
		TableColumn<Customer,String> addressColumn = new TableColumn<>("Address");
		addressColumn.setMinWidth(100);
		addressColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
		
		
		TableColumn<Customer,String> owedColumn = new TableColumn<>("Amount Owed");
		owedColumn.setMinWidth(100);
		owedColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("owed"));
		
		customerTable.getColumns().addAll(idColumn,nameColumn,phoneColumn,addressColumn,owedColumn);
		customerTable.setItems(getCustomer());
		
		Button addCustomerButton = new Button("Add Customer");
		Button updateCustomerButton = new Button("Update Customer");
		Button deductButton = new Button("Deduct");
		Button sumButton = new Button("Sum Money");
		
		addCustomerButton.setOnAction(e -> addCustomer(mainStage));
		updateCustomerButton.setOnAction(e -> updateCustomer(customerTable.getSelectionModel().getSelectedItem(),mainStage ));
		deductButton.setOnAction(e -> deductCustomer(customerTable.getSelectionModel().getSelectedItem(),mainStage ));
		sumButton.setOnAction(e -> sumCustomer(customerTable.getSelectionModel().getSelectedItem(),mainStage));
		
		HBox buttonHolder = new HBox();
		buttonHolder.setSpacing(20);
		buttonHolder.setAlignment(Pos.CENTER);
		buttonHolder.getChildren().addAll(addCustomerButton,updateCustomerButton,deductButton,sumButton);
		buttonHolder.setMargin(addCustomerButton, new Insets(20,0,20,0));
		buttonHolder.setMargin(updateCustomerButton, new Insets(20,0,20,0));
		buttonHolder.setMargin(deductButton, new Insets(20,0,20,0));
		buttonHolder.setMargin(sumButton, new Insets(20,0,20,0));

		
		HBox searchBarLayout = new HBox();
		TextField searchField = new TextField();
		Button searchButton = new Button("Get By Phone");
		searchBarLayout.setAlignment(Pos.CENTER);
		searchBarLayout.setSpacing(20);
		searchBarLayout.getChildren().addAll(searchField,searchButton);
		 
		searchButton.setOnAction(e -> {
			customerTable.getItems().clear();
			if(!searchField.getText().equals(""))
				phone = searchField.getText();
			else
				phone = "";
			
			customerTable.setItems(getCustomer());
		});
		
		VBox topElement = new VBox();
		topElement.setSpacing(20);
		topElement.setAlignment(Pos.CENTER);
		topElement.getChildren().addAll(viewInventoryButton,searchBarLayout);
		topElement.setMargin(viewInventoryButton, new Insets(20,0,0,0));
		topElement.setMargin(searchBarLayout, new Insets(0,0,20,0));

	        BorderPane root = new BorderPane();
	        root.setTop(topElement);
	        root.setCenter(customerTable);
	        root.setBottom(buttonHolder);
	        BorderPane.setAlignment(customerTable, Pos.CENTER);
	        BorderPane.setAlignment(viewInventoryButton, Pos.CENTER);
	        BorderPane.setAlignment(buttonHolder, Pos.CENTER);
	        
	        BorderPane.setMargin(customerTable, new Insets(0,10,0,10));
	        Scene customerScene = new Scene(root);
	        
	        return customerScene;
	}
	
	public static void addCustomer(Stage mainStage) {
		
		Label nameLabel = new Label("Customer Name");
		Label phoneLabel = new Label("Customer Phone no");
		Label addressLabel = new Label("Customer Address");
		Label owedLabel = new Label("Amount owed by customer");
		
		TextField nameField = new TextField();
		TextField phoneField = new TextField();
		TextField addressField = new TextField();
		TextField owedField = new TextField();
		
		Button submitAddCustomerForm = new Button("Add Customer");
		
		VBox addCustomerForm = new VBox();
		addCustomerForm.setSpacing(10);
		addCustomerForm.setAlignment(Pos.CENTER);
		
		addCustomerForm.getChildren().addAll(nameLabel,nameField,phoneLabel,phoneField,addressLabel,addressField,owedLabel,owedField,submitAddCustomerForm);
		VBox.setMargin(nameField,new Insets(0,30,0,30));
		VBox.setMargin(addressField,new Insets(0,30,0,30));
		VBox.setMargin(phoneField,new Insets(0,30,0,30));
		VBox.setMargin(owedField,new Insets(0,30,0,30));
		
		Scene addCustomerScene = new Scene(addCustomerForm,400,400);
		
		Stage addCustomerStage = new Stage();
		addCustomerStage.setTitle("Add a new Customer");
		mainStage.setX(200);
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\Vaibhav Jamwal\\Downloads\\inventoryLogo.png");
	        Image imageLogo = new Image(inputLogo);
	        addCustomerStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		addCustomerStage.setScene(addCustomerScene);
		addCustomerStage.setX(mainStage.getX()+525);
		addCustomerStage.setY(mainStage.getY());
		addCustomerStage.show();
		
		addCustomerStage.setOnCloseRequest(cl -> {
			mainStage.setX(400);
		});
		
		submitAddCustomerForm.setOnAction(er -> {
			
			Customer newCustomer = new Customer();
			newCustomer.setName(nameField.getText());
			newCustomer.setPhone(phoneField.getText());
			newCustomer.setAddress(addressField.getText());
			newCustomer.setOwed(Float.parseFloat(owedField.getText()));
			
			DBConnector.insertCustomer(newCustomer);
			
			nameField.clear();
			phoneField.clear();
			addressField.clear();
			owedField.clear();
			
			customerTable.setItems(getCustomer());
			
		});

	}
	
	public static void updateCustomer(Customer selectedCustomer,Stage mainStage) {
		Label nameLabel = new Label("Customer Name");
		Label phoneLabel = new Label("Customer Phone no");
		Label addressLabel = new Label("Customer Address");
		Label owedLabel = new Label("Amount owed by customer");
		
		TextField nameField = new TextField();
		TextField phoneField = new TextField();
		TextField addressField = new TextField();
		TextField owedField = new TextField();
		
		Button submitUpdateCustomerForm = new Button("Update Customer");
		
		VBox updateCustomerForm = new VBox();
		updateCustomerForm.setSpacing(10);
		updateCustomerForm.setAlignment(Pos.CENTER);
		
		updateCustomerForm.getChildren().addAll(nameLabel,nameField,phoneLabel,phoneField,addressLabel,addressField,owedLabel,owedField,submitUpdateCustomerForm);
		VBox.setMargin(nameField,new Insets(0,30,0,30));
		VBox.setMargin(addressField,new Insets(0,30,0,30));
		VBox.setMargin(phoneField,new Insets(0,30,0,30));
		VBox.setMargin(owedField,new Insets(0,30,0,30));
		
		Scene updateCustomerScene = new Scene(updateCustomerForm,400,400);
		
		Stage updateCustomerStage = new Stage();
		updateCustomerStage.setTitle("Update Customer");
		mainStage.setX(200);
		updateCustomerStage.setScene(updateCustomerScene);
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\Vaibhav Jamwal\\Downloads\\inventoryLogo.png");
	        Image imageLogo = new Image(inputLogo);
	        updateCustomerStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		updateCustomerStage.setX(mainStage.getX()+525);
		updateCustomerStage.setY(mainStage.getY());
		updateCustomerStage.show();
		updateCustomerStage.setOnCloseRequest(cl->{
			mainStage.setX(400);
		});
		
		submitUpdateCustomerForm.setOnAction(er -> {
			
//			Customer selectedCustomer = new Customer();
//			selectedCustomer.setId(customerTable.getSelectionModel().getSelectedItem().getId());
//			selectedCustomer.setName(customerTable.getSelectionModel().getSelectedItem().getName());
//			selectedCustomer.setPhone(customerTable.getSelectionModel().getSelectedItem().getPhone());
//			selectedCustomer.setAddress(customerTable.getSelectionModel().getSelectedItem().getAddress());
//			selectedCustomer.setOwed(customerTable.getSelectionModel().getSelectedItem().getOwed());
			
			if(!nameField.getText().equals(""))
				selectedCustomer.setName(nameField.getText());
			if(!phoneField.getText().equals(""))
				selectedCustomer.setPhone(phoneField.getText());
			if(!addressField.getText().equals(""))
				selectedCustomer.setAddress(addressField.getText());
			if(!owedField.getText().equals(""))
				selectedCustomer.setOwed(Float.parseFloat(owedField.getText()));
			
			DBConnector.updateCustomer(selectedCustomer);
			
			nameField.clear();
			phoneField.clear();
			addressField.clear();
			owedField.clear();
			
			customerTable.setItems(getCustomer());
			
		});
	}
	
	public static void deductCustomer(Customer selectedCustomer,Stage mainStage) {
		Label amountLabel = new Label("Amount to deduct");
		TextField amountField = new TextField();
		Button submitDeductForm = new Button("Deduct");
		
		VBox deductForm = new VBox();
		deductForm.setSpacing(20);
		deductForm.setAlignment(Pos.CENTER);
		
		deductForm.getChildren().addAll(amountLabel,amountField,submitDeductForm);
		VBox.setMargin(amountField, new Insets(0,40,0,40));
		Scene deductScene = new Scene(deductForm,200,200);
		
		Stage deductStage = new Stage();
		deductStage.setScene(deductScene);
		mainStage.setX(200);
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\Vaibhav Jamwal\\Downloads\\inventoryLogo.png");
	        Image imageLogo = new Image(inputLogo);
	        deductStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		deductStage.setTitle("Deduct Amount from customer");
		deductStage.setX(mainStage.getX()+525);
		deductStage.setY(mainStage.getY());
		deductStage.show();
		deductStage.setOnCloseRequest(cl->{
			mainStage.setX(400);
		});
		
		submitDeductForm.setOnAction(er -> {
			
//			Customer selectedCustomer = new Customer();
//			selectedCustomer.setId(customerTable.getSelectionModel().getSelectedItem().getId());
//			selectedCustomer.setName(customerTable.getSelectionModel().getSelectedItem().getName());
//			selectedCustomer.setPhone(customerTable.getSelectionModel().getSelectedItem().getPhone());
//			selectedCustomer.setAddress(customerTable.getSelectionModel().getSelectedItem().getAddress());
//			selectedCustomer.setOwed(customerTable.getSelectionModel().getSelectedItem().getOwed());

			float toDeduct = Float.parseFloat(amountField.getText());
			float bal = selectedCustomer.getOwed() - toDeduct;
			System.out.print(bal);
			try {
				Connection con = DBConnector.getConnection();
				PreparedStatement stmt = con.prepareStatement("update customers set owed = ? where id = ? ");
				stmt.setFloat(1, bal);
				stmt.setInt(2, selectedCustomer.getId());
				stmt.executeUpdate();
				con.close();
				amountField.clear();
				customerTable.setItems(getCustomer());
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		});
		
	}
	
	public static void sumCustomer(Customer selectedCustomer,Stage mainStage) {
		Label amountLabel = new Label("Amount to ADD");
		TextField amountField = new TextField();
		Button submitSumForm = new Button("Sum");
		
		VBox sumForm = new VBox();
		sumForm.setSpacing(20);
		sumForm.setAlignment(Pos.CENTER);
		
		sumForm.getChildren().addAll(amountLabel,amountField,submitSumForm);
		VBox.setMargin(amountField, new Insets(0,40,0,40));
		Scene deductScene = new Scene(sumForm,200,200);
		
		Stage sumStage = new Stage();
		sumStage.setScene(deductScene);
		mainStage.setX(200);
		
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\Vaibhav Jamwal\\Downloads\\inventoryLogo.png");
	        Image imageLogo = new Image(inputLogo);
	        sumStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		sumStage.setTitle("Add to customer");
		sumStage.setX(mainStage.getX()+525);
		sumStage.setY(mainStage.getY());
		sumStage.show();
		sumStage.setOnCloseRequest(cl -> {
			mainStage.setX(400);
		});
		
		submitSumForm.setOnAction(er -> {

			float toSum = Float.parseFloat(amountField.getText());
			float bal = selectedCustomer.getOwed() + toSum;
			try {
				Connection con = DBConnector.getConnection();
				PreparedStatement stmt = con.prepareStatement("update customers set owed = ? where id = ? ");
				stmt.setFloat(1, bal);
				stmt.setInt(2, selectedCustomer.getId());
				stmt.executeUpdate();
				con.close();
				amountField.clear();
				customerTable.setItems(getCustomer());
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
	
	public static ObservableList<Customer> getCustomer(){
		ObservableList<Customer> customers = FXCollections.observableArrayList();
		try {
			Connection con = DBConnector.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			final String liability = ".*";

				rs = stmt.executeQuery("select * from customers where phone regexp '"+liability+phone+"'");
			
			while(rs.next())
				customers.add(new Customer(rs.getInt("id"),rs.getString("name"),rs.getString("phone"),rs.getString("address"),rs.getFloat("owed")));
			
			rs.close();
		}catch(SQLException e) {
			System.out.println("getInventoryList: "+e);
			e.printStackTrace();
		}
		
		return customers;
	}
}