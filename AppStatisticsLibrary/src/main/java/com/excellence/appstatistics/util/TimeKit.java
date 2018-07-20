package com.excellence.appstatistics.util;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/20
 *     desc   : 时间工具
 * </pre> 
 */
public class TimeKit
{
	public static final int SEC = 1000;
	public static final int MIN = 60 * 1000;
	public static final int HOUR = 60 * 60 * 1000;
	public static final int DAY = 24 * 60 * 60 * 1000;

	/**
	 * 获取当日00:00:00的时间戳
	 *
	 * @param time
	 * @return
	 */
	public static long getZeroClockTime(long time)
	{
		long zeroClockTime = time;
		zeroClockTime -= zeroClockTime % DAY;
		return zeroClockTime;
	}
}
