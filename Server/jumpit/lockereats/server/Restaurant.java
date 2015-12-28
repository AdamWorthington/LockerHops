package jumpit.lockereats.server;

public class Restaurant {
	String name;		//Name of the restaurant
	String address;		//Restaurant's address
	String phone;		//Restaurant's phone number
	String website;		//Restaurant's website (www.xyz.com)
	String type;		//Restaurant's type (American, mexican, pasta, pizza, etc)
	
	public Restaurant(String name, String address, String phone, String website, String type) {
		this.name 		= name;
		this.address 	= address;
		this.phone 		= phone;
		this.website 	= website;
		this.type 		= type;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getWebsite() {
		return this.website;
	}

	public String getType() {
		return this.type;
	}
}
