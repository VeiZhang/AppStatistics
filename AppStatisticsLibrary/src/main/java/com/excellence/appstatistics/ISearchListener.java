package com.excellence.appstatistics;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;

import java.util.List;

/**
* <pre>
*     author : VeiZhang
*     blog   : http://tiimor.cn
*     time   : 2018/7/20
*     desc   :
* </pre> 
*/
public interface ISearchListener
{
	void onSuccess(List<UsageEvents.Event> eventList, List<UsageStats> usageStatsList);
}
