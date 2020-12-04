package com.micarol.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;


public class Test {

	public static void main(String[] args) throws Exception {
		fileHandle();
	}
	
	public static void fileHandle() throws Exception {
		String path = "/Users/micarol/Documents/tmp/juzi_data/t";
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		String line = reader.readLine();
		while (null!=line && !"".equals(line)) {
			strHandle(line);
			line = reader.readLine();
		}
	}
	
	public static void strHandle(String line) throws Exception {
		StringBuilder sbuf = new StringBuilder();
		JsonNode data = JsonUtil.readTree(line);
		JsonNode list = data.get("comments");
		Iterator<JsonNode> it = list.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss Z yyyy",Locale.US);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int c = 0;
		while(it.hasNext()) {
			c++;
			JsonNode o = it.next();
			if(null != o.get("user")) {
				JsonNode u = o.get("user");
				sbuf.append(u.get("id").asLong()).append("\t");
				sbuf.append(StringUtil.getMD5(u.get("id").asText())).append("\t");
				sbuf.append(u.get("screen_name").asText()).append("\t");
				sbuf.append(u.get("province").asLong()).append("\t");
				sbuf.append(u.get("city").asLong()).append("\t");
				sbuf.append(u.get("location").asText()).append("\t");
				sbuf.append(u.get("description").asText()).append("\t");
				sbuf.append(u.get("gender").asText()).append("\t");
				sbuf.append(u.get("followers_count").asLong()).append("\t");
				sbuf.append(u.get("friends_count").asLong()).append("\t");
				sbuf.append(u.get("statuses_count").asLong()).append("\t");
				sbuf.append(u.get("favourites_count").asLong()).append("\t");
				sbuf.append(sdf1.format(sdf.parse(u.get("created_at").asText()))).append("\t");
				sbuf.append(u.get("verified").asBoolean()).append("\t");
				sbuf.append(u.get("verified_type").asText()).append("\t");
				sbuf.append(u.get("urank").asLong()).append("\t");
				sbuf.append(u.get("user_ability").asLong()).append("\t");
				sbuf.append("\n");
			}
			
		}
		writeFile("/Users/micarol/Documents/tmp/juzi_data/u_data", sbuf);
		System.out.println("total:"+c);
	}
	
	public static void writeFile(String outPath, StringBuilder sbuffer) {
    	String dirPath = outPath.substring(0, outPath.lastIndexOf('/'));
        File dir = new File(dirPath);
    	if(!dir.isDirectory()) {
    		dir.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(outPath)) {
            byte[] bs = sbuffer.toString().getBytes("UTF-8");
            out.write(bs);
            out.flush();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
