package com.iaoe.jwExp.dao;

import java.util.List;
import com.iaoe.jwExp.entity.Area;

public interface AreaDao {
	/**
	 * 列出区域列表
	 * @return areaList
	 */
	List<Area> queryArea();
}
