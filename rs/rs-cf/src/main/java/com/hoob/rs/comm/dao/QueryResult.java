/**
 * 
 */
package com.hoob.rs.comm.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raul 2017年5月19日
 */
public class QueryResult<T> {

	private List<T> results = new ArrayList<T>();

	private long count;
    /***/
	public QueryResult(){
		
	}
    /***/
	public QueryResult(List<T> results, long count) {
		super();
		this.results = results;
		this.count = count;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
