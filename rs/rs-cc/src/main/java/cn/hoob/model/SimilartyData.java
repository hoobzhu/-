package cn.hoob.model;

import java.io.Serializable;

public class SimilartyData implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1626214490167342962L;
	private String contentId;
    private String similarContentId;
    private String similarContentIds;//内容Id
    private Double similarty;

	public String getSimilarContentId() {
		return similarContentId;
	}


	public void setSimilarContentId(String similarContentId) {
		this.similarContentId = similarContentId;
	}


	public String getContentId() {
		return contentId;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}


	public String getSimilarContentIds() {
		return similarContentIds;
	}


	public void setSimilarContentIds(String similarContentIds) {
		this.similarContentIds = similarContentIds;
	}

	public Double getSimilarty() {
		return similarty;
	}


	public void setSimilarty(Double similarty) {
		this.similarty = similarty;
	}


	@Override
	public String toString() {
		return "SimilartyData [contentId=" + contentId + ", similarContentId="
				+ similarContentId + ", similarContentIds=" + similarContentIds
				+ ", similarty=" + similarty + "]";
	}


	/*@Override
    public int compareTo(Object o) {
        if (o instanceof SimilartyData) {
            SimilartyData goods = (SimilartyData) o;
            if (this.similarty > goods.getSimilarty()) {
                return 1;
            } else if (this.similarty < goods.getSimilarty()) {
                return -1;
            } else {
                return 0;

            }
        }
        return 0;
    }*/

}
