package com.micarol.stock.util.spider;

import java.util.ArrayList;
import java.util.List;

import com.micarol.stock.util.Loggers;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class XiciDaiLiSpider implements PageProcessor {
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	private final static String XICI_HTTPS_URL = "https://www.xicidaili.com/wn/"; 
	
	public void httpsProxyFetch() {
		
	}

	@Override
	public void process(Page page) {
		
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @param fromDate
	 * @return
	 */
	public List<String> getPageData(String url, int pageIndex) {
		Page page = this.getPage(url);
		if (page == null) {
			Loggers.RUNNING_LOG.error("{} fetch error", url);
		} else {
			List<String> list = new ArrayList<>();
			Selectable ele = page.getHtml().xpath("//table[@id='ip_list']").xpath("//tr[]");
			List<Selectable> nodes = ele.nodes();
			for (Selectable selectable : nodes) {
				String pubDate = selectable.$("span.date", "text").get();
				int d = Integer.parseInt(pubDate.replace("-", ""));
				
			}
			return list;
		}
		return null;
	}
	
	public Page getPage(String url) {
		Request request = new Request(url);
		Downloader downloader = new HttpClientDownloader();
		Page page = downloader.download(request, Spider.create(new XiciDaiLiSpider()).addUrl(url));
		return page;
	}

	
	public static void main(String[] args) {
		XiciDaiLiSpider spider = new XiciDaiLiSpider();
		spider.getPageData(XICI_HTTPS_URL, 1);
	}
}
