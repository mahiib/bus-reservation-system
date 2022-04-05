package application;

import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	
	TableView<InventoryItem> inventoryTable;
    Button viewInventoryButton = new Button("View Inventory");
	Button viewSalesButton = new Button("View Sales");
	Button viewCustomersButton = new Button("View Customers");
	Scene inventoryScene;
	Scene customerScene;
	Scene salesScene;
	Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		
			inventoryTable = new TableView<>();
			
			TableColumn<InventoryItem,String> idColumn = new TableColumn<>("Id");
			idColumn.setMinWidth(100);
			idColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem,String>("id"));
		
			TableColumn<InventoryItem,String> nameColumn = new TableColumn<>("Name");
			nameColumn.setMinWidth(200);
			nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem,String>("name"));
			
			TableColumn<InventoryItem,String> qtyColumn = new TableColumn<>("Qty");
			qtyColumn.setMinWidth(150);
			qtyColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem,String>("qty"));
			
			TableColumn<InventoryItem,String> priceColumn = new TableColumn<>("Price");
			priceColumn.setMinWidth(150);
			priceColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem,String>("price"));
			
			inventoryTable.setItems(getInventoryItem());
			inventoryTable.getColumns().addAll(idColumn,nameColumn,qtyColumn,priceColumn);
			
			VBox inventoryList = new VBox();
			inventoryList.setSpacing(5);
			inventoryList.setAlignment(Pos.CENTER);
			inventoryList.getChildren().add(inventoryTable);
			inventoryList.setMargin(inventoryTable, new Insets(0,10,0,10));
			
			Button add = new Button("ADD");
			Button sell = new Button("SELL");
			Button delete = new Button("DELETE");
			Button update = new Button("UPDATE");
			
			add.setOnAction(e -> displayAdd());
			sell.setOnAction(e -> displaySell(primaryStage));
			delete.setOnAction(e-> {
				DBConnector.delete(inventoryTable.getSelectionModel().getSelectedItem().getId());//Check if delete functioning
				inventoryTable.getItems().clear();
				inventoryTable.setItems(getInventoryItem());
			});
			update.setOnAction(e-> displayUpdate());
			
			HBox optionButtons = new HBox();
			optionButtons.setSpacing(10);
			optionButtons.setAlignment(Pos.CENTER);
			optionButtons.getChildren().addAll(add,sell,delete,update);
			
			HBox panelButtons = new HBox();
			panelButtons.setSpacing(40);
			panelButtons.setAlignment(Pos.CENTER);
			
			panelButtons.getChildren().addAll(viewSalesButton,viewCustomersButton);
			panelButtons.setMargin(viewSalesButton,new Insets(20,0,0,0));
			panelButtons.setMargin(viewCustomersButton,new Insets(20,0,0,0));

			viewInventoryButton.setOnAction(in -> {
	        	primaryStage.setScene(inventoryScene);
	        });
			
			viewSalesButton.setOnAction(e -> {
				//Add a Sales vs Timestamp line chart
		        primaryStage.setScene(SalesScene.salesScene(viewInventoryButton,mainStage) );  
		        primaryStage.setTitle("LineChart ");  
			});
			
			viewCustomersButton.setOnAction(e -> {
				//A view of customer details
		        primaryStage.setScene(CustomersScene.customerScene(viewInventoryButton,mainStage) );
		        primaryStage.setTitle("Customers");
			});
			
			BorderPane borderPane = new BorderPane();
			borderPane.setCenter(inventoryList);
						
			
			borderPane.setBottom(optionButtons);//Bottom Panel
			BorderPane.setMargin(optionButtons,new Insets(0,0,40,0));
			inventoryScene = new Scene(borderPane,600,600);
			
			borderPane.setTop(panelButtons);//Top Panel
			
			//Login Scene
			VBox loginScene = new VBox();
			loginScene.setAlignment(Pos.CENTER);
			loginScene.setSpacing(20);
			
			Label username = new Label("Enter your username");
			Label password = new Label("Enter your password");
			
			username.setAlignment(Pos.CENTER);
			password.setAlignment(Pos.CENTER);
			
			username.setMinWidth(180);
			username.setMinHeight(20);
		
			password.setMinWidth(180);
			password.setMinHeight(20);
			
            BackgroundFill background_fill = new BackgroundFill(Color.WHITE, 
                    CornerRadii.EMPTY, Insets.EMPTY);
			
            Background labelFill = new Background(background_fill);
            
            Font labelFont = new Font("Aria",15);
            
            username.setFont(labelFont);
            password.setFont(labelFont);
            
			username.setBackground(labelFill);
			password.setBackground(labelFill);
			
			TextField userField = new TextField();
			PasswordField passField = new PasswordField();
			
			Button submitLogin = new Button("Login");
			
			Label errorMsg = new Label();
			errorMsg.setFont(labelFont);
			errorMsg.setBackground(labelFill);
			errorMsg.setTextFill(Color.RED);
			
			loginScene.getChildren().addAll(username,userField,password,passField,submitLogin,errorMsg);
			VBox.setMargin(userField, new Insets(0,50,0,50));
			VBox.setMargin(passField,new Insets(0,50,0,50));
			
			FileInputStream input = new FileInputStream("C:\\Users\\DESIGN 1\\Downloads\\download.png");
			  
            // create a image
            Image image = new Image(input);
  
            // create a background image
            BackgroundImage backgroundimage = new BackgroundImage(image, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundPosition.DEFAULT, 
                                              new BackgroundSize(300,300, false, false, false, false));
  
            // create Background
            Background background = new Background(backgroundimage);
            
            loginScene.setBackground(background);
			
			Scene login = new Scene(loginScene,300,300);
			
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\DESIGN 1\\Downloads\\download.png");
			  
            // create a image
            Image imageLogo = new Image(inputLogo);
			
            primaryStage.getIcons().add(imageLogo);
			primaryStage.setTitle("Login");
			primaryStage.setScene(login);
			primaryStage.show();
			//Login Page
			submitLogin.setOnAction(e -> {
				String user = userField.getText();
				String pass = DBConnector.login(user);
				if(user.equals(userField.getText()) && pass.equals(passField.getText()) && !user.equals("") && !pass.equals(""))
				{
					primaryStage.setTitle("Inventory Management System");
					primaryStage.setScene(inventoryScene);
					primaryStage.setX(primaryStage.getX() - 100);
					primaryStage.setY(primaryStage.getY() - 100);
					primaryStage.show();
				}
				else {
					errorMsg.setText("Invalid Credentials");
					userField.clear();
					passField.clear();
				}
			});
			
	}
	
	public void displaySell(Stage primaryStage) {
		ObservableList<String> names = FXCollections.observableArrayList("Id			Name			Qty			Price");
		ObservableList<Cart> cart = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(names);
		
		Label qtyLabel = new Label("Qty Req");
		TextField qtyField = new TextField();
		Label mssg = new Label("");
		mssg.setFont(Font.font("Arial",15));
		
		Button addButton = new Button("Add Item");
		addButton.setOnAction(e -> {
			if(qtyField.getText().equals("")) {
				mssg.setText("Must enter a quantity");
				mssg.setTextFill(Color.RED);
			}
			else {
				mssg.setText("");

				Cart item = new Cart();
				item.setId(inventoryTable.getSelectionModel().getSelectedItem().getId());
				item.setName(inventoryTable.getSelectionModel().getSelectedItem().getName());
				item.setQty(Integer.parseInt(qtyField.getText()));
				item.setPrice(inventoryTable.getSelectionModel().getSelectedItem().getPrice());
				Float totalPrice = item.getQty()*item.getPrice();
				item.setPrice(totalPrice);
				
				cart.add(item);
				
				names.add(String.format("%d			%s			%d			%f", item.getId(),item.getName(),item.getQty(),totalPrice));
				qtyField.clear();
			}
		});
		
		Button sellButton = new Button("Sell All Selected");
		sellButton.setOnAction(er -> {
			//Function to sell
			float totalPrice = DBConnector.sell(cart);
			mssg.setTextFill(Color.BLACK);
			mssg.setText("Amount Owed: "+ Float.toString(totalPrice));
		});
		
		HBox downPanel = new HBox();
		downPanel.setSpacing(10);
		downPanel.setAlignment(Pos.CENTER);
		downPanel.getChildren().addAll(qtyLabel,qtyField,addButton,sellButton);
		
		BorderPane root = new BorderPane();
		root.setBottom(downPanel);
		root.setCenter(listView);
		root.setTop(mssg);
		Scene rootScene = new Scene(root,350,400);
		
		Stage rootStage = new Stage();
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\DESIGN 1\\Downloads\\download.png");
	        Image imageLogo = new Image(inputLogo);
	        rootStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		rootStage.setTitle("Sell items");
		rootStage.setScene(rootScene);
		primaryStage.setX(100);
		rootStage.setX(primaryStage.getX()+600);
		rootStage.setY(primaryStage.getHeight()/4);
		rootStage.show();
		
		rootStage.setOnCloseRequest(cl -> {
			primaryStage.setX(400);
		});
		
		
	}
	
