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

public class CalculateStoreRate {
	public CalculateStoreRate(ReadData data) throws IOException {

		FileOutputStream fos = new FileOutputStream("stores_rate.csv");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
		BufferedWriter bw = new BufferedWriter(osw);

		HashMap<String, ArrayList<Comment>> storeCommentMap = data.getStoreCommentMap();
		ArrayList<Store> stores = data.getStores();
		for (int i = 0; i < stores.size(); i++) {
			
			Store store = stores.get(i);
			System.out.println(store.getId());
			ArrayList<Comment> comments = storeCommentMap.get(store.getId());
			if (comments == null) {
				store.setRate(-1);
			} else { 
				int sum = 0;
				int count = 0;
				for (int j  = 0; j < comments.size(); j++) {
					if (comments.get(j).getRate() >= 0) {
						sum += comments.get(j).getRate();
						count ++;
					}
				}
				store.setRate(  (int)Math.round((double)sum / count)  );
			}
			bw.write(store.toString()+"\n");
			System.out.println(store.toString());
			bw.flush();
		}
		bw.close();
		
	}
}
