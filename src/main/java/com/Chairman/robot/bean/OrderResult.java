package com.Chairman.robot.bean;

import java.util.List;

public class OrderResult {
	private int code;
	private String text;
	private String url;
	private List<OrderList> list;
	public OrderResult(int resultCode, String msg,String url,List<OrderList> list)
	{
		this.code = resultCode;
		this.text = msg;
		this.url = url;
		this.list = list;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<OrderList> getList() {
		return list;
	}
	public void setList(List<OrderList> list) {
		this.list = list;
	}
}
