package cn.hoob.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.spark.sql.Row;

import cn.hoob.utils.StringUtils;
import cn.hoob.utils.SysUtils;
/*
 * 合集数据模型对象
 * ***/
public class SeriesModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3902841608255260387L;
	private Long id;
	private String contentId;
	private String kind;//内容类型
	private String catgoryids;//叶子栏目
	private String programType; 
	private String cast;
	private String similartyText;//相似文本
	private long   updateTime;//更新时间
	private Integer status;//上下线状态


	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getCatgoryids() {
		return catgoryids;
	}
	public void setCatgoryids(String catgoryids) {
		this.catgoryids = catgoryids;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getSimilartyText() {
		return similartyText;
	}
	public void setSimilartyText(String similartyText) {
		this.similartyText = similartyText;
	}
	/****/
	public SeriesModel() {

	}
	/***/
	public SeriesModel(Long id, String contentId,Integer status) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.status=status;
	}
	/***/
	public SeriesModel(Long id, String contentId, String kind,String programType,
			String cast,String catgoryids,long updateTime,Integer status) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.kind = kind;
		this.catgoryids = catgoryids;
		this.programType = programType;
		this.cast = cast;
		this.updateTime=updateTime;
		this.status=status;
	}
	/***/
	public SeriesModel(Long id, String contentId, String kind,String programType, String cast,
			String catgoryids,String similartyText,long updateTime,Integer status) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.kind = kind;
		this.catgoryids = catgoryids;
		this.programType = programType;
		this.cast = cast;
		this.similartyText=similartyText;
		this.updateTime=updateTime;
		this.status=status;
	}
	/***/
	public SeriesModel(Long id, String contentId, String kind,String programType, String cast,
			long updateTime,Integer status) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.kind = kind;
		this.programType = programType;
		this.cast = cast;
		this.updateTime=updateTime;
		this.status=status;
	}
	/***/
	public SeriesModel(Long id, String contentId,String programType,Integer status) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.programType = programType;
		this.status=status;

	}
	/***/
	public static SeriesModel getSeriesModelByIdAndContentId(Row row) {

		Long id=row.getAs("id");
		String uid=row.getAs("contentId");
		Integer status=row.getAs("status");
		return new SeriesModel(id,uid,status);
	}
	/***/
	public static SeriesModel getSeriesModel(Row row) {
		String simiartyField=SysUtils.getSysparamString("SimiartyField", "kind,cast,catgoryids");
		Long id=row.getAs("id");
		String contentId=row.getAs("contentId");
		String kind=row.getAs("kind");
		String catgoryids=row.getAs("catgoryids");
		String programType=row.getAs("programType");
		String cast=row.getAs("cast");
		String similartyText="";
		long updateTime=row.getAs("updateTime");
		Integer status=row.getAs("status");
		if(simiartyField.contains("kind")){
			similartyText=kind;
		}
		if(simiartyField.contains("cast")){
			if(StringUtils.isNotEmpty(similartyText)){
				similartyText=similartyText+"|"+cast;
			}else{
				similartyText=cast;
			}
		}
		if(simiartyField.contains("catgoryids")){
			if(StringUtils.isNotEmpty(similartyText)){
				similartyText=similartyText+"|"+catgoryids;
			}else{
				similartyText=catgoryids;
			}
		}
		//根据配置模型,选择需要用来计算相似性的文本
		return new SeriesModel(id,contentId,kind,programType,cast,catgoryids,similartyText,updateTime,status);
	}
	/***/
	public static SeriesModel getSeriesModelWithNotCatgoryids(Row row) {

		long id=Long.parseLong(row.getAs("id")+"");
		String contentId=row.getAs("contentId");
		String kind=row.getAs("genre");
		//String catgoryids=row.getAs("catgoryids");
		String programType=row.getAs("vodType");
		String cast=row.getAs("actors");
		String directors=row.getAs("directors");
		long updateTime=((Date)row.getAs("updateTime")).getTime();
		Integer status=row.getAs("status");
		if(StringUtils.isNotEmpty(directors)){
			if(StringUtils.isNotEmpty(cast)){
				cast=cast+"|"+directors;
			}else{
				cast=directors;
			}
		}
		if(StringUtils.isNotEmpty(cast)){
			cast=cast.replaceAll(",","|");
		}
		return new SeriesModel(id,contentId,kind,programType,cast,updateTime,status);
	}
	/***/
	public static SeriesModel getSeriesModelWithProgramType(Row row) {

		long id=row.getAs("id");
		String contentId=row.getAs("contentId");
		String programType=row.getAs("vodType");
		Integer status=row.getAs("status");
		return new SeriesModel(id,contentId,programType,status);
	}

}
