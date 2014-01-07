package storeCluster;

import java.util.ArrayList;
import java.util.HashMap;

import struct.Comment;
import struct.ReadData;
import struct.Store;

public class MonthCount {
	private HashMap<String, Store> storeIDMap;
	private HashMap<String, ArrayList<Comment>> storeCommentMap;
	private HashMap<String, int[]> typeMonthCountMap = new HashMap<String, int[]>();
	private int[] totalCounts = new int[13];
	
	public MonthCount(ReadData data) {
		storeIDMap = data.getStoreIDMap();
		storeCommentMap = data.getStoreCommentMap();
		
		
		for (String storeID : storeCommentMap.keySet()) {
			String type = storeIDMap.get(storeID).getType();
			String[] types = type.split(" ");
			for (int i = 0; i < types.length; i++) {
				type = types[i];
				if (!typeMonthCountMap.containsKey(type) ) {
					typeMonthCountMap.put(type, new int[13]);
				}
				ArrayList<Comment> comments = storeCommentMap.get(storeID);
				for (Comment tpComment: comments) {
					typeMonthCountMap.get(type)[tpComment.getMonth()]++;
					totalCounts[tpComment.getMonth()]++;
				}
			}
		}
		
		for (String type:typeMonthCountMap.keySet()) {
			int[] monthCounts = typeMonthCountMap.get(type);
			System.out.print(type + ":");
			for (int i = 1; i <= 12; i++)
				System.out.print(  Math.round( ((float)monthCounts[i]/(float)totalCounts[i]) *1000) + " ");
			System.out.println();
		}
	}
}
