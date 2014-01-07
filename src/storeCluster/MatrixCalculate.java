package storeCluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import struct.Comment;
import struct.ReadData;
import struct.Store;

public class MatrixCalculate {
	private HashMap<String, Store> storeIDMap;
	HashMap<String, Integer> relateStoreCountMap = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Comment>> userCommentMap;
	public MatrixCalculate(ReadData data) {
		storeIDMap = data.getStoreIDMap();
		userCommentMap = data.getUserCommentMap();
		System.out.println("user count:" + userCommentMap.keySet().size());
		for (String userID : userCommentMap.keySet()) {
			ArrayList<Comment> commentList = userCommentMap.get(userID);
			for (int i = 0; i < commentList.size(); i++) {
				for (int j = i + 1; j < commentList.size(); j++) {
					String storeID1 = commentList.get(i).getStoreID();
					String storeID2 = commentList.get(j).getStoreID();
					String key = "";
					if (storeID1.compareTo(storeID2) < 0)
						key = storeID1 + "-" + storeID2;
					else 
						key = storeID2 + "-" + storeID1;
					if (relateStoreCountMap.containsKey(key))
						relateStoreCountMap.put(key, relateStoreCountMap.get(key) + 1);
					else
						relateStoreCountMap.put(key, 1);
				}
			}
		}
		ArrayList<String> titleList = new ArrayList<String>();
		ArrayList<Double> valueList = new ArrayList<Double>();
		
		for (String key : relateStoreCountMap.keySet()) {
			String[] keys = key.split("-");
			Store store1 = storeIDMap.get(keys[0]);
			Store store2 = storeIDMap.get(keys[1]);
			double value = (double)relateStoreCountMap.get(key) / Math.max( store1.getComment_count(), store2.getComment_count() ) ;
			if ( value > 0.01) {
				titleList.add(store1.getName() + "," + store1.getType() + "  " + store2.getName() + "," + store2.getType() + " -- " + relateStoreCountMap.get(key) + "," + value );
				titleList.add(store2.getName() + "," + store2.getType() + "  " + store1.getName() + "," + store1.getType() + " -- " + relateStoreCountMap.get(key) + "," + value );
			}
		}
		Collections.sort(titleList);
		for (String key : titleList)
			System.out.println(key);
	}
}
