package com.excellence.appstatistics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.excellence.appstatistics.callback.IPermissionListener;

import static com.excellence.appstatistics.util.EventKit.checkUsagePermission;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/7/23
 *     desc   :
 * </pre> 
 */
public class PermissionActivity extends Activity
{

	private static final int USAGE_REQUEST_CODE = 1024;

	private static IPermissionListener mPermissionListener = null;

	public static void setPermissionListener(IPermissionListener permissionListener)
	{
		mPermissionListener = permissionListener;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (mPermissionListener == null)
		{
			finish();
			return;
		}

		if (!checkUsagePermission(this))
		{
			new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog).setCancelable(false).setTitle(R.string.setting_prompt).setMessage(R.string.allow_permission)
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							mPermissionListener.onPermissionDenied();
							mPermissionListener = null;
							finish();
						}
					}).setPositiveButton(R.string.open, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
							startActivityForResult(intent, USAGE_REQUEST_CODE);
						}
					}).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (mPermissionListener != null)
		{
			switch (requestCode)
			{
			case USAGE_REQUEST_CODE:
				if (checkUsagePermission(this))
				{
					mPermissionListener.onPermissionGranted();
				}
				else
				{
					mPermissionListener.onPermissionDenied();
				}
				break;

			}
			mPermissionListener = null;
		}
		finish();
	}
}
