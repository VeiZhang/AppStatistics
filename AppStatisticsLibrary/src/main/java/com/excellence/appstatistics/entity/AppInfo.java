package com.excellence.appstatistics.entity;

import java.util.List;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/23
 *     desc   :
 * </pre> 
 */
public class AppInfo
{
	private int mUsedCount;
	private long mUsedTime;
	private String mPackageName;
	private List<ActivityInfo> mActivityInfoList;

	public AppInfo(int usedCount, long usedTime, String packageName, List<ActivityInfo> activityInfoList)
	{
		mUsedCount = usedCount;
		mUsedTime = usedTime;
		mPackageName = packageName;
		mActivityInfoList = activityInfoList;
	}

	public int getUsedCount()
	{
		return mUsedCount;
	}

	public long getUsedTime()
	{
		return mUsedTime;
	}

	public String getPackageName()
	{
		return mPackageName;
	}

	public List<ActivityInfo> getActivityInfoList()
	{
		return mActivityInfoList;
	}
}
