package dianpingCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlLocation {
	private static String fileName = "筛选的数据.csv"; 
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	private static String baseUrl = "http://api.map.baidu.com/place/v2/search?ak=B2deebf8d1163f8a81d348ab329216d3&output=json&page_size=1&page_num=0&scope=1&region=北京&query=";
	public static void main(String[] args) throws IOException, JSONException {
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");  
		BufferedReader br = new BufferedReader(isr);
		FileOutputStream fos = new FileOutputStream("含坐标数据.csv");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
		BufferedWriter bw = new BufferedWriter(osw);
		
		String str;
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		int count = 0;
		while ( (str = br.readLine()) != null) {
			String[] strs = str.split(",");
			String shop = strs[1];
			String label = strs[2];
			String location = strs[3];
			float[] coords = queryToBaiduMap(location);
			for (int i = 0; i < strs.length; i++) {
				bw.write(strs[i] + ",");
			}
			bw.write(coords[0] + "," + coords[1] + "\n");
			System.out.println(count++);
		}
		bw.close();

	}
	
	private static float[] queryToBaiduMap(String location) throws IOException, JSONException {
		float []coords = new float[2];
//		System.out.println(location);
		String url = baseUrl + location; 
		Document document = Jsoup.connect(url).userAgent(userAgent).get();
		JSONObject json = new JSONObject(document.text());
		JSONArray results = json.getJSONArray("results");
		if (results.length() == 0) {
			System.out.println("00000000000");
			coords[0] = 0;
			coords[1] = 0;
		} else {
			JSONObject result = results.optJSONObject(0).getJSONObject("location");
			coords[0] = (float) result.getDouble("lng");
			coords[1] = (float) result.getDouble("lat");
		}
		return coords;
	}
}
