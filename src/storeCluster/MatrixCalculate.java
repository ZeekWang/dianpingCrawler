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
//		ArrayList<String> titleList = new ArrayList<String>();
//		ArrayList<Double> valueList = new ArrayList<Double>();
//		
//		for (String key : relateStoreCountMap.keySet()) {
//			String[] keys = key.split("-");
//			Store store1 = storeIDMap.get(keys[0]);
//			Store store2 = storeIDMap.get(keys[1]);
//			double value = (double)relateStoreCountMap.get(key) / ( store1.getComment_count() + store2.getComment_count() ) * 2 ;
//			if ( value > 0.005 && relateStoreCountMap.get(key) >= 5) {
//				titleList.add(store1.getName() + "," + store1.getType() + "  " + store2.getName() + "," + store2.getType() + " -- " + relateStoreCountMap.get(key) + ",  " + value );
//				titleList.add(store2.getName() + "," + store2.getType() + "  " + store1.getName() + "," + store1.getType() + " -- " + relateStoreCountMap.get(key) + ",  " + value );
//			}
//		}
//		Collections.sort(titleList);
//		for (String key : titleList)
//			System.out.println(key);
		
		
		System.out.println("start cluster");
		double[][] distanceMatrix = new double[data.getStores().size()][data.getStores().size()];
		for (int i = 0; i < data.getStores().size(); i++) {
			for (int j = 0; j < data.getStores().size(); j++) { 
				if (i == j) {
					distanceMatrix[i][j] = -1;
					continue;
				}
				Store store1 = data.getStores().get(i);
				Store store2 = data.getStores().get(j);
				String key = "";
				if (store1.getId().compareTo(store2.getId()) < 0)
					key = store1.getId() + "-" + store2.getId();
				else 
					key = store2.getId() + "-" + store1.getId();
				double value;
				if (relateStoreCountMap.containsKey(key)) {
//					value = (double)relateStoreCountMap.get(key) / ( store1.getComment_count() + store2.getComment_count() ) * 2 ;
					value = relateStoreCountMap.get(key);
				} else {
//					value = 0;
					value = 1;
				}
//				distanceMatrix[i][j] = 1 - value;
				distanceMatrix[i][j] = 1 / value;
			}
		}
		
		
//		for (int i = 0; i < distanceMatrix.length; i++) {
//			for (int j = 0; j < distanceMatrix.length; j++) {
//				System.out.print(distanceMatrix[i][j]  + " ");	
//			}
//			System.out.println();
//		}
//		
		
		
		HierarchyCluster hc = new HierarchyCluster(distanceMatrix, 100);
		ArrayList<ArrayList<Integer>> clusterList = hc.getClusterList();

		int clusterCount = 0;
		for (int i = 0; i < clusterList.size(); i++) {
			if (clusterList.get(i).size() > 0) {
				clusterCount++;
				System.out.println(clusterCount + ":" + clusterList.get(i).size());
				for (int j : clusterList.get(i)) {
//					System.out.println("   " + data.getStores().get(j).getName() ) ;
				}
				System.out.println();
			}
		
		}

		
		
	}
}
