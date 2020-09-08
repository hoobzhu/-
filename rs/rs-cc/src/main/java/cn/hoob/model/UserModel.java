package cn.hoob.model;

import java.io.Serializable;

import org.apache.spark.sql.Row;

/*
 * 用户对象模型
 * **/
public class UserModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2050562591632708426L;
	private long id;
	private String userId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    /***/
	public UserModel() {
		
	}
    /***/
	public UserModel(long id, String userId) {
		super();
		this.id = id;
		this.userId = userId;
	}
    /***/
	public static UserModel getUserModel(Row row) {

		long id=Long.parseLong(row.getAs("id")+"");
		String uid=row.getAs("userId");
		return new UserModel(id,uid);
	}
}
