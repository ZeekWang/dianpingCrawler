package dianpingCrawler;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class Main {
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	private static FileWriter logWriter, dataWriter;
	private static boolean isAppend = true;
	private static int[] priceSections = {
		0,10,  10,20,  20,30,  30,40,  40,50,  50,60,  60,70,  70,80,  80,90,  90,100,  100,120,  120,140,
		140,160,  160,180,  180,200,  200,250,  250,300,  300,0  
	};
	private static int[] typeList = {
		112, //小吃快餐
		311, //北京菜
		116, //西餐
		113, //日本
		102, //川菜
		110, //火锅
		111, //自助餐
		114, //韩国料理
		101, //江浙菜
		103, //粤菜
		118, //其他
		108, //清真菜
		104, //湘菜
		117, //面包甜点
		115, //东南亚菜
		248, //云南菜
		26483, //鲁菜
		106, //东北菜
		26481, //西北菜
		251, //海鲜
		109, //素菜
		105, //贵州菜
		246, //湖北菜
		3243 //新疆菜
	};
	
	private static String[] typeTextList = {
		"小吃快餐",
		"北京菜", 
		"西餐",
		"日本",
		"川菜",
		"火锅",
		"自助餐",
		"韩国料理",
		"江浙菜",
		"粤菜",
		"其他",
		"清真菜",
		"湘菜",
		"面包甜点",
		"东南亚菜",
		"云南菜",
		"鲁菜",
		"东北菜",
		"西北菜",
		"海鲜",
		"素菜",
		"贵州菜",
		"湖北菜",
		"新疆菜"
	};
	
	private static int restrauntCount = 0;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		logWriter = new FileWriter("log.txt", isAppend);
		dataWriter = new FileWriter("data.csv", isAppend);
		for (int i = 0; i < typeList.length; i++) {
			for (int j = 0; j < priceSections.length; j+=2) {
				crawl(typeList[i], typeTextList[i], priceSections[j], priceSections[j+1]);
			}
		}
	}
	
	public static void crawl(int type, String typeText, int lowPrice, int highPrice) throws IOException{
		String baseUrl = "http://www.dianping.com/search/category/2/10/o10";
		baseUrl = baseUrl + "g" + type;
		if (lowPrice > 0)
			baseUrl = baseUrl + "x" + lowPrice;
		if (highPrice > 0) 
			baseUrl = baseUrl + "y" + highPrice;
		int page = 1;
		while (page >= 0) {
			String url = baseUrl + "p" + page;
			System.out.println(url);
			Document document;
			try {
				document = Jsoup.connect(url).userAgent(userAgent).get();
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				continue;
			}
			String totalCountStr = document.select(".searchNav > .guide .Color7").text().replace("(", "").replace(")", "");
			int totalCount = Integer.parseInt(totalCountStr);
			if (totalCount > 750 && highPrice - lowPrice > 1) {
				for (int i = lowPrice; i < highPrice; i++) {
					crawl(type, typeText, i, i+1);
				}
				break;
			}
			
			Elements restaurantNodes = document.select(".searchResult > dl > dd");
			for (int j = 0; j < restaurantNodes.size(); j++) {
				Element restaurantNode = restaurantNodes.get(j);
				String name = restaurantNode.select(".shopname").text();
				String href = restaurantNode.select(".shopname > a").attr("href");
				
				String address = restaurantNode.select(".address").text().replace("地址:", "").replaceAll("   ", " ");
				String tags = restaurantNode.select(".tags").text().replace("标签:", "").replaceAll("   ", " ");
				String features = restaurantNode.select(".features").text().replace("特色: ", "").replaceAll("   ", " ");
				String price = restaurantNode.select(".average").text().replace("¥", "");
				String[] grades = restaurantNode.select(".grade").text().split(" ");
				if (grades.length != 3)
					continue;
				String grade1 = grades[0];
				String grade2 = grades[1];
				String grade3 = grades[2];
				String remarkCount = restaurantNode.select(".remark > li").get(2).text().replace("封点评", "");
				dataWriter.write(
					name + "," + href + "," + typeText + "," + address + "," + tags + "," + features + "," + price + "," 
							+ grade1 + "," + grade2 + "," + grade3 + "," + remarkCount + "\n"	
						);
				++restrauntCount;
				
			}
			System.out.println("餐厅数："+ restrauntCount);
			logWriter.write(url + "\n");
			dataWriter.flush();
			logWriter.flush();
			Elements nextPageNode = document.select(".Pages .NextPage");
			
			if (nextPageNode.size() == 0) {
				page = -1;
			} else 
				page++;
			
		}
	}
}
