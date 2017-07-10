package com.oumiao.monitor.model;


import com.oumiao.monitor.utils.DateUtil;

import java.io.Serializable;

/**
 * 实体类
 * 
 */
public abstract class Entity implements Serializable,Comparable<Entity>{

	public String mUUID;
	public String mTempSaveTime;//用于临时草稿箱保存时间

	@Override
	public int compareTo(Entity another) {
		return realCompareTo(another);
	}

	public int realCompareTo(Entity another){
		long time = DateUtil.parseTimestamp(another.mTempSaveTime).getTime();
		long time1 = DateUtil.parseTimestamp(this.mTempSaveTime).getTime();
		if(time > time1) return 1;
		else if(time == time1) return 0;
		else return -1;
	}
}
