package struct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadData {
	private String storeFileName = "stores.csv";
	private String commentFileName = "comments.csv";
	private ArrayList<Store> stores = new ArrayList<Store>();
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private HashMap<String, ArrayList<Comment>> userCommentMap = new HashMap<String, ArrayList<Comment>>();
	private HashMap<String, ArrayList<Comment>> storeCommentMap = new HashMap<String, ArrayList<Comment>>();
	private HashMap<String, Store> storeIDMap = new HashMap<String, Store>();
	
	public ReadData() throws IOException {
		InputStreamReader storeISR = new InputStreamReader(new FileInputStream(storeFileName), "GBK");
		BufferedReader storeReader = new BufferedReader(storeISR);
		
		InputStreamReader commentISR = new InputStreamReader(new FileInputStream(commentFileName), "GBK");
		BufferedReader commentReader = new BufferedReader(commentISR);
		
//		BufferedReader storeReader = new BufferedReader(new FileReader(storeFileName));
//		BufferedReader commentReader = new BufferedReader(new FileReader(commentFileName));
		
		String str = "";
		str = storeReader.readLine();
		while ( (str = storeReader.readLine()) != null) {
			Store store = new Store(str);
			stores.add(store);
			storeIDMap.put(store.getId(), store);
		}
		
		str = commentReader.readLine();
		while ( (str = commentReader.readLine()) != null ) {
			Comment comment = new Comment(str);
			comments.add(comment);
			
			if (!storeCommentMap.containsKey(comment.getStoreID())) {
				storeCommentMap.put(comment.getStoreID(), new ArrayList<Comment>());
			} 
			storeCommentMap.get(comment.getStoreID()).add(comment);
			
			if (!userCommentMap.containsKey(comment.getUserID())) {
				userCommentMap.put(comment.getUserID(), new ArrayList<Comment>());
			} 
			userCommentMap.get(comment.getUserID()).add(comment);
		}
		
	}
	
	public ArrayList<Store> getStores() {
		return stores;
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}
	
	public HashMap<String, ArrayList<Comment>> getUserCommentMap() {
		return userCommentMap;
	}
	
	public HashMap<String, Store> getStoreIDMap() {
		return storeIDMap;
	}
	
	public HashMap<String, ArrayList<Comment>> getStoreCommentMap() {
		return storeCommentMap;
	}
}
