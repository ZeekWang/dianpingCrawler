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
//		112, //????????????
//		311, //?????????
//		116, //??????
//		113, //??????
//		102, //??????
//		110, //??????
//		111, //?????????
//		114, //????????????
//		101, //?????????
//		103, //??????
//		118, //??????
//		108, //?????????
//		104, //??????
//		117, //????????????
//		115, //????????????
//		248, //?????????
//		26483, //??????
//		106, //?????????
//		26481, //?????????
//		251, //??????
//		109, //??????
		105, //?????????
		246, //?????????
		3243 //?????????
	};
	
	private static String[] typeTextList = {
//		"????????????",
//		"?????????", 
//		"??????",
//		"??????",
//		"??????",
//		"??????",
//		"?????????",
//		"????????????",
//		"?????????",
//		"??????",
//		"??????",
//		"?????????",
//		"??????",
//		"????????????",
//		"????????????",
//		"?????????",
//		"??????",
//		"?????????",
//		"?????????",
//		"??????",
//		"??????",
		"?????????",
		"?????????",
		"?????????"
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
				
				String address = restaurantNode.select(".address").text().replace("??????:", "").replaceAll(" ????", " ");
				String tags = restaurantNode.select(".tags").text().replace("??????:", "").replaceAll(" ????", " ");
				String features = restaurantNode.select(".features").text().replace("??????: ", "").replaceAll("???? ", " ");
				String price = restaurantNode.select(".average").text().replace("??", "");
				String[] grades = restaurantNode.select(".grade").text().split(" ");
				if (grades.length != 3)
					continue;
				String grade1 = grades[0];
				String grade2 = grades[1];
				String grade3 = grades[2];
				String remarkCount = restaurantNode.select(".remark > li").get(2).text().replace("?????????", "");
				dataWriter.write(
					name + "," + href + "," + typeText + "," + address + "," + tags + "," + features + "," + price + "," 
							+ grade1 + "," + grade2 + "," + grade3 + "," + remarkCount + "\n"	
						);
				++restrauntCount;
				
			}
			System.out.println("????????????"+ restrauntCount);
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
