package com.et.entity;

public class Food {
	private int foodid;
	private String foodname;
	private int price;
	private String imagepath;
	public Food(){
		
	}
	public Food(int foodid, String foodname, int price, String imagepath) {
		super();
		this.foodid = foodid;
		this.foodname = foodname;
		this.price = price;
		this.imagepath = imagepath;
	}
	public int getFoodid() {
		return foodid;
	}
	public void setFoodid(int foodid) {
		this.foodid = foodid;
	}
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	
	
}
