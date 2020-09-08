package com.hoob.rs.constants;

public class Constants {

	public static final int PROTOCOL_TYPE_IPTV = 1;
	public static final int PROTOCOL_TYPE_HPD = 2;
	public static final int PROTOCOL_TYPE_ISMA = 4;
	public static final int PROTOCOL_TYPE_HLS = 8;

	public static final String MEDIA_FORMATS_M3U8 = ".m3u8";
	public static final String MEDIA_FORMATS_TS = ".ts";
	public static final String MEDIA_FORMATS_MP4 = ".mp4";

	public static final String RATECONTROLMODEL_VBR = "VBR";
	public static final String RATECONTROLMODEL_CBR = "CBR";

	public static final String SP_CONTENTID = "SP_CATEGORY";

	public static final String VODTYPE_MOVIE = "1000";
	
	/**
	 * 百视通code前缀，用于匹配这类code，根据规则统一转换
	 */
	public static final String BESTV_PREF = "Umai,umai";
}
