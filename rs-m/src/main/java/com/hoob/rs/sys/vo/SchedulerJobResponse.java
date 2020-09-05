package com.hoob.rs.sys.vo;

import java.util.List;

import com.hoob.rs.comm.vo.Response;

public class SchedulerJobResponse extends Response{
private List<SchedulerJobVO>schedulerJobList;
private Long total;
private SchedulerJobVO schedulerJobVO;
public SchedulerJobVO getSchedulerJobVO() {
	return schedulerJobVO;
}
public void setSchedulerJobVO(SchedulerJobVO schedulerJobVO) {
	this.schedulerJobVO = schedulerJobVO;
}

public Long getTotal() {
	return total;
}
public void setTotal(Long total) {
	this.total = total;
}
public List<SchedulerJobVO> getSchedulerJobList() {
	return schedulerJobList;
}
public void setSchedulerJobList(List<SchedulerJobVO> schedulerJobList) {
	this.schedulerJobList = schedulerJobList;
}
}
