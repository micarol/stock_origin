package com.micarol.stock.util.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.micarol.stock.pojo.StockPubNotice;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.StringUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class EastMoneySpider implements PageProcessor {
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	public Site getSite() {
		return site;
	}
	
	public void process(Page page) {}
	
	
	/**
	 * 
	 * @param url
	 * @param fromDate
	 * @return
	 */
	public List<StockPubNotice> getPubNoticeList(String url, String code, int fromDate) {
		Page page = this.getPage(url);
		if (page == null) {
			Loggers.RUNNING_LOG.error("{} fetch error", url);
		} else {
			List<StockPubNotice> list = new ArrayList<>();
			Selectable ele = page.getHtml().css("div.snbox").css("ul").css("li");
			List<Selectable> nodes = ele.nodes();
			for (Selectable selectable : nodes) {
				String pubDate = selectable.$("span.date", "text").get();
				int d = Integer.parseInt(pubDate.replace("-", ""));
				if(d >= fromDate) {
					StockPubNotice notice = new StockPubNotice();
					String title = selectable.$("a", "text").get();
					notice.setUniKey(StringUtil.getMD5(code+title+pubDate));
					notice.setCode(code);
					notice.setDate(pubDate);
					notice.setTitle(title);
					notice.setLink(selectable.links().get());
					list.add(notice);
				}
			}
			return list;
		}
		return null;
	}
	
	public Page getPage(String url) {
		Request request = new Request(url);
		Downloader downloader = new HttpClientDownloader();
		Page page = downloader.download(request, Spider.create(new EastMoneySpider()).addUrl(url));
		return page;
	}

	
	public static void main(String[] args) {
//		System.out.println(new String("\u4e16\u8363\u5146\u4e1a\uff1a\u7b2c\u516d\u5c4a\u8463\u4e8b\u4f1a\u7b2c\u4e03\u6b21\u4f1a\u8bae\u51b3\u8bae\u516c\u544a"));
//		EastMoneySpider spider = new EastMoneySpider();
//		List<StockPubNotice> list = spider.getPubNoticeList("http://data.eastmoney.com/notice/002292.html", "002292", 20160729);
//		System.out.println(list!=null?list.toString():"");
		//qq,http://news.gtimg.cn/notice_more.php?q=sz002292&page=1
	}
}
