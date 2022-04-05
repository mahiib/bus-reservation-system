package application;

public class Cart {
	int id;
	String name;
	int qty;
	float price;
	
	public Cart() {
		this.id = 0;
		this.name = "";
		this.qty = 0;
		this.price = 0;
	}
	
	public Cart(int id,String name,int qty,float price) {
		this.id = id;
		this.name = name;
		this.qty = qty;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}