package storeCluster;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import struct.Comment;
import struct.ReadData;
import struct.Store;

public class KNNForUser {
	HashMap<String, ArrayList<Comment>> userCommentMap;
	HashMap<String, ArrayList<Comment>> storeCommentMap;
	HashMap<String, Store> storeIDMap;
	ArrayList<String> userSet = new ArrayList<String>();
	ArrayList<String> trainUserSet = new ArrayList<String>();
	ArrayList<String> testUserSet = new ArrayList<String>();
	HashMap<String, Integer> commonStoreCountMap = new HashMap<String, Integer>();
	
	int K = 2;
	int testCommentNum = 10;
	int estiStoreNum = 10;
	double accurate = 0;
	
	public KNNForUser(ReadData data) throws FileNotFoundException {
		int sumResult = 0;
		int countResult = 0;
		userCommentMap = data.getUserCommentMap();
		storeCommentMap = data.getStoreCommentMap();
		storeIDMap = data.getStoreIDMap();
		Random random = new Random();
		int count = 0;

		
		for (String userID : userCommentMap.keySet()) {
			ArrayList<Comment> comments = userCommentMap.get(userID);
			if (comments.size() >= 30) {
				userSet.add(userID);
				if (random.nextFloat() < 0.9)
					trainUserSet.add(userID);
				else 
					testUserSet.add(userID);
				count++;
			}
		}
		
//		for (String userID1 : userSet) {
//			for (String userID2 : userSet) {
//				if (userID1.compareTo(userID2) < 0) {
//					ArrayList<Comment> comments1 = userCommentMap.get(userID1);
//					ArrayList<Comment> comments2 = userCommentMap.get(userID2);
//					count = 0;
//					for (int i = 0; i < comments1.size(); i++) {
//						for (int j = 0; j < comments2.size(); j++) {
//							if (comments1.get(i).getStoreID().equals(comments2.get(j).getStoreID()))
//								count++;
//						}
//					}
//					commonStoreCountMap.put(userID1 + "-" + userID2, count);
//					commonStoreCountMap.put(userID2 + "-" + userID1, count);
//				}
//			}
//		}
		
//		System.out.println("train num:" + trainUserSet.size() + "   test num:" + testUserSet.size());
		
		for (int i = 0; i < testUserSet.size(); i++) {
			String testUserID = testUserSet.get(i);
			ArrayList<Comment> testComments = userCommentMap.get(testUserID);
			HashSet<String> testStores = new HashSet<String>();
			HashMap<Double, String> commonTrainUserMap = new HashMap<Double, String>();
			HashMap<String, Double> storeValueMap = new HashMap<String, Double>();
			HashMap<String, Double> nearUsers = new HashMap<String, Double>();
			
			for (int j = 0; j < testComments.size() - testCommentNum; j++) {
				Comment tpComment = testComments.get(j);
				testStores.add(tpComment.getStoreID());
			}
			for (String trainUserID : trainUserSet) {
				ArrayList<Comment> tpCommentList = userCommentMap.get(trainUserID);
				int sameStoreCount = 0;
				for (int j = 0; j < tpCommentList.size(); j++) {
					if (testStores.contains(tpCommentList.get(j).getStoreID()))
						sameStoreCount++;
				}
				if (sameStoreCount > 3){
					commonTrainUserMap.put((double)sameStoreCount/tpCommentList.size() , trainUserID);
				}
			}
			
			if (commonTrainUserMap.size() < K) 
				continue;
			
			ArrayList<Double> commonTrainUserRateList = new ArrayList<Double>();
			for (Double tempRate : commonTrainUserMap.keySet()) {
				commonTrainUserRateList.add(tempRate);
			}
			Collections.sort(commonTrainUserRateList);
			
			//找到近邻
			for (int j = commonTrainUserRateList.size() - 1; j >= commonTrainUserRateList.size() - K; j--) {
				nearUsers.put(commonTrainUserMap.get(commonTrainUserRateList.get(j)), commonTrainUserRateList.get(j));
			}
//			System.out.println(i + " " + nearUsers.size());
			
			
			//对商家进行估值
			for (String userID: nearUsers.keySet()) {
				ArrayList<Comment> tpComments = userCommentMap.get(userID);
//				double weight = nearUsers.get(userID);
				double weight = 1;
				for (Comment tpComment : tpComments) {
					String storeID = tpComment.getStoreID();
					if (!storeValueMap.containsKey(storeID)) 
						storeValueMap.put(storeID, 0d);
					storeValueMap.put(storeID, storeValueMap.get(storeID) + weight);
				}
			}
			
			//对估值进行排序
			List<String> sortedStoreList = sortHashMapByValue(storeValueMap);
//			for (int j = 0; j < 10; j++)
//				System.out.println(storeIDMap.get(sortedStoreList.get(j)).getName() );
			ArrayList<String> estiStores = new ArrayList<String>();
			int order = 0;
			while (estiStores.size() < estiStoreNum) {
				if (order >= sortedStoreList.size())
					break;
				if ( ! testStores.contains(sortedStoreList.get(order)) ) {
					estiStores.add(sortedStoreList.get(order));
//					System.out.println(storeIDMap.get(sortedStoreList.get(order)) == null ?
//							"" : storeIDMap.get(sortedStoreList.get(order)).getName());
				}
				order++;
			}
			
//			System.out.println("***********");
			
			int resultCount = 0;
			for (int j = testComments.size() - testCommentNum; j < testComments.size(); j++) {
//				System.out.println(testComments.get(j));
//				System.out.println( storeIDMap.get(testComments.get(j).getStoreID()) == null ? 
//						"" : storeIDMap.get(testComments.get(j).getStoreID()).getName() );
				int index = estiStores.indexOf(testComments.get(j).getStoreID());
				if (index >= 0)
					resultCount++;
			}
			sumResult += resultCount;
			countResult ++;
//			System.out.println("**" + resultCount + "**");
			
			

		}
		
		System.out.println("平均猜中数：" + ( (float)sumResult / countResult));
		accurate = (float)sumResult / countResult;

	}
	
	public double getAccurate() {
		return accurate;
	}
	
	private List<String> sortHashMapByValue(HashMap<String, Double> map) {
	    List<String> keyList = new LinkedList();
	    keyList.addAll(map.keySet());
	    List<Double> valueList = new LinkedList();
	    valueList.addAll(map.values());

	    for(int i = 0; i < valueList.size(); i++) 
	       for(int j= i + 1; j < valueList.size(); j++) {
	          if(valueList.get(i) < valueList.get(j)) {
	        	  Double tpValue = valueList.get(j);
	              valueList.set(j, valueList.get(i));
	              valueList.set(i, tpValue);
	              
	              String tpKey = keyList.get(j);
	              keyList.set(j, keyList.get(i));
	              keyList.set(i, tpKey);
	          }
	       }
	    
	    return keyList;
	}
 }
