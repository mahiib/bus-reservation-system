package application;

import java.sql.Date;
import java.sql.Timestamp;

public class Sales {

	int id;
	int prod_id;
	String prod_name;
	int qty_sold;
	float sell_price;
	Timestamp timestamp;
	
	public Sales() {
		this.id = 0;
		this.prod_id = 0;
		this.prod_name = "";
		this.qty_sold = 0;
		this.sell_price = (float)0.0;
		this.timestamp = new Timestamp(6000);
	}
	
	public Sales(int id,int prod_id,String prod_name,int qty_sold,float sell_price,Timestamp timestamp) {
		this.id = id;
		this.prod_id = prod_id;
		this.prod_name = prod_name;
		this.qty_sold = qty_sold;
		this.sell_price = sell_price;
		this.timestamp = timestamp;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public int getQty_sold() {
		return qty_sold;
	}
	public void setQty_sold(int qty_sold) {
		this.qty_sold = qty_sold;
	}
	public float getSell_price() {
		return sell_price;
	}
	public void setSell_price(float sell_price) {
		this.sell_price = sell_price;
	}	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}



}