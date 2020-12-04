package com.micarol.stock.service.cache;

import java.util.Map;

import javax.annotation.PreDestroy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class LocalCache {
	
	private static final Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(102400).build();
	
	@PreDestroy
	public void detroy() {
		cache.cleanUp();
	}

	public static void putValue(final String key, final Object value) {
		cache.put(key, value);
	}
	
	/**
	 * 从本地缓存中获取key对应的值，如果该值不存则则返回null
	 *
	 * @param key
	 * @return
	 */
	public static Object getValue(final String key) {
		return cache.getIfPresent(key);
	}

	public static void clearcache(final String key) {
		cache.invalidate(key);
	}

	public static Map<String, Object> getAll() {
		return cache.asMap();
	}
}
