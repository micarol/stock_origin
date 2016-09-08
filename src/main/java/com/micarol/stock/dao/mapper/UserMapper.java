package com.micarol.stock.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.micarol.stock.pojo.User;

public interface UserMapper {

	public User userByName(@Param("name") String name);
}
