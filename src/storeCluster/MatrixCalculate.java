package storeCluster;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import struct.Comment;
import struct.ReadData;
import struct.Store;

public class MatrixCalculate {
	private HashMap<String, Store> storeIDMap;
	HashMap<String, Integer> relateStoreCountMap = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Comment>> userCommentMap;
	HashSet<String> validStores = new HashSet<String>();
	ArrayList<Store> stores = new ArrayList<Store>();
	public MatrixCalculate(ReadData data) throws IOException {
		
		FileOutputStream fos = new FileOutputStream("cluster.json");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
		BufferedWriter bw = new BufferedWriter(osw);		
		
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
					validStores.add(storeID1);
					validStores.add(storeID2);
				}
			}
		}
		for (int i = 0; i < data.getStores().size(); i++) {
			if (validStores.contains(data.getStores().get(i).getId()))
				stores.add(data.getStores().get(i));
		}
		System.out.println(stores.size());
		
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
		int matrixSize = stores.size();
		int clusterSize = 250;
		double[][] distanceMatrix = new double[matrixSize][matrixSize];
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) { 
				if (i == j) {
					distanceMatrix[i][j] = -1;
					continue;
				}
				Store store1 = stores.get(i);
				Store store2 = stores.get(j);
				
				String key = "";
				if (store1.getId().compareTo(store2.getId()) < 0)
					key = store1.getId() + "-" + store2.getId();
				else 
					key = store2.getId() + "-" + store1.getId();
				double value;
				if (relateStoreCountMap.containsKey(key)) {
//					value = (double)relateStoreCountMap.get(key) / ( store1.getComment_count() + store2.getComment_count() ) * 2 ;
					value = (double)relateStoreCountMap.get(key) / store1.getComment_count() + (double)relateStoreCountMap.get(key) / store2.getComment_count();				
//					value = relateStoreCountMap.get(key);
				} else {
//					value = 0;
					value = (double)1 / 2000 + (double)1 / 2000;
				}

//				distanceMatrix[i][j] = 1 - value;
				distanceMatrix[i][j] = 1 / value;
				if (distanceMatrix[i][j] > 100000000)
					System.out.println(i + " " + j + " " + distanceMatrix[i][j]);

			}
		}
		
//		
//		for (int i = 0; i < distanceMatrix.length; i++) {
//			for (int j = 0; j < distanceMatrix.length; j++) {
//				System.out.print(distanceMatrix[i][j]  + "\t");	
//			}
//			System.out.println();
//		}
//		
		
		
		HierarchyCluster hc = new HierarchyCluster(distanceMatrix, clusterSize);
		ArrayList<ArrayList<Integer>> clusterList = hc.getClusterList();
		
		JSONArray clusterResults = new JSONArray();
		int clusterCount = 0;
		for (int i = 0; i < clusterList.size(); i++) {
			if (clusterList.get(i).size() > 10) {
				JSONArray singleCluster = new JSONArray();
				clusterResults.put(singleCluster);
				
				clusterCount++;
				System.out.println(clusterCount + ":" + clusterList.get(i).size());
				for (int j : clusterList.get(i)) {
					singleCluster.put(stores.get(j).getId());
					System.out.println("   " + stores.get(j).getName() + "   **   " + stores.get(j).getLabel()) ;
				}
				System.out.println();
			}
		}
		
		bw.write(clusterResults.toString());
		
		bw.close();
		

		
		
	}
}
