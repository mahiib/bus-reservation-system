package application;

public class Customer {
	int id;
	String name;
	String phone;
	String address;
	float owed;
	
	public Customer() {
		this.id = 0;
		this.name = "";
		this.phone = "";
		this.address = "";
		this.owed = (float) 0.0;
	}
	
	public Customer(int id,String name,String phone,String address,float owed) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.owed = owed;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public float getOwed() {
		return owed;
	}

	public void setOwed(float owed) {
		this.owed = owed;
	}
	
	
}