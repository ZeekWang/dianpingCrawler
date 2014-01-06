package dianpingCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogRecord;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlComment {
	private static String fileName = "stores.csv"; 
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	private static String baseUrl = "http://api.map.baidu.com/place/v2/search?ak=B2deebf8d1163f8a81d348ab329216d3&output=json&page_size=1&page_num=0&scope=1&region=北京&query=";
	private static boolean isAppend = true;
	private static String dataFile = "comment.csv";
	private static String logFile = "commentlog.txt";
	
	private static ArrayList<String> crawledStores = new ArrayList<String>();
	private static ArrayList<String[]> stores = new ArrayList<String[]>();
	
	public static void main(String[] args) throws IOException, JSONException {
		
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");
		BufferedReader listReader = new BufferedReader(isr);
		BufferedReader logReader = new BufferedReader(new FileReader(logFile));
				
		FileOutputStream fos = new FileOutputStream(dataFile, isAppend);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
		BufferedWriter dataWriter = new BufferedWriter(osw);
		FileWriter logWriter = new FileWriter(logFile, isAppend);
		
		//读取已爬取的数据
		if (isAppend)
			readLog(logReader);
		//读取店家列表
		stores = readStores(listReader);
		for (String[] store: stores) {
			if (crawledStores.indexOf(store[2]) < 0 ) {
				ArrayList<Comment> comments = crawlComment(store[2]);
				writeData(dataWriter, comments);
				writeLog(logWriter, store[2]);
			}
		}
		logWriter.close();
		dataWriter.close();
	}
	
	private static ArrayList<Comment> crawlComment(String storeID) throws IOException {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		String baseUrl = "http://www.dianping.com/shop/" + storeID + "/review_all?";
		int page = 1;
		boolean flag = true;
		while (flag) {
			String url = baseUrl + "pageno=" + page;
			System.out.println(url);
			Document document = Jsoup.connect(url).userAgent(userAgent).get();
			Elements nodes = document.select(".comment-list > ul > li");
			if (nodes.size() == 0)
				flag = false;
			for (int i = 0; i < nodes.size(); i++) {
				Comment comment = new Comment();
				Element node = nodes.get(i);
				String commentID = node.attr("data-id");
				String userName = node.select(".name > a").text();
				String userID = node.select(".name > a").attr("href").replace("/member/", "");
				String price = node.select(".comm-per").text().replaceAll(" ", "").replace("人均￥", "");
				String rate = node.select(".user-info .item-rank-rst").attr("class").replace("item-rank-rst irr-star", "");
				Elements rsts = node.select(".comment-rst > .rst");
				String comment1 = "-1", comment2 = "-1", comment3 = "-1";
				if (rsts.size() == 3) {
					comment1 = rsts.get(0).text().replace("口味", "").substring(0, 1);
					comment2 = rsts.get(1).text().replace("环境", "").substring(0, 1);
					comment3 = rsts.get(2).text().replace("服务", "").substring(0, 1);
				}
				String recommend = node.select(".comment-recommend").text().replaceAll(",", " ");
				String time = node.select(".time").text();
				if (time.startsWith("01-"))
					continue;
				if (!time.startsWith("13")) {
					flag = false;
					break;
				}
				time = time.substring(0, 8);
				comment.setStoreID(storeID);
				comment.setCommentID(commentID);
				comment.setUserName(userName);
				comment.setUserID(userID);
				comment.setRate(rate);
				comment.setPrice(price);
				comment.setComment1(comment1);
				comment.setComment2(comment2);
				comment.setComment3(comment3);
				comment.setRecommend(recommend);
				comment.setTime(time);
				System.out.println(comment);
				comments.add(comment);
			}
			page++;
		}
		return comments;
	}
	
	private static void readLog(BufferedReader reader) throws IOException {
		String str;
		while ( (str = reader.readLine()) != null) {
			crawledStores.add(str);
		}
	}
	
	private static void writeLog(FileWriter writer, String id) throws IOException {
		writer.write(id + "\n");
		writer.flush();
	}
	
	private static void writeData(BufferedWriter writer, ArrayList<Comment> comments) throws IOException {
		for (int i = 0; i < comments.size(); i++) {
			writer.write(comments.get(i).toString() + "\n");
		}
		writer.flush();
	}
	
	private static ArrayList<String[]> readStores(BufferedReader reader) throws IOException {
		String str;
		ArrayList<String[]> stores = new ArrayList<String[]>();
		reader.readLine();
		while ( (str = reader.readLine()) != null) {
			String[] strs = str.split(",");
			stores.add(strs);
		}
		return stores;
	}
	

}

class Comment {
	private String storeID;
	private String commentID;
	private String userName;
	private String userID;
	private String price;
	private String comment1, comment2, comment3;
	private String recommend;
	private String time;
	private String rate;

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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getComment1() {
		return comment1;
	}
	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}
	public String getComment2() {
		return comment2;
	}
	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}
	public String getComment3() {
		return comment3;
	}
	public void setComment3(String comment3) {
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
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}

}
