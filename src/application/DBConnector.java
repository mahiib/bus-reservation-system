package application;

import java.sql.*;

import javafx.collections.ObservableList;

public class DBConnector {
	   static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	   static final String USER = "root";
	   static final String PASS = "mahesh123";
	   static float totalPrice = 0;
	   
	   public static Connection getConnection() {
		   Connection con = null;
		   try {
			   con = DriverManager.getConnection(DB_URL, USER, PASS);
		   }catch(SQLException e) {
			   System.out.println("Get Connection : "+e);
			   e.printStackTrace();
		   }
		   
		   return con;
	   }
	   
	   public static void insertCustomer(Customer item) {
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
		       String sql = "insert into customers(name,phone,address,owed) values ('"+item.getName()+"','"+item.getPhone()+"','"+item.getAddress()+"','"+item.getOwed()+"')";
		       stmt.executeUpdate(sql);
		       conn.close();
			   
		   }catch(SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   
	   public static void updateCustomer(Customer item) {
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);			   
		       String sql = "update customers set name= ?,phone= ?,address= ?,owed= ? where id= ?";
		       PreparedStatement stmt = conn.prepareStatement(sql);
		       stmt.setString(1, item.getName());
		       stmt.setString(2, item.getPhone());
		       stmt.setString(3, item.getAddress());
		       stmt.setFloat(4, item.getOwed());
		       stmt.setInt(5, item.getId());
		       
		       stmt.executeUpdate();
		       
		       conn.close();
		   }catch(SQLException e) {
			   System.out.println("Customer Update: "+e);
			   e.printStackTrace();
		   }
	   }
	   
	   public static void logSales(Cart sold) {
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   PreparedStatement stmt = conn.prepareStatement("insert into sales(prod_id,prod_name,qty_sold,sell_price) values(?,?,?,?)");
			   stmt.setInt(1, sold.getId());
			   stmt.setString(2, sold.getName());
			   stmt.setInt(3, sold.getQty());
			   stmt.setFloat(4, sold.getPrice());
			   stmt.executeUpdate();
			   conn.close();
		   }catch(SQLException e) {
			   System.out.println("Log Sales: "+e);
			   e.printStackTrace();
		   }
	   }
	   
	   public static String getAll() {
		   String response = "";
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery("select * from inventory");
			   while(rs.next()) {
				   response = response.concat(String.format("%s,%s,%s,%s;",Integer.toString(rs.getInt("item_id")),rs.getString("item_name"),Integer.toString(rs.getInt("item_qty")),Float.toString(rs.getFloat("item_price"))));
			   }
			   conn.close();
		   }catch(SQLException e) {
			   System.out.println("GetAll failed : "+e);
			   e.printStackTrace();
		   }
		   return response;
	   }
	   
	   public static InventoryItem getById(int id) {
		   InventoryItem foundItem = new InventoryItem();
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery(String.format("select * from inventory where item_id = %d", id));
			   rs.next();
			   foundItem.setId(rs.getInt("item_id"));
			   foundItem.setName(rs.getString("item_name"));
			   foundItem.setPrice(rs.getFloat("item_price"));
			   foundItem.setQty(rs.getInt("item_qty"));
			   conn.close();
		   }catch(SQLException e) {
			   System.out.println("GetById failed : "+e);
			   e.printStackTrace();
		   }
		   return foundItem;
	   }

	   public static InventoryItem insert(InventoryItem item) {
		      // Open a connection
		   InventoryItem newItem = new InventoryItem();
			   try {
				   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				   Statement stmt = conn.createStatement();
			       String sql = "insert into inventory(item_name,item_qty,item_price) values ('"+item.getName()+"','"+item.getQty()+"','"+item.getPrice()+"')";
			       stmt.executeUpdate(sql);
			       //Retrieve inserted
				   Statement stmt1 = conn.createStatement();
			       ResultSet rs = stmt1.executeQuery("select * from inventory order by item_id desc limit 1");
			       rs.next();

			       newItem.setId(rs.getInt("item_id"));
			       newItem.setName(rs.getString("item_name"));
			       newItem.setQty(rs.getInt("item_qty"));
			       newItem.setPrice(rs.getFloat("item_price"));
			       conn.close();
				   
			   }catch(SQLException e) {
				   e.printStackTrace();
			   }
			   
			   return newItem;
		   }
	   
	   public static void delete(int id) {
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
		       String sql = "delete from inventory where item_id = '"+id+"'";
		       stmt.executeUpdate(sql);
		       conn.close();
			   
		   }catch(SQLException e) {
			   e.printStackTrace();
		   }
		   
	   }
	   
	   public static void update(InventoryItem item) {
		   try {
			   //Check if entry exists
			   InventoryItem searchedItem = getById(item.getId());
			   if(searchedItem.getId() == 0) {
				   System.out.println("Item not found cannot update");
			   }
			   else {
				 //Validate the updating data
				   if(!item.getName().equals(""))
					   searchedItem.setName(item.getName());
				   
				   if(item.getQty() != 0)
					   searchedItem.setQty(item.getQty());
				  
				   if(item.getPrice() != 0.0)
					   searchedItem.setPrice(item.getPrice());
				   
				   //Do the udpate
				   
				   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);			   
			       String sql = "update inventory set item_name= ?,item_qty= ?,item_price= ? where item_id= ?";
			       PreparedStatement stmt = conn.prepareStatement(sql);
			       stmt.setInt(4, searchedItem.getId());
			       stmt.setString(1, searchedItem.getName());
			       stmt.setInt(2,searchedItem.getQty());
			       stmt.setFloat(3,searchedItem.getPrice());
			       stmt.executeUpdate();
			       
			       conn.close();
			   }
		   }catch(SQLException e) {
			   System.out.println("Update function failed: "+e);
			   e.printStackTrace();
		   }
	   }
	   
	   public static float sell(ObservableList<Cart> list ) {
		   //Read the list and calculate the total price
		   //Return the total price
		   //log the sale into sales table
		   //Create a Sales object for logSales()
		   //Update the inventory table
		   //Create new InventoryItem for each update()
		    
		    	list.forEach((item) -> {
		    		totalPrice += item.getPrice(); 
		    		logSales(new Cart(item.getId(),item.getName(),item.getQty(),item.getPrice()));
		    		int oldQty = getById(item.getId()).getQty();//Quantity left
		    		float price = getById(item.getId()).getPrice();//Item price
		    		int newQty = oldQty - item.getQty();
		    		update(new InventoryItem(item.getId(),item.getName(),newQty,price));
		    	});
		   

		   return totalPrice;
	   }
	   
	   public static String login(String username) {
		   String response = "";
		   try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery("select * from users where username = '"+username+"'");
			   while(rs.next()) {
				   response = response.concat(String.format("%s",rs.getString("password")));
			   }
			   conn.close();
		   }catch(SQLException e) {
			   System.out.println("Get User failed : "+e);
			   e.printStackTrace();
		   }
		   return response;
	   }
	   
}