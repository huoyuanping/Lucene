package com.et.dao;

import java.util.List;
import java.util.Map;

public interface FoodDao {
	/**
	 *获取数据库中的数据
	 * @param foodname
	 * @return
	 */
	public List<Map<String, Object>> getFood();
	/**
	 * 创建索引
	 */
	public void write();
	/**
	 * 搜索索引
	 */
	public  List<String> search(String foodname);
}
