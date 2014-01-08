package storeCluster;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import struct.Comment;
import struct.ReadData;
import struct.Store;

public class CalculateAccordingUser {
	public CalculateAccordingUser(ReadData data) throws IOException {
		FileOutputStream fos = new FileOutputStream("comments_2.csv");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
		BufferedWriter bw = new BufferedWriter(osw);

		HashMap<String, ArrayList<Comment>> userCommentMap = data.getUserCommentMap();
		HashMap<String, Store> storeIDMap = data.getStoreIDMap();
		
		int count = 0;
		for (String userID : userCommentMap.keySet()) {
			ArrayList<Comment> comments = userCommentMap.get(userID);
			if (comments.size() >= 3) {
				int sumPrice = 0;
				int countPrice = 0;
				System.out.println(comments.size());
				for (Comment comment : comments) {
					if (comment.getPrice() < 0) {
						Store store = storeIDMap.get(comment.getStoreID());
						if (store != null) {
							comment.setPrice(store.getPrice());
						}
					}
					Store store = storeIDMap.get(comment.getStoreID());
					if (store != null) {
						sumPrice += store.getPrice();
						countPrice++;
					}
				}
				int averagePrice = (int)Math.round( (float)sumPrice / (float)countPrice );
				for (Comment comment : comments) {
					Store store = storeIDMap.get(comment.getStoreID());
					if (store != null) {
						String[] sLabels = store.getLabel().split(" ");
						String type = "";
						if (sLabels.length > 1) 
							type = sLabels[sLabels.length - 1];
						bw.write(comment.toString() + "," + type + "," + averagePrice + "," + store.getName() 
								+  "," + comments.size()  + "\n");
						bw.flush();
					}
				}
			}
			count++;
		}
		System.out.println(count);
	}
}
