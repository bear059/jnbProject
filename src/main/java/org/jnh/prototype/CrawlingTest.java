package org.jnh.prototype;

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
		System.out.println("Hello World!!");
		try {
			//crawling();
			Response response = connectDocument(centralUrl, "0");
			int totalNum = findTotalPageNum (response);
			System.out.println(totalNum);
			
			for (int i = 0; i< totalNum; i=i+10) {
				crawlingCenterList(centralUrl, Integer.toString(i));
			}
			//crawlingPost("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void crawlingCenterList(String url, String offset) throws Exception{			
		Response response = connectDocument(url, offset);
		// parse the document from response
		Document document = response.parse();
		Elements es = document.getElementsByClass("com_list1");
		Elements list = null;
		
		//System.out.println(document);
		System.out.println("#############################################추가 합니다. 추가로 삽입 진행 합니다.");
		int beginNum = Integer.parseInt(offset);
		
		for (Element e: es) {
			//System.out.println(e);
			list = e.getElementsByAttribute("target");
			for (Element ce: list) {
				String centerName = ce.html();
				if (ce.hasAttr("href") && !centerName.contains("img")) {
					System.out.print(beginNum+"/"+centerName+"\t/\t");						
					System.out.println(ce.attr("href"));
					beginNum++;
				}
				
			}
		}
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
