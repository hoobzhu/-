package com.hoob.rs.utils;

/**
 * apk文件中的包名、版本号等信息
 * 
 * 从apps复制而来
 */
public class ApkInfo{
	private String packageName;
	private String versionName;
	private Long versionCode;
	private double size;
	private String requires;

	public ApkInfo() {
		
	}
	public ApkInfo(String packageName, String versionName, Long versionCode) {
		this.packageName = packageName;
		this.versionName = versionName;
		this.versionCode = versionCode;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Long versionCode) {
		this.versionCode = versionCode;
	}

	@Override
    public String toString() {
		return "ApkInfo [packageName=" + this.packageName + ", versionName="
				+ this.versionName + ", versionCode=" + this.versionCode + "]";
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String requires) {
		this.requires = requires;
	}
}