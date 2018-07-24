package com.excellence.appstatistics.callback;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/24
 *     desc   :
 * </pre> 
 */
public interface IPermissionListener
{
	void onPermissionGranted();

	void onPermissionDenied();
}
