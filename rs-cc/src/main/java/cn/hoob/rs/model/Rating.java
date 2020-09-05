package cn.hoob.rs.model;

import java.io.Serializable;

public class Rating implements Serializable {
  private int uId;
  private int sId;
  private double rating;
  private String userId;
  private String scontentId;
  public Rating() {}
 
  
 
  public int getuId() {
	return uId;
}



public void setuId(int uId) {
	this.uId = uId;
}



public int getsId() {
	return sId;
}



public void setsId(int sId) {
	this.sId = sId;
}



public double getRating() {
	return rating;
}



public void setRating(double rating) {
	this.rating = rating;
}



public String getUserId() {
	return userId;
}



public void setUserId(String userId) {
	this.userId = userId;
}



public String getScontentId() {
	return scontentId;
}



public void setScontentId(String scontentId) {
	this.scontentId = scontentId;
}



public Rating(int uId, int sId, double rating, String userId, String scontentId) {
	super();
	this.uId = uId;
	this.sId = sId;
	this.rating = rating;
	this.userId = userId;
	this.scontentId = scontentId;
}



public Rating(String userId, String scontentId, double rating) {
	super();
	this.userId = userId;
	this.scontentId = scontentId;
	this.rating = rating;
}



public static Rating parseSimpleRating(String str) {
	//userId|contentId|playcount
    String[] fields = str.split("\\|");
    if (fields.length != 3) {
      return null;
     // throw new IllegalArgumentException("Each line must contain 3 fields");
    }
    String userId=fields[0];
    String contentId=fields[1];
    double rating=Double.parseDouble(fields[2]);
    return new Rating(userId, contentId, rating);
  }
}
