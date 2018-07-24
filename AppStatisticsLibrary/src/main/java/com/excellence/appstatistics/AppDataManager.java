package com.excellence.appstatistics;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.excellence.appstatistics.callback.IPermissionListener;
import com.excellence.appstatistics.callback.IUsageEventListener;
import com.excellence.appstatistics.entity.AppInfo;
import com.excellence.appstatistics.entity.ActivityInfo;

import java.util.ArrayList;
import java.util.List;

import static com.excellence.appstatistics.util.EventKit.checkUsagePermission;
import static com.excellence.appstatistics.util.EventKit.queryEventList;
import static com.excellence.appstatistics.util.EventKit.queryUsageStatsList;
import static com.excellence.appstatistics.util.TimeKit.DAY;
import static com.excellence.appstatistics.util.TimeKit.getZeroClockTime;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/19
 *     desc   : 权限 {@link android.Manifest.permission#PACKAGE_USAGE_STATS}
 *     			开启 {@link Settings#ACTION_USAGE_ACCESS_SETTINGS}
 *     @see
 * </pre>
 */
public class AppDataManager
{
	public static final String TAG = AppDataManager.class.getSimpleName();

	private static AppDataManager mInstance = null;
	private Context mContext = null;
	private long mStartTime = 0;
	private long mEndTime = 0;
	private IPermissionListener mPermissionListener = null;
	private IUsageEventListener mUsageEventListenerImp = null;
	private IUsageEventListener mUsageEventListener = null;
	private List<AppInfo> mAppInfoList = null;
	private List<UsageEvents.Event> mEventList = null;
	private List<UsageStats> mUsageStatsList = null;

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
		mPermissionListener = new PermissionListener();
		mUsageEventListenerImp = new UsageEventListenerImp();
		mAppInfoList = new ArrayList<>();
		mEventList = new ArrayList<>();
		mUsageStatsList = new ArrayList<>();
	}

	/**
	 * 时间戳里的应用痕迹
	 *
	 * @param startTime
	 * @param endTime
	 * @param listener
	 */
	public void search(long startTime, long endTime, IUsageEventListener listener)
	{
		if (endTime <= 0)
		{
			/**
			 * 默认获取当天
			 */
			endTime = System.currentTimeMillis();
			startTime = getZeroClockTime(endTime);
		}
		mStartTime = startTime;
		mEndTime = endTime;
		mUsageEventListener = listener;
		if (checkUsagePermission(mContext))
		{
			mPermissionListener.onPermissionGranted();
		}
		else
		{
			startPermissionActivity();
		}
	}

	private void startPermissionActivity()
	{
		PermissionActivity.setPermissionListener(mPermissionListener);
		Intent intent = new Intent(mContext, PermissionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	/**
	 * 前几天的当天：一天里的应用痕迹
	 *
	 * @param days
	 * @param listener
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void search(long days, IUsageEventListener listener)
	{
		long startTime = 0;
		long endTime = 0;
		if (days > 0)
		{
			/**
			 * 距离现在的前多少天当天
			 * 如距离现在的前一天，00:00:00 - 23:59:59
			 */
			endTime = getZeroClockTime(System.currentTimeMillis() - DAY * (days - 1)) - 1;
			startTime = endTime - DAY;
		}
		search(startTime, endTime, listener);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private List<AppInfo> generateAppInfoList(List<UsageEvents.Event> eventList, List<UsageStats> usageStatsList)
	{
		List<AppInfo> appInfoList = new ArrayList<>();
		for (UsageStats usageStats : usageStatsList)
		{
			String pkg = usageStats.getPackageName();
			List<List<ActivityInfo>> activityInfoList = formatOneTimeAppInfo(pkg, eventList);
			appInfoList.add(new AppInfo(activityInfoList.size(), usageStats.getTotalTimeInForeground(), pkg, activityInfoList));
		}
		return appInfoList;
	}

	/**
	 * 应用一次打开：可以是单个Activity或者多个Activity
	 * 如果是单个Activity，那就是两个相同包名和Activity名字的{@link android.app.usage.UsageEvents.Event}
	 * 如果是多个Activity，那就是连续的{@link android.app.usage.UsageEvents.Event}
	 *
	 * @param pkg
	 * @param eventList
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private List<List<ActivityInfo>> formatOneTimeAppInfo(String pkg, List<UsageEvents.Event> eventList)
	{
		List<List<ActivityInfo>> activityInfoList = new ArrayList<>();
		List<ActivityInfo> oneTimeActivityInfoList = new ArrayList<>();
		for (int i = 0; i < eventList.size() - 1; i++)
		{
			UsageEvents.Event preEvent = eventList.get(i);
			UsageEvents.Event nextEvent = eventList.get(i + 1);
			if (pkg.equals(preEvent.getPackageName()) && preEvent.getClassName().equals(nextEvent.getClassName()))
			{
				/**
				 * 过滤指定的应用：一次打开应用的时候收集的所有痕迹
				 */
				oneTimeActivityInfoList.add(new ActivityInfo(preEvent.getClassName(), preEvent.getTimeStamp(), nextEvent.getTimeStamp()));
			}
			else
			{
				/**
				 * 收集指定包名所有打开次数的痕迹：注意连续的Event算一次打开
				 * 包名不对时，则此次痕迹收集完毕，再进行下一次检索
				 */
				if (oneTimeActivityInfoList.size() > 0)
				{
					/**
					 * 添加一次打开记录
					 */
					activityInfoList.add(oneTimeActivityInfoList);
					/**
					 * 继续下一次打开痕迹的采集
					 */
					oneTimeActivityInfoList = new ArrayList<>();
				}
			}
			i++;
		}
		return activityInfoList;
	}

	/**
	 * 上一次查询的数据
	 *
	 * @return
	 */
	public static List<AppInfo> getAppInfoList()
	{
		checkManager();
		return mInstance.mAppInfoList;
	}

	/**
	 * 上一次查询的数据
	 *
	 * @return
	 */
	public static List<UsageEvents.Event> getEventList()
	{
		checkManager();
		return mInstance.mEventList;
	}

	/**
	 * 上一次查询的数据
	 * 
	 * @return
	 */
	public static List<UsageStats> getUsageStatsList()
	{
		checkManager();
		return mInstance.mUsageStatsList;
	}

	private static void checkManager()
	{
		if (mInstance == null)
		{
			throw new RuntimeException("AppDataManager not initialized!!!");
		}
	}

	private class UsageEventListenerImp implements IUsageEventListener
	{

		@Override
		public void onSuccess(List<AppInfo> appInfoList)
		{
			if (mUsageEventListener != null)
			{
				mUsageEventListener.onSuccess(appInfoList);
			}
		}

		@Override
		public void onError(Throwable t)
		{
			if (mUsageEventListener != null)
			{
				mUsageEventListener.onError(t);
			}
		}
	}

	private class PermissionListener implements IPermissionListener
	{
		@Override
		public void onPermissionGranted()
		{
			mEventList = queryEventList(mContext, mStartTime, mEndTime);
			mUsageStatsList = queryUsageStatsList(mContext, mStartTime, mEndTime);
			mAppInfoList = generateAppInfoList(mEventList, mUsageStatsList);
			mUsageEventListenerImp.onSuccess(mAppInfoList);
		}

		@Override
		public void onPermissionDenied()
		{
			mUsageEventListenerImp.onError(new Throwable("Permission denied"));
		}
	}
}
