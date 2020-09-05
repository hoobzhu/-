/**
 * 
 */
package com.hoob.rs.sys.vo;

import java.util.List;

import com.hoob.rs.comm.vo.Response;

/**
 * @Description 
 * @author hoob
 * @date 2017年9月18日下午12:23:39
 */
public class OperlogResponse extends Response{

	private List<OperlogVO>operlogList;
	private OperlogVO operlogVO;
	private Long total;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<OperlogVO> getOperlogList() {
		return operlogList;
	}
	public void setOperlogList(List<OperlogVO> operlogList) {
		this.operlogList = operlogList;
	}
	public OperlogVO getOperlogVO() {
		return operlogVO;
	}
	public void setOperlogVO(OperlogVO operlogVO) {
		this.operlogVO = operlogVO;
	}
	

}
