package com.et.dao;

import java.util.List;
import java.util.Map;

public interface FoodDao {
	/**
	 *��ȡ���ݿ��е�����
	 * @param foodname
	 * @return
	 */
	public List<Map<String, Object>> getFood();
	/**
	 * ��������
	 */
	public void write();
	/**
	 * ��������
	 */
	public  List<String> search(String foodname);
}
