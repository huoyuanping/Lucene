package com.et.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.et.service.FoodService;

@Controller
public class FoodController {
	
	@Autowired
	FoodService service;
	@ResponseBody
	@RequestMapping("/queryFood")
	public List<String> serch(String foodname){
	  return service.search(foodname);
		
	}
	
	@ResponseBody
	@RequestMapping("/loadData")
	public void write(){
		service.write();
		
	}
}
