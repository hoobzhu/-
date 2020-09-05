package cn.hoob.rs.model;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Row;

/****
 * 分词后合集的向量模型
 * ******/
public class SimiartyDataFeatureModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7558786832446218987L;
	private Long id;
	private String contentId;
	private String similartyText;
    private String vectorIndices;
    private String vectorValues;
    private Integer vectorSize;
    private SparseVector features;
    

	
	public String getSimilartyText() {
		return similartyText;
	}

	public void setSimilartyText(String similartyText) {
		this.similartyText = similartyText;
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

	public String getVectorIndices() {
		return vectorIndices;
	}

	public void setVectorIndices(String vectorIndices) {
		this.vectorIndices = vectorIndices;
	}

	public String getVectorValues() {
		return vectorValues;
	}

	public void setVectorValues(String vectorValues) {
		this.vectorValues = vectorValues;
	}

	public Integer getVectorSize() {
		return vectorSize;
	}

	public void setVectorSize(Integer vectorSize) {
		this.vectorSize = vectorSize;
	}
	
	public SparseVector getFeatures() {
		return features;
	}

	public void setFeatures(SparseVector features) {
		this.features = features;
	}

	public SimiartyDataFeatureModel(){}
	
	public SimiartyDataFeatureModel(Long id, String contentId, String similartyText,
			String vectorIndices, String vectorValues, Integer vectorSize) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.similartyText = similartyText;
		this.vectorIndices = vectorIndices;
		this.vectorValues = vectorValues;
		this.vectorSize = vectorSize;
	}

	public static SimiartyDataFeatureModel getSimiartyDataFeatureModel(Row row) {
		 SimiartyDataFeatureModel data=new SimiartyDataFeatureModel();
		 data.setId(Long.parseLong(row.getAs("id") + ""));
         data.setContentId(row.getAs("contentId"));
         data.setSimilartyText(row.getAs("similartyText"));
         SparseVector vector = row.getAs("features");
         data.setFeatures(vector);
         data.setVectorSize(vector.size());
         data.setVectorIndices(Arrays.toString(vector.indices()));
         data.setVectorValues(Arrays.toString(vector.values()));
         data.toString();
         return data;
         
	}

	@Override
	public String toString() {
		return "SimiartyDataFeatureModel [id=" + id + ", contentId="
				+ contentId + ", similartyText=" + similartyText
				+ ", vectorIndices=" + vectorIndices + ", vectorValues="
				+ vectorValues + ", vectorSize=" + vectorSize + ", features="
				+ features + "]";
	}
	
}