public void displayAdd() {
		
		Stage addStage = new Stage();
		addStage.setTitle("Add an Item");
		
		VBox addForm = new VBox();
		addForm.setSpacing(10);
		addForm.setAlignment(Pos.CENTER);
		
		Label item_name = new Label("Item Name");
		Label item_qty = new Label("Item Quantity");
		Label item_price = new Label("Item Price");
		
		TextField nameField = new TextField();
		TextField qtyField = new TextField();
		TextField priceField = new TextField();
		
		Button submitAdd = new Button("ADD");
		
		submitAdd.setOnAction(ad -> {
			//Connect to JDBC and Insert the new data
			InventoryItem newItem = new InventoryItem();
			newItem.setName(nameField.getText());
			newItem.setQty(Integer.parseInt(qtyField.getText()));
			newItem.setPrice(Float.parseFloat(priceField.getText()));
//			InventoryItem insertedItem = DBConnector.insert(newItem);
//			inventoryTable.getItems().add(insertedItem);
			DBConnector.insert(newItem);//insert called from DBConnector
			inventoryTable.getItems().clear();
			inventoryTable.setItems(getInventoryItem());
			nameField.clear();
			qtyField.clear();
			priceField.clear();
			addStage.close();
		});
		
		addForm.getChildren().addAll(item_name,nameField,item_qty,qtyField,item_price,priceField,submitAdd);
		
		VBox.setMargin(nameField,new Insets(0,50,0,50));
		VBox.setMargin(qtyField,new Insets(0,50,0,50));
		VBox.setMargin(priceField,new Insets(0,50,0,50));
		
		Scene addScene = new Scene(addForm,400,400);
		addStage.setScene(addScene);
		mainStage.setX(200);
		addStage.setX(mainStage.getX()+600);
		
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\DESIGN 1\\Downloads\\download.png");
	        Image imageLogo = new Image(inputLogo);
	        addStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
        
		addStage.show();
		
		addStage.setOnCloseRequest(cl -> {
			mainStage.setX(400);
		});
		
	}
	
	
	public void displayUpdate() {
		Stage updateStage = new Stage();
		updateStage.setTitle("Update an Item");
		
		VBox updateForm = new VBox();
		updateForm.setSpacing(10);
		updateForm.setAlignment(Pos.CENTER);
		
		Label item_id = new Label("Item id");
		Label item_name = new Label("Item Name");
		Label item_qty = new Label("Item Quantity");
		Label item_price = new Label("Item Price");
		
		TextField idField = new TextField();
		TextField nameField = new TextField();
		TextField qtyField = new TextField();
		TextField priceField = new TextField();
		
		Button submitUpdate = new Button("UPDATE");
		
		submitUpdate.setOnAction(up -> {
			//Send the update request from here
			InventoryItem newItem = new InventoryItem();
			
			newItem.setId(Integer.parseInt(idField.getText()));
			newItem.setName(nameField.getText());
			if(!qtyField.getText().equals(""))
				newItem.setQty(Integer.parseInt(qtyField.getText()));
			if(!priceField.getText().equals(""))
				newItem.setPrice(Float.parseFloat(priceField.getText()));
			DBConnector.update(newItem);									//update function called from DBConnector
			inventoryTable.getItems().clear();
			inventoryTable.setItems(getInventoryItem());
			updateStage.close();
		});
		
		updateForm.getChildren().addAll(item_id,idField,item_name,nameField,item_qty,qtyField,item_price,priceField,submitUpdate);
		
		VBox.setMargin(idField,new Insets(0,50,0,50));
		VBox.setMargin(nameField,new Insets(0,50,0,50));
		VBox.setMargin(qtyField,new Insets(0,50,0,50));
		VBox.setMargin(priceField,new Insets(0,50,0,50));
		
		
		Scene updateScene = new Scene(updateForm,400,400);
		mainStage.setX(200);
		try {
			FileInputStream inputLogo = new FileInputStream("C:\\Users\\DESIGN 1\\Downloads\\download.png");
	        Image imageLogo = new Image(inputLogo);
	        updateStage.getIcons().add(imageLogo);
		}catch(Exception e) {
			System.out.println(e);
		}
		updateStage.setScene(updateScene);
		updateStage.setX(mainStage.getX()+600);
		updateStage.setY(mainStage.getY());
		updateStage.show();
		
		updateStage.setOnCloseRequest(cl -> {
			mainStage.setX(400);
		});
	}
	
	public ObservableList<InventoryItem> getInventoryItem(){
		ObservableList<InventoryItem> inventoryItems = FXCollections.observableArrayList();
		try {
			Connection con = DBConnector.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from inventory");
			
			while(rs.next())
				inventoryItems.add(new InventoryItem(rs.getInt("item_id"),rs.getString("item_name"),rs.getInt("item_qty"),rs.getFloat("item_price")));
			
			rs.close();
		}catch(SQLException e) {
			System.out.println("getInventoryList: "+e);
			e.printStackTrace();
		}
		
		return inventoryItems;
	}
	
	
	public static void main(String[] args)  {
		launch(args);
	}

}