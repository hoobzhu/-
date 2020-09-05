package com.hoob.rs.sys.vo;

import java.util.List;

import com.hoob.rs.comm.vo.Response;

public class ContentIdGeneratorResponse extends Response{
List<ContentIdGeneratorVO>contentIdGeneratorList;
private  Long checkheadResult;
private Long total;



public Long getTotal() {
	return total;
}

public void setTotal(Long total) {
	this.total = total;
}

public Long getCheckheadResult() {
	return checkheadResult;
}

public void setCheckheadResult(Long checkheadResult) {
	this.checkheadResult = checkheadResult;
}

public List<ContentIdGeneratorVO> getContentIdGeneratorList() {
	return contentIdGeneratorList;
}

public void setContentIdGeneratorList(
		List<ContentIdGeneratorVO> contentIdGeneratorList) {
	this.contentIdGeneratorList = contentIdGeneratorList;
}
}
