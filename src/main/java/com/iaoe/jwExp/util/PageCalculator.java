package com.iaoe.jwExp.util;

public class PageCalculator {
	/**
	 * 通过页码和页内数据条数，转换为rowIndex值，也就是从第几行开始的值
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static int calculateRowIndex(int pageIndex,int pageSize) {
		return (pageIndex>0)?(pageIndex-1)*pageSize:0;
	}
}
