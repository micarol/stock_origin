

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class StockProcessor implements PageProcessor {
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	public void process(Page page) {
		StringBuffer sb = new StringBuffer();
		List<String> list = page.getHtml().css("div.snbox").css("ul").css("li").css("span").$("a").all();
		System.out.println(list.size());
		sb.append("<html>");
		for (String string : list) {
			if(string.contains("业绩")){
				sb.append("<text style='color:red'>业绩</text>");
			} else {
				sb.append("<text>普通</text>");
			}
			sb.append(" | ");
			sb.append(string);
			sb.append("</br>");
			sb.append("\n");
		}
		sb.append("</html>");
//		String str = new String(page.getRawText());
//		System.out.println(str);
		
		try {
			FileOutputStream out = new FileOutputStream(new File("/Users/micarol/Documents/spider/test/sz002292"));
			byte[] bs = sb.toString().getBytes("UTF-8");
			out.write(bs);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
//		System.out.println(new String("\u4e16\u8363\u5146\u4e1a\uff1a\u7b2c\u516d\u5c4a\u8463\u4e8b\u4f1a\u7b2c\u4e03\u6b21\u4f1a\u8bae\u51b3\u8bae\u516c\u544a"));
//		System.out.println(Spider.create(new StockProcessor()).get("http://data.eastmoney.com/notice/002292.html"));
		//qq,http://news.gtimg.cn/notice_more.php?q=sz002292&page=1
	}
}
