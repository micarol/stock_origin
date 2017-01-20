package com.micarol.stock.service.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.micarol.stock.util.Loggers;

public class LocalCache {
public static Map<String, Long> updateMap = new HashMap<String, Long>();
	
	// 默认的缓存容量
	private static int DEFAULT_CAPACITY = 1024;
	// 最大容量
	private static int MAX_CAPACITY = 102400;
	// 刷新缓存的频率, 单位秒
	private static int MONITOR_DURATION = 60;
	// 启动监控线程
	
	static {
		new Thread(new TimeoutTimerThread()).start();
	}
	// 使用默认容量创建一个Map
	private static ConcurrentHashMap<String, CacheEntity> cache = new ConcurrentHashMap<String, CacheEntity>(DEFAULT_CAPACITY);

	/**
	 * 将key-value 保存到本地缓存并制定该缓存的过期时间
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            过期时间，如果是-1 则表示永不过期
	 * @return
	 */
	public static boolean putValue(String key, Object value, int expireTime) {
		return putCloneValue(key, value, expireTime);
	}

	/**
	 * 将值通过序列化clone 处理后保存到缓存中，可以解决值引用的问题
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	private static boolean putCloneValue(String key, Object value, int expireTime) {
		try {
			if (cache.size() >= MAX_CAPACITY) {
				Loggers.ERROR_LOG.error("cache map has reached max size: {}", MAX_CAPACITY);
				return false;
			}
			// 序列化赋值
			CacheEntity entityClone = clone(new CacheEntity(value, System.currentTimeMillis(), expireTime));
			cache.put(key, entityClone);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 序列化 克隆处理
	 * 
	 * @param object
	 * @return
	 */
	private static <T extends Serializable> T clone(T object) {
		T cloneObject = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			cloneObject = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cloneObject;
	}

	/**
	 * 从本地缓存中获取key对应的值，如果该值不存则则返回null
	 * 
	 * @param key
	 * @return
	 */
	public static Object getValue(String key) {
		if(cache.containsKey(key)) {
			return cache.get(key).getValue();
		}
		return null;
	}

	/**
	 * 清空所有
	 */
	public static void clear() {
		cache.clear();
	}

	public static void clearCache(String key) {
		if(cache.containsKey(key)) {
			cache.remove(key);
			Loggers.RUNNING_LOG.info("clear cache by key: {}", key);
		} else {
			Loggers.ERROR_LOG.error("clear cache but cache not found, key: {}", key);
		}
	}
	
	public static ConcurrentHashMap<String, CacheEntity> getAll() {
		return cache;
	}



	/**
	 * 过期处理线程
	 */
	static class TimeoutTimerThread implements Runnable {
		public void run() {
			while (true) {
				try {
					TimeUnit.SECONDS.sleep(MONITOR_DURATION);
					Loggers.RUNNING_LOG.info("cache monitor start, map size:{}", cache.size());
					checkTime();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 过期缓存的具体处理方法
		 * @throws Exception
		 */
		private void checkTime() throws Exception {
			Set<String> keys = cache.keySet();
			for (String key : keys) {
				CacheEntity tce = cache.get(key);
				if ((tce.getExpire() > (System.currentTimeMillis()-tce.getCreateAt())/1000) || (tce.getExpire() == -1)) {
					continue;
				}
				// 清除过期缓存和删除对应的缓存队列
				clearCache(key);
			}
		}
	}
}
