package struct;

public class Store {
	private String name;
	private String url;
	private String id;
	private String type;
	private String address;
	private String label;
	private String feature;
	private float price;
	private float comment1;
	private float comment2;
	private float comment3;
	private int comment_count;
	private float lng;
	private float lat;
	private int rate;
	
	public Store(String string) {
		String[] strs = string.split(",");
		name = strs[0]; 
		url = strs[1];
		id = strs[2];
		type = strs[3];
		address = strs[4];
		label = strs[5];
		feature = strs[6];
		price = Float.parseFloat(strs[7]);
		comment1 = Float.parseFloat(strs[8]);
		comment2 = Float.parseFloat(strs[9]);
		comment3 = Float.parseFloat(strs[10]);
		comment_count = Integer.parseInt(strs[11]);
		lng = Float.parseFloat(strs[12]);
		lat = Float.parseFloat(strs[13]);
	}
	
	
	@Override
	public String toString() {
		return name+","+url+","+id+","+type+","+address+","+label+","+feature
				+","+price+","+rate+","+comment1+","+comment2+","+comment3+","+comment_count+
				","+lng+","+lat;
		
	}	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getComment1() {
		return comment1;
	}
	public void setComment1(float comment1) {
		this.comment1 = comment1;
	}
	public float getComment2() {
		return comment2;
	}
	public void setComment2(float comment2) {
		this.comment2 = comment2;
	}
	public float getComment3() {
		return comment3;
	}
	public void setComment3(float comment3) {
		this.comment3 = comment3;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getRate() {
		return rate;
	}
	
}
