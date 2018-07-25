package com.excellence.appstatistics.sample;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.excellence.appstatistics.AppDataManager;
import com.excellence.appstatistics.callback.IUsageEventListener;
import com.excellence.appstatistics.entity.ActivityInfo;
import com.excellence.appstatistics.entity.AppInfo;
import com.excellence.appstatistics.sample.databinding.ActivityMainBinding;
import com.excellence.basetoolslibrary.databinding.BaseRecyclerBindingAdapter;
import com.excellence.basetoolslibrary.databinding.MultiItemTypeBindingRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
	private static final String PARAM_TYPE = "type";
	private static final String PARAM_PKG = "pkg";
	private static final String PARAM_POS = "pos";
	private static final int TYPE_ALL = 0;
	private static final int TYPE_APP = 1;
	private static final int TYPE_ACTIVITY = 2;

	private Context mContext = null;
	private ActivityMainBinding mBinding = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		mContext = this;

		int type = getIntent().getIntExtra(PARAM_TYPE, TYPE_ALL);
		String pkg = getIntent().getStringExtra(PARAM_PKG);
		int pos = getIntent().getIntExtra(PARAM_POS, -1);
		switch (type)
		{
		case TYPE_ALL:
		default:
			setTitle("全部应用数据");
			initAllView();
			break;

		case TYPE_APP:
			setTitle("单个应用具体数据");
			initAppView(pkg);
			break;

		case TYPE_ACTIVITY:
			setTitle("单个应用使用一次期间的Activity:" + pkg);
			initActivityView(pkg, pos);
			break;
		}
	}

	private void initActivityView(final String pkg, int pos)
	{
		List<AppData> showDataList = new ArrayList<>();
		List<List<ActivityInfo>> appInfoList = new ArrayList<>();
		for (int i = 0; i < AppDataManager.getAppInfoList().size(); i++)
		{
			if (pkg.equals(AppDataManager.getAppInfoList().get(i).getPackageName()))
			{
				appInfoList = AppDataManager.getAppInfoList().get(i).getActivityInfoList();
			}
		}
		for (int i = 0; i < appInfoList.get(pos).size(); i++)
		{
			showDataList.add(new AppData(mContext, pkg, appInfoList.get(pos).get(i)));
		}
		BaseRecyclerBindingAdapter<AppData> adapter = new BaseRecyclerBindingAdapter<>(showDataList, R.layout.item_layout, BR.item);
		mBinding.setAdapter(adapter);
		mBinding.setLayoutManager(new LinearLayoutManager(mContext));
	}

	private void initAppView(final String pkg)
	{
		List<AppData> showDataList = new ArrayList<>();
		List<List<ActivityInfo>> appInfoList = new ArrayList<>();
		for (int i = 0; i < AppDataManager.getAppInfoList().size(); i++)
		{
			if (pkg.equals(AppDataManager.getAppInfoList().get(i).getPackageName()))
			{
				appInfoList = AppDataManager.getAppInfoList().get(i).getActivityInfoList();
			}
		}
		for (int i = 0; i < appInfoList.size(); i++)
		{
			showDataList.add(new AppData(mContext, pkg, appInfoList.get(i)));
		}
		BaseRecyclerBindingAdapter<AppData> adapter = new BaseRecyclerBindingAdapter<>(showDataList, R.layout.item_layout, BR.item);
		mBinding.setAdapter(adapter);
		mBinding.setLayoutManager(new LinearLayoutManager(mContext));
		adapter.setOnItemClickListener(new MultiItemTypeBindingRecyclerAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(ViewDataBinding binding, View v, int position)
			{
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra(PARAM_TYPE, TYPE_ACTIVITY);
				intent.putExtra(PARAM_PKG, pkg);
				intent.putExtra(PARAM_POS, position);
				startActivity(intent);
			}
		});
	}

	private void initAllView()
	{
		AppDataManager.with(this).search(0, new IUsageEventListener()
		{
			@Override
			public void onSuccess(final List<AppInfo> appInfoList)
			{
				List<AppData> showDataList = new ArrayList<>();
				for (AppInfo appInfo : appInfoList)
				{
					showDataList.add(new AppData(mContext, appInfo));
				}
				BaseRecyclerBindingAdapter<AppData> adapter = new BaseRecyclerBindingAdapter<>(showDataList, R.layout.item_layout, BR.item);
				mBinding.setAdapter(adapter);
				mBinding.setLayoutManager(new LinearLayoutManager(mContext));
				adapter.setOnItemClickListener(new MultiItemTypeBindingRecyclerAdapter.OnItemClickListener()
				{
					@Override
					public void onItemClick(ViewDataBinding binding, View v, int position)
					{
						Intent intent = new Intent(mContext, MainActivity.class);
						intent.putExtra(PARAM_TYPE, TYPE_APP);
						intent.putExtra(PARAM_PKG, appInfoList.get(position).getPackageName());
						intent.putExtra(PARAM_POS, position);
						startActivity(intent);
					}
				});
			}

			@Override
			public void onError(Throwable t)
			{
				Toast.makeText(mContext, "权限拒绝", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
