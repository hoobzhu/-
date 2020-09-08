package com.hoob.rs.sys.vo;

public class FileProviderVO {
	private String name ; //文件名
	private int type;   //0: 文件夹       1：文件
	private String path;  //路径
	private String url;	//url
	private String length;//文件大小

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	
}
