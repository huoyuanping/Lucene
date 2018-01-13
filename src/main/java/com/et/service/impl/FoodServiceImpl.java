package com.et.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.et.dao.FoodDao;
import com.et.service.FoodService;
@Service
public class FoodServiceImpl implements FoodService{
	@Autowired
	FoodDao dao;
	public List<Map<String, Object>> getFood() {
		
		return dao.getFood();
	}
	@Override
	public void write() {
		dao.write();
		
	}
	@Override
	public List<String> search(String foodname) {
		return dao.search(foodname);
	}

}
