package org.jnh.prototype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManagementCSVFile {
	public ManagementCSVFile() {
		
	}
	
	public List<List<String>> readCSV() {
		List<List<String>> csvList = new ArrayList<List<String>>();
        File csv = new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/testCheckList.csv");
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                List<String> aLine = new ArrayList<String>();
                String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) { 
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        return csvList;
	}
	
	public List<List<String>> readCSVForCenters() {
		List<List<String>> csvList = new ArrayList<List<String>>();
        File csv = new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/centerList.csv");
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                List<String> aLine = new ArrayList<String>();
                String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) { 
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        return csvList;
	}
	
	public Map<String, String> readCSVForCentersByMap() throws Exception {
		Map<String, String> csvMap = new LinkedHashMap<String, String>();
        File csv = new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/centerList.csv");
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(csv));
        
        try {        	
        	while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
        		String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
        		csvMap.put(lineArr[0].trim(), lineArr[1].trim());
        	}
        } finally {        	
        	br.close();
        }
        
        
        return csvMap;
	}
	
	public String generateCSV (List<List<String>> strList) throws Exception{
		PrintWriter writer = new PrintWriter(new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/centerList.csv"));

        StringBuilder sb = new StringBuilder();
        for (List<String> row: strList) {
        	int columnSize = row.size()-1;
        	int idx = 0;
        	for (String column: row) {
        		sb.append(column);
        		if (idx != columnSize ) 
        			sb.append(",");
        		idx++;
        	}
        	sb.append("\n");
        	
        }

        writer.write(sb.toString());
        writer.close();
        System.out.println("done!");
        
        return sb.toString();
	}
	
	public String generateCSVByMap (Map<String, String> strMap) throws Exception{
		PrintWriter writer = new PrintWriter(new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/centerList.csv"));
		Iterator<String> urlKey = strMap.keySet().iterator();
		
        StringBuilder sb = new StringBuilder();
        
        while (urlKey.hasNext()) {
        	String key = urlKey.next();
        	
        	sb.append(key);
        	sb.append(",");
        	sb.append(strMap.get(key));
        	sb.append("\n");
        }

        writer.write(sb.toString());
        writer.close();
        System.out.println("done!");
        
        return sb.toString();
	}
	
	public boolean isExist() throws Exception {
		File file = new File("/Users/bear059/Documents/DevSources/2.JnHLab_Projects/keyanalysis/centerList.csv");
		return file.exists();
	}
}
