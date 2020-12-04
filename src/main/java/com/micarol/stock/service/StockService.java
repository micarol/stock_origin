package com.micarol.stock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micarol.stock.constants.Constants;
import com.micarol.stock.dao.mapper.StockMapper;
import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.pojo.StockAlarmRec;
import com.micarol.stock.pojo.StockAlarmSetting;
import com.micarol.stock.pojo.StockPubNotice;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.service.rabbitmq.RabbitMQService;
import com.micarol.stock.util.DateUtile;
import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.StringUtil;
import com.micarol.stock.util.spider.EastMoneySpider;

@Service
public class StockService {

	@Autowired
	private StockMapper stockMapper;
	@Autowired
	private RabbitMQService queueService;
	@Autowired
	private Queue stockAlarmQueue;
	@Autowired
	private Queue stockMailQueue;

	private final static String NOTICE_URL = "http://data.eastmoney.com/notice/{STOCK_CODE}.html";

	/**
	 * 根据股票代码与最新公告日期爬公告数据, 并检测是否符合监控条件, 如果符合条件则丢入监控处理队列
	 */
	public void pubNoticeSpider() {
		List<Map<String, Object>> codeMap = stockMapper.allCodesForPubNotice();
		EastMoneySpider spider = new EastMoneySpider();
		List<StockPubNotice> tempList = new ArrayList<>();
		Map<String, List<StockPubNotice>> alarmeMap = null;
		int rs = 0;
		List<StockAlarmSetting> settings = this.listSettings();
		for (Map<String, Object> map : codeMap) {
			Loggers.RUNNING_LOG.info("code:{}, date:{}", map.get("code"), map.get("date"));
			String url = NOTICE_URL.replace("{STOCK_CODE}", String.valueOf(map.get("code")));
			List<StockPubNotice> list = spider.getPubNoticeList(url, String.valueOf(map.get("code")),
					DateUtile.dateStrToInt(String.valueOf(map.get("date"))));
			if (null != list) {
				tempList.addAll(list);
			}
			if (tempList.size() >= 1000) {
				rs += stockMapper.insertUpdatePubNotice(tempList);
				this.dataFilter(settings, tempList);
				tempList.clear();
			}
		}
		if (tempList.size() > 0) {
			rs += stockMapper.insertUpdatePubNotice(tempList);
			this.dataFilter(settings, tempList);
		}
		Loggers.RUNNING_LOG.info("pubNoticeSpider effect rows:{}", rs);
	}

	public StockAlarmSetting addStockAlarmSetting(StockAlarmSetting setting) {
		if (stockMapper.addStockAlarmSetting(setting) > 0) {
			return setting;
		}
		return null;
	}

	@Transactional
	public int backupStockAlarmSetting(int id) {
		if (stockMapper.backupStockAlarmSetting(id) > 0) {
			return this.backupStockAlarmSetting(id);
		}
		return 0;
	}

	public int delStockAlarmSetting(int userid, int id) {
		return stockMapper.delStockAlarmSetting(userid, id);
	}

	public List<StockAlarmSetting> listSettings() {
		return stockMapper.listSettings();
	}

	/**
	 * 检测是否符合监控
	 * @param settings
	 * @param list
	 */
	private void dataFilter(List<StockAlarmSetting> settings, List<StockPubNotice> list) {
		boolean start = false;
		boolean end = false;
		boolean match = false;
		o: for (StockPubNotice notice : list) {
			start = false;
			end = false;
			match = false;
			// 数据太少, 不考虑性能, 直接遍历匹配
			i: for (StockAlarmSetting setting : settings) {
				if (end)
					continue o;
				if (start) {
					if (!setting.getCode().equals(notice.getCode())) {
						end = true;
					} else {
						match = this.keywordMatch(setting.getKeyword(), notice.getTitle());
					}
				} else {
					if (setting.getCode().equals(notice.getCode())) {
						start = true;
						match = this.keywordMatch(setting.getKeyword(), notice.getTitle());
					}
				}
				if (match) {
					queueService.putMessage(stockAlarmQueue, setting.toString() + Constants.SPLIT_NOTIFY+ notice.toString());
				}
			}
		}
	}

	private boolean keywordMatch(String keyword, String text) {
		if (text.indexOf(keyword) > -1) {
			return true;
		}
		return false;
	}

