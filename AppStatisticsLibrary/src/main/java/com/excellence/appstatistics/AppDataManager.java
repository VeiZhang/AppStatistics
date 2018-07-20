package com.excellence.appstatistics;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.Context;

import java.util.List;

import static com.excellence.appstatistics.util.EventKit.queryEventList;
import static com.excellence.appstatistics.util.EventKit.queryUsageStatsList;
import static com.excellence.appstatistics.util.TimeKit.DAY;
import static com.excellence.appstatistics.util.TimeKit.getZeroClockTime;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/19
 *     desc   :
 * </pre>
 */
public class AppDataManager
{
	private static AppDataManager mInstance = null;
	private Context mContext = null;

	public static AppDataManager with(Context context)
	{
		if (mInstance == null)
		{
			mInstance = new AppDataManager(context.getApplicationContext());
		}
		return mInstance;
	}

	private AppDataManager(Context context)
	{
		mContext = context;
	}

	public void search(long days, ISearchListener listener)
	{
		ISearchListener searchListener = new ResultListener(listener);
		long startTime = 0;
		long endTime = 0;
		if (days == 0)
		{
			/**
			 * 默认获取当天
			 */
			endTime = System.currentTimeMillis();
			startTime = getZeroClockTime(endTime);
		}
		else
		{
			/**
			 * 距离现在的前多少天当天
			 * 如距离现在的前一天，00:00:00 - 23:59:59
			 */
			endTime = getZeroClockTime(System.currentTimeMillis() - DAY * (days - 1)) - 1;
			startTime = endTime - DAY;
		}
		List<UsageEvents.Event> eventList = queryEventList(mContext, startTime, endTime);
		List<UsageStats> usageStatsList = queryUsageStatsList(mContext, startTime, endTime);
	}

	private class ResultListener implements ISearchListener
	{
		private ISearchListener mSearchListener = null;

		public ResultListener(ISearchListener listener)
		{
			mSearchListener = listener;
		}

		@Override
		public void onSuccess(List<UsageEvents.Event> eventList, List<UsageStats> usageStatsList)
		{
			if (mSearchListener != null)
			{
				mSearchListener.onSuccess(eventList, usageStatsList);
			}
		}
	}
}
