package com.excellence.appstatistics.entity;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/23
 *     desc   :
 * </pre> 
 */
public class ActivityInfo
{
	private String mActivityCls;
	private long mStartUsedTime;
	private long mEndUsedTime;

	public ActivityInfo(String activityCls, long startUsedTime, long endUsedTime)
	{
		mActivityCls = activityCls;
		mStartUsedTime = startUsedTime;
		mEndUsedTime = endUsedTime;
	}

	public String getActivityCls()
	{
		return mActivityCls;
	}

	public long getStartUsedTime()
	{
		return mStartUsedTime;
	}

	public long getEndUsedTime()
	{
		return mEndUsedTime;
	}
}
