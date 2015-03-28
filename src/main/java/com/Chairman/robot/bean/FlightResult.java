package com.Chairman.robot.bean;

import java.util.List;

public class FlightResult {
	private int code;
	private String text;
	private String url;
	private List<FlightList> list;
	public FlightResult(int resultCode, String msg,String url, List<FlightList> list)
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
	public  List<FlightList> getList() {
		return list;
	}
	public void setList( List<FlightList> list) {
		this.list = list;
	}
}