	/**
	 * 处理符合监控设置的公告信息
	 */
	public void alarmHander() {
		String message = queueService.getMessage(stockAlarmQueue);
		int count = 0;
		int limit = 100;
		Map<StockAlarmSetting, List<StockPubNotice>> map = new HashMap<>();
		List<StockPubNotice> list;
		while (StringUtils.isNotEmpty(message) && count < limit) {
			count++;
			try {
				String[] arr = message.split(Constants.SPLIT_NOTIFY);
				StockAlarmSetting setting = JsonUtil.jsonStr2Obj(arr[0], StockAlarmSetting.class);
				StockPubNotice notice = JsonUtil.jsonStr2Obj(arr[1], StockPubNotice.class);
				if (map.containsKey(arr[0])) {
					map.get(setting).add(notice);
				} else {
					list = new ArrayList<>();
					list.add(notice);
					map.put(setting, list);
				}
				message = queueService.getMessage(stockAlarmQueue);
			} catch (Exception e) {
				Loggers.ERROR_LOG.error("err str: {}", message);
			}
		}

		if (map.size() > 0) {
			List<StockAlarmRec> recList = new ArrayList<>();
			StockAlarmRec rec = null;
			Set<Entry<StockAlarmSetting, List<StockPubNotice>>> enties = map.entrySet();
			for (Entry<StockAlarmSetting, List<StockPubNotice>> entry : enties) {
				List<StockPubNotice> notices = entry.getValue();
				StockAlarmSetting setting = entry.getKey();
				for (StockPubNotice notice : notices) {
					Object o = LocalCache.getValue(CacheConstants.CACHE_MAIL+notice.getUniKey());
					if(setting.getNotice()==1 && null==o) {// TODO: @micarol 去除重复提醒
						rec = new StockAlarmRec();
						rec.setAlarmid(setting.getId());
						rec.setCode(setting.getCode());
						rec.setKeyword(setting.getKeyword());
						rec.setUniKey(notice.getUniKey());
						recList.add(rec);
						queueService.putMessage(stockMailQueue, getMailNotice(setting, notice));
					} 
				}
			}
			if(recList.size() > 0) {
				Loggers.RUNNING_LOG.info("insert rec siez:{}", this.insertAlarmRec(recList));
			}
		}
	}

	private String getMailNotice(StockAlarmSetting setting, StockPubNotice notice) {
		return setting.toString()+Constants.SPLIT_MAIL+notice.toString();
	}
	
	public int insertAlarmRec(List<StockAlarmRec> list) {
		int rs = 0;
		if (list.size() > 0) {
			rs = stockMapper.insertAlarmRec(list);
		}
		return rs;
	}
	
	public void testQueue() {
		Loggers.RUNNING_LOG.info("test queue start");
		queueService.putMessage("testQueue", "testqueue"+System.currentTimeMillis());
		Loggers.RUNNING_LOG.info("test queue end");
	}
	
	public static void main(final String... args) throws Exception {

//		ConnectionFactory cf = new CachingConnectionFactory();
//
//		// set up the queue, exchange, binding on the broker
//		RabbitAdmin admin = new RabbitAdmin(cf);
//		Queue queue = new Queue("test1.queue");
//		admin.declareQueue(queue);
//		TopicExchange exchange = new TopicExchange("myExchange");
//		admin.declareExchange(exchange);
//		admin.declareBinding(
//			BindingBuilder.bind(queue).to(exchange).with("foo.*"));
//
//		// set up the listener and container
//		SimpleMessageListenerContainer container =
//				new SimpleMessageListenerContainer(cf);
//		Object listener = new Object() {
//			public void handleMessage(String foo) {
//				System.out.println(foo);
//			}
//		};
//		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
//		container.setMessageListener(adapter);
//		container.setQueueNames("myQueue");
//		container.start();
//
//		// send something
//		RabbitTemplate template = new RabbitTemplate(cf);
//		template.convertAndSend("myExchange", "foo.bar", "Hello, world!");
//		Thread.sleep(1000);
//		container.stop();
		
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
//		RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
		RabbitMQService service = ctx.getBean(RabbitMQService.class);
//		template.convertAndSend("test", "Hello, world!");
//		System.out.println("~~~~~~~~~1");
//		template.convertAndSend("stock.notice.keyword.alarm", "Hello, world!");
//		System.out.println("~~~~~~~~~2");
//		System.out.println("~~~~~~~~~3:"+template.convertSendAndReceive("test1", "Hello1, world!"));
//		System.out.println("~~~~~~~~~4:"+template.receiveAndConvert("test"));
//		System.out.println("~~~~~~~~~5:"+template.receiveAndConvert("test1"));
//		System.out.println("~~~~~~~~~6:"+template.receiveAndConvert("stock.notice.keyword.alarm"));
		//给队列发送消息
//        String routingKey = "queueOne";
//        template.convertAndSend(routingKey, "q1 msg");
//
//        routingKey = "queueTwo";
//        template.convertAndSend(routingKey, "q2 msg");
//
//        System.out.println("~~~~~~~~~3:"+template.convertSendAndReceive(routingKey	, "Hello1, world!"));
        
		ctx.destroy();
		
		
	}
}
