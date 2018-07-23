package com.excellence.appstatistics.util;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.usage.UsageEvents.Event.MOVE_TO_BACKGROUND;
import static android.app.usage.UsageEvents.Event.MOVE_TO_FOREGROUND;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/20
 *     desc   : {@link UsageEvents.Event}
 *              {@link UsageStats}
 * </pre> 
 */
public class EventKit
{
	public static final String TAG = EventKit.class.getSimpleName();

	public static List<UsageEvents.Event> queryEventList(Context context, long startTime, long endTime)
	{
		List<UsageEvents.Event> eventList = new ArrayList<>();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
		{
			UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			UsageEvents events = usageStatsManager.queryEvents(startTime, endTime);
			while (events.hasNextEvent())
			{
				UsageEvents.Event event = new UsageEvents.Event();
				events.getNextEvent(event);
				if (event.getEventType() == 1 || event.getEventType() == 2)
				{
					eventList.add(event);
				}
			}
			checkEventList(eventList);
		}
		else
		{
			Log.e(TAG, "queryEventList: api version below");
		}
		return eventList;
	}

	/**
	 * 检索删除异常数据
	 * 异常数据：一个应用在队列里是成对出现的，比如位置0、位置1，是同一个应用，但是type不一样；
	 *          event均为type=1或type=2，成对出现，一旦发现未成对出现的数据，即视为异常数据
	 *
	 * @param eventList
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void checkEventList(List<UsageEvents.Event> eventList)
	{
		List<UsageEvents.Event> tempList = new ArrayList<>(eventList);
		eventList.clear();
		for (int i = 0; i < tempList.size() - 1; i++)
		{
			UsageEvents.Event preItem = tempList.get(i);
			UsageEvents.Event nextItem = tempList.get(i + 1);
			if (preItem.getClassName().equals(nextItem.getClassName()))
			{
				if (preItem.getEventType() != MOVE_TO_FOREGROUND || nextItem.getEventType() != MOVE_TO_BACKGROUND)
				{
					Log.e(TAG, "checkEventList: event exception - " + preItem.getPackageName());
				}
				else
				{
					eventList.add(preItem);
					eventList.add(nextItem);
					/**
					 * 正常：P0=1、P1=2则每次都加2
					 * 异常：P0=1、P1=1则每次都加1
					 */
					i++;
				}
			}
			else
			{
				Log.e(TAG, "checkEventList: package exception - " + preItem.getPackageName() + " : " + nextItem.getPackageName());
			}
		}
	}

	public static List<UsageStats> queryUsageStatsList(Context context, long startTime, long endTime)
	{
		List<UsageStats> usageStatsList = new ArrayList<>();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
		{
			UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime);
			for (Map.Entry<String, UsageStats> item : usageStatsMap.entrySet())
			{
				UsageStats usageStats = item.getValue();
				if (usageStats.getTotalTimeInForeground() > 0)
				{
					usageStatsList.add(usageStats);
				}
			}
		}
		else
		{
			Log.e(TAG, "queryUsageStatsList: api version below");
		}
		return usageStatsList;
	}
}
