package struct;


public class Comment {
	private String storeID;
	private String commentID;
	private String userName;
	private String userID;
	private int price;
	private float comment1, comment2, comment3;
	private String recommend;
	private String time;
	private int rate;
	private int month;
	private int date;

	public Comment(String string) {
		String[] strs = string.split(",");
		commentID = strs[0];
		storeID = strs[1];
		userName = strs[2];
		userID = strs[3];
		rate = -1;
		if (strs[4].length() > 0)
			rate = Integer.parseInt(strs[4]);
		price = -1;
		if (strs[5].length() > 0)
			price = Integer.parseInt(strs[5]);
		comment1 = -1;
		if (strs[6].length() > 0)
			comment1 = Float.parseFloat(strs[6]);
		comment2 = -1;
		if (strs[7].length() > 0)
			comment2 = Float.parseFloat(strs[7]);
		comment3 = -1;
		if (strs[8].length() > 0)
			comment3 = Float.parseFloat(strs[8]);
		recommend = strs[9];
		time = strs[10];
		String[] timeStrs = time.split("-");
		month = Integer.parseInt(timeStrs[1]);
		date = Integer.parseInt(timeStrs[2]);
				
				
	}
	
	public Comment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return commentID+","+storeID+","+userName+","+userID+","+rate+","+price+","
				+comment1+","+comment2+","+comment3+","+recommend+","+time;
	}
	

	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getCommentID() {
		return commentID;
	}
	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}


}
