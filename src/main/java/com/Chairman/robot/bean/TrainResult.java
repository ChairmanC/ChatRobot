package com.Chairman.robot.bean;

import java.util.List;

public class TrainResult {
	private int code;
	private String text;
	private String url;
	private List<TrainList> list;
	public TrainResult(int resultCode, String msg,String url,List<TrainList> list)
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
	public List<TrainList> getList() {
		return list;
	}
	public void setList(List<TrainList> list) {
		this.list = list;
	}
}
