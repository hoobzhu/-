package cn.hoob.rs.model;

import java.io.Serializable;



/**
 * @author Hoob 2020年8月7日
 */

public class TopNContent implements Serializable {
	private static final long serialVersionUID = 1L;
	

    private String contentId;//内容Id
    private Integer playCount;//次数
	
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public TopNContent() {}
	public TopNContent(String contentId, Integer playCount) {
		super();
		this.contentId = contentId;
		this.playCount = playCount;
	}
	public static TopNContent parseModel(String str) {
	    String[] fields = str.split("\\|");
	    if (fields.length != 2) {
	      return null;
	    }
	    String contentId=fields[0];
	    Integer count=Integer.parseInt(fields[1]);
	    return new TopNContent(contentId,count);
	  }
}
