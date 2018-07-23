package com.excellence.appstatistics.sample;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.excellence.appstatistics.AppDataManager;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**
		Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
		startActivity(intent);
		 */

		AppDataManager.with(this).search(0, null);
	}
}
