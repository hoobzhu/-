package com.hoob.rs.sys.vo;

import java.util.Comparator;

public class ListComparator implements Comparator<FileProviderVO> {
	@Override
	public int compare(FileProviderVO f1, FileProviderVO f2) {
		if (f1.getType() > f2.getType()) {
			return 1;
		}else if(f1.getType() < f2.getType()){
			return -1;
		}
		else if(f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase())!=0){
			return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
		}
		else {
			return 0;
		}
	}
}
