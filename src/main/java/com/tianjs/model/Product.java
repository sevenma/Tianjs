package com.tianjs.model;

public class Product {

	 private Integer id; 

	 private String proName; 

	 private String proCode; 


	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}

	public void setProName(String proName){
		this.proName = proName;
	}

	public String getProName(){
		return this.proName;
	}

	public void setProCode(String proCode){
		this.proCode = proCode;
	}

	public String getProCode(){
		return this.proCode;
	}

}
