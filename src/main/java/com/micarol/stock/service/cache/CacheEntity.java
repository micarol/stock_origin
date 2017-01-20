package com.micarol.stock.service.cache;

import java.io.Serializable;

/**
 * 本地缓存保存的实体
 */
public class CacheEntity implements Serializable {
	
	private static final long serialVersionUID = -712677047614740438L;

	/**
	 * 值
	 */
	private Object value;

	/**
	 * 保存的时间戳
	 */
	private long createAt;

	/**
	 * 过期时间
	 */
	private int expire;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public CacheEntity(Object value, long createAt, int expire) {
		super();
		this.value = value;
		this.createAt = createAt;
		this.expire = expire;
	}

}
