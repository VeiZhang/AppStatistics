package com.excellence.appstatistics.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.excellence.appstatistics.AppDataManager;
import com.excellence.appstatistics.IUsageEventListener;
import com.excellence.appstatistics.entity.AppInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AppDataManager.with(this).search(0, new IUsageEventListener()
		{
			@Override
			public void onSuccess(List<AppInfo> appInfoList)
			{
				for (AppInfo appInfo : appInfoList)
				{
					System.out.println(appInfo.getPackageName() + ":" + appInfo.getUsedCount() + ":" + appInfo.getUsedTime());
				}
			}

			@Override
			public void onError(Throwable t)
			{
				Toast.makeText(MainActivity.this, "权限拒绝", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
