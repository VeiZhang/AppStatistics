package com.excellence.appstatistics;

import com.excellence.appstatistics.entity.AppInfo;

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
	void onSuccess(List<AppInfo> appInfoList);
}
