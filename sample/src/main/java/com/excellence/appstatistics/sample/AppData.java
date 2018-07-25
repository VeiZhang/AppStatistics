package com.excellence.appstatistics.sample;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.excellence.appstatistics.entity.ActivityInfo;
import com.excellence.appstatistics.entity.AppInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* <pre>
*     author : VeiZhang
*     blog   : http://tiimor.cn
*     time   : 2018/7/24
*     desc   :
* </pre> 
*/
public class AppData
{
	public static final String TAG = AppData.class.getSimpleName();
	public ObservableField<Drawable> mImage = new ObservableField<>();
	public ObservableField<String> mPackageName = new ObservableField<>();
	public ObservableField<String> mUsedCount = new ObservableField<>();
	public ObservableField<String> mUsedTime = new ObservableField<>();

	public AppData(Context context, AppInfo appInfo)
	{
		try
		{
			PackageManager pm = context.getPackageManager();
			mImage.set(pm.getApplicationIcon(appInfo.getPackageName()));
			mPackageName.set(String.format("包名: %1$s", appInfo.getPackageName()));
			mUsedCount.set(String.format("使用次数: %1$d", appInfo.getUsedCount()));
			mUsedTime.set(String.format("使用时间: %1$d ms", appInfo.getUsedTime()));
		}
		catch (Exception e)
		{
			Log.e(TAG, "get application icon error");
		}
	}

	public AppData(Context context, String pkg, List<ActivityInfo> activityInfoList)
	{
		try
		{
			ActivityInfo start = activityInfoList.get(0);
			ActivityInfo end = activityInfoList.get(activityInfoList.size() - 1);
			PackageManager pm = context.getPackageManager();
			mImage.set(pm.getApplicationIcon(pkg));
			mPackageName.set(String.format("包名: %1$s", pkg));
			mUsedCount.set(formatTime(start.getStartUsedTime(), end.getEndUsedTime()));
			mUsedTime.set(String.format(" 耗时: %1$d ms", end.getEndUsedTime() - start.getStartUsedTime()));
		}
		catch (Exception e)
		{
			Log.e(TAG, "get application icon error");
		}
	}

	public AppData(Context context, String pkg, ActivityInfo activityInfo)
	{
		try
		{
			PackageManager pm = context.getPackageManager();
			mImage.set(pm.getApplicationIcon(pkg));
			mPackageName.set(String.format("Activity: %1$s", activityInfo.getActivityCls()));
			mUsedCount.set(formatTime(activityInfo.getStartUsedTime(), activityInfo.getEndUsedTime()));
			mUsedTime.set(String.format(" 耗时: %1$d ms", activityInfo.getEndUsedTime() - activityInfo.getStartUsedTime()));
		}
		catch (Exception e)
		{
			Log.e(TAG, "get application icon error");
		}
	}

	private String formatTime(long start, long end)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return String.format("使用时间: %1$s - %2$s", format.format(new Date(start)), format.format(new Date(end)));
	}
}
