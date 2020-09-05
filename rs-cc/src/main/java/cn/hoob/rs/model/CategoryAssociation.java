package cn.hoob.rs.model;

import java.io.Serializable;

import org.apache.spark.sql.Row;

public class CategoryAssociation  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String objectContentId;//内容contentId
	private String categoryContentId;//栏目contentId
	private String categoryIdentityno;//栏目编号
	private Integer objectMediaType;//对象对应的媒体类型	


	public CategoryAssociation() {
		super();
	}


	public String getObjectContentId() {
		return objectContentId;
	}


	public void setObjectContentId(String objectContentId) {
		this.objectContentId = objectContentId;
	}


	public String getCategoryContentId() {
		return categoryContentId;
	}


	public void setCategoryContentId(String categoryContentId) {
		this.categoryContentId = categoryContentId;
	}


	public String getCategoryIdentityno() {
		return categoryIdentityno;
	}


	public void setCategoryIdentityno(String categoryIdentityno) {
		this.categoryIdentityno = categoryIdentityno;
	}


	public Integer getObjectMediaType() {
		return objectMediaType;
	}


	public void setObjectMediaType(Integer objectMediaType) {
		this.objectMediaType = objectMediaType;
	}


	public CategoryAssociation(String objectContentId,
			String categoryContentId, String categoryIdentityno) {
		super();
		this.objectContentId = objectContentId;
		this.categoryContentId = categoryContentId;
		this.categoryIdentityno = categoryIdentityno;

	}

	public static CategoryAssociation getCategoryAssociationModel(Row row) {
		String objectContentId=row.getAs("objectContentId");
		String categoryContentId=row.getAs("categoryContentId");
		String categoryIdentityno=row.getAs("categoryIdentityno");
		Integer objectMediaType=(Integer)row.getAs("objectMediaType");
		if(2!=objectMediaType&&!"2".equals(objectMediaType)){
			return null;
		}
		return new CategoryAssociation(objectContentId,categoryContentId,categoryIdentityno);
	}
}
