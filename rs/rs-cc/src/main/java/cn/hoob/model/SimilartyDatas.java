package cn.hoob.model;

import java.io.Serializable;
import java.util.List;

public class SimilartyDatas implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4493086664391697502L;
	private   String  contentId;//合集
    private   List<SimilartyData> similartyDatas;//合集魔某性相似的结构体

   
    public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public List<SimilartyData> getSimilartyDatas() {
        return similartyDatas;
    }

    public void setSimilartyDatas(List<SimilartyData> similartyDatas) {
        this.similartyDatas = similartyDatas;
    }

	@Override
	public String toString() {
		return "SimilartyDatas [contentId=" + contentId + ", similartyDatas="
				+ similartyDatas + "]";
	}

  
}
