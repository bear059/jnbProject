package org.jnh.prototype;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlingTest {
	private final String centralUrl = "http://central.childcare.go.kr/ccef/sitelink/SiteLinkCenterSlPL.jsp";
	public CrawlingTest() {
		try {
			Response response = connectDocument(centralUrl, "0");
			int totalNum = findTotalPageNum (response);
			System.out.println(totalNum);
			ManagementCSVFile rck = new ManagementCSVFile();
			// 1. 육아종합지원센터 URL 모으기
			List<List<String>> centerList = null;
			Map<String, String> centerMap = null;
			if (!rck.isExist()) {
				// CSV 파일이 없는 경우 
				// 1) take URL from Crawling site
				// 2) Generate URL List CSV file
				//centerList = crawlingTargetUrl(totalNum);
				//rck.generateCSV(centerList);
				centerMap = crawlingTargetUrlMap(totalNum);
				rck.generateCSVByMap(centerMap);
			} else {
				// CSV 파일 있는 경우 
				// 1) take URL list from CSV file
				//centerList = rck.readCSVForCenters();
				centerMap = rck.readCSVForCentersByMap();
			}
			
			List<List<String>> targetGroup = rck.readCSV();
			//System.out.println(result.toString());
			gatherContentForTarget(centerMap, targetGroup);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void gatherContentForTarget(Map<String, String> centerMap, List<List<String>> targetGroup) {
		for (List<String> targetList: targetGroup) {
			for (String target: targetList) {
				System.out.print(target+"/"+centerMap.containsKey(target));
				System.out.print("("+centerMap.get(target)+")\t");
			}
			System.out.println();
		}
	}
	
	private List<List<String>> crawlingTargetUrl(int totalNum) {
		List<List<String>> centerList = new ArrayList<List<String>>();
		try {
			//crawling();
			for (int i = 0; i< totalNum; i=i+10) {
				centerList.addAll(crawlingCenterList(centralUrl, Integer.toString(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return centerList;
	}
	
	
	private List<List<String>> crawlingCenterList(String url, String offset) throws Exception{	
		List<List<String>> centerList = new ArrayList<List<String>>();
		
		Response response = connectDocument(url, offset);
		// parse the document from response
		Document document = response.parse();
		Elements es = document.getElementsByClass("com_list1");
		Elements list = null;
		
		List<String> centerInfo = null;
		int beginNum = Integer.parseInt(offset);
		
		for (Element e: es) {
			list = e.getElementsByAttribute("target");
			for (Element ce: list) {
				String centerName = ce.html();
				if (ce.hasAttr("href") && !centerName.contains("img")) {
					centerInfo = new ArrayList<String>();
					centerInfo.add(centerName.replaceAll(" ", ""));
					centerInfo.add(ce.attr("href"));
					//System.out.print(beginNum+"/"+centerName+"\t/\t");						
					//System.out.println(ce.attr("href"));
					beginNum++;
					centerList.add(centerInfo);
				}
			}
		}
		
		return centerList;
	}
	
	private Map<String, String> crawlingTargetUrlMap(int totalNum) {
		Map<String, String> centerMap = new LinkedHashMap<String, String>();
		try {
			//crawling();
			for (int i = 0; i< totalNum; i=i+10) {
				centerMap.putAll(crawlingCenterURLMap(centralUrl, Integer.toString(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return centerMap;
	}
	
	private Map<String, String> crawlingCenterURLMap(String url, String offset) throws Exception{	
		Map<String, String> centerUrlMap = new LinkedHashMap<String, String>();
		
		Response response = connectDocument(url, offset);
		// parse the document from response
		Document document = response.parse();
		Elements es = document.getElementsByClass("com_list1");
		Elements list = null;
		
		int beginNum = Integer.parseInt(offset);
		
		for (Element e: es) {
			list = e.getElementsByAttribute("target");
			for (Element ce: list) {
				String centerName = ce.html();
				if (ce.hasAttr("href") && !centerName.contains("img")) {
					centerUrlMap.put(centerName.replaceAll(" ", ""), ce.attr("href"));
				}
			}
		}
		
		return centerUrlMap;
	}


	
	private int findTotalPageNum(Response response) throws Exception {
		Document doc = response.parse();
		Elements total = doc.getElementsByClass("sum");
		int totalNum = 0;
		
		for (Element t: total) {
			Elements em = t.getElementsByTag("em");
			totalNum = Integer.parseInt(em.html());
		}
		
		return totalNum;
	}
	
	private Response connectDocument(String url, String offset) throws Exception {
		Response response = Jsoup.connect(url)
				.userAgent("Mozilla/5.0")
				.timeout(10 * 1000)
				.method(Method.POST)
				.data("flag", "SlPL")
				.data("schRELATESITEGBCODE", "98")
				.data("SURVEYSEQ", "")
				.data("userS1", "")
				.data("SYSCODE", "")
				.data("offset", offset)
				.followRedirects(true)
				.execute();

		// get cookies
		Map<String, String> mapCookies = response.cookies();
		
		return response;
	}

	private void crawling() throws Exception {
		final String inflearnUrl = "http://central.childcare.go.kr/ccef/sitelink/SiteLinkCenterSlPL.jsp";
		Connection conn = Jsoup.connect(inflearnUrl);
		Document document = conn.get();

		System.out.println(document);
	}

	public static void main(String[] args) throws Exception {
		CrawlingTest c = new CrawlingTest();
	}
}
