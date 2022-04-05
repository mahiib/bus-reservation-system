package application;

public class InventoryItem {
	private int id;
	private String name;
	private int qty;
	private float price;
	
	public InventoryItem() {
		this.setId(0);
		this.setName("");
		this.setQty(0);
		this.setPrice(0);
	}
	
	public InventoryItem(int id,String name,int qty,float price) {
		this.setId(id);
		this.setName(name);
		this.setQty(qty);
		this.setPrice(price);
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