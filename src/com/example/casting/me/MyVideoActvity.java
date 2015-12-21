package com.example.casting.me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.casting.MainTab;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.VideoBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.adapter.MyVideoAdapter;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.video.GetListVideoProcessor;
import com.example.casting.publisheddynamic.VideoPlayActivity;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshGridView;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownLoadCallback;
import com.ta.util.download.DownloadManager;

public class MyVideoActvity extends BaseForm implements OnItemClickListener{

	View titleLayout;
	GetListVideoProcessor getVideoProcessor;
	MyVideoAdapter imgsAdapter;
	List<VideoBean> videoBeans = new ArrayList<VideoBean>();
	PullToRefreshGridView gridView ;
	GridView myGridView;
	DownloadManager downloadManager;
	boolean isRefresh = false;
	boolean isLoadMore = false;
	int currenPage = 1;
	RegistBean registbean = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		View view = View.inflate(this, R.layout.videogrally, null);
		setContentView(view);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(false,"添加");
		setTitle("我的视频");
		gridView = (PullToRefreshGridView) findViewById(R.id.gridView1);
		gridView.setScrollLoadEnabled(true);
		
		imgsAdapter = new MyVideoAdapter(this,videoBeans);
		myGridView = gridView.getRefreshableView();
		myGridView.setAdapter(imgsAdapter);
		myGridView.setOnItemClickListener(this);
		initRefreshListener();
		downloadManager = DownloadManager.getDownloadManager();
		downloadManager.setDownLoadCallback(new DownLoadCallback() {
				@Override
				public void onSuccess(String url) {
					imgsAdapter.notifyDataSetChanged();
				}
				@Override
				public void onLoading(String url, long totalSize,
						long currentSize, long speed) {
					System.out.println("-----video onloading----");
					
				}
			});
		registbean = initData(getIntent());
//		getVideo(false);
	}
	
	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.myvideoactvity + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.myvideoactvity + "");
				return bean;
			}
		}
		return null;
	}

	/**
	 * 从服务端请求数据
	 * @param isLoadMore 是否加载下一页，true：page++
	 */
	public void getVideo(boolean isLoadMore)
	{
		getVideoProcessor = new GetListVideoProcessor();
		if(registbean!=null){
			BaseBean bean = new BaseBean();
			bean.setId(registbean.getId());
			HttpCall(false, getVideoProcessor,bean);
		}
		else{
			showToast("未获取到传值"+MyVideoActvity.class.getName());
		}
	}
	public void initRefreshListener()
	{
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				isRefresh = true;
				currenPage = 1;
				getVideo(false);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				System.out.println("---上拉加载更多------");
				isLoadMore = true;
				getVideo(true);
			}
			
		});
		gridView.setLastUpdatedLabel(com.example.casting.util.view.Util.getCurrentTime());
		gridView.doPullRefreshing(true, 500);
	}
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		if(bean.getCode() == Errors.OK)
		{
			if(processor.getMethod().equals(getVideoProcessor.getMethod()))
			{
				List<VideoBean> beans = (List<VideoBean>) bean.getObj();
				if(beans.size()>0)
				{
					if(isLoadMore)
					{
						 currenPage++;
					}
					else {
						videoBeans.clear();
					}
					videoBeans.addAll(beans);
					imgsAdapter.notifyDataSetChanged();
					for(VideoBean videoBean:beans)
					{
						if(!videoBean.getUrl().equals(""))
						{
							 downloadManager.addHandler(Server_path.serverfile_path+videoBean.getUrl());
						}
					}
				}
				else {
					if(isLoadMore)
						showToast(getString(R.string.no_data));
					else 
					   showToast(getString(R.string.nodata));
				}
				reFreshOrLoadComplete();
			}
			
		}
		else {
			showToast(bean.getMessage());
			reFreshOrLoadComplete();
		}
	}
	@Override
	public void LeftButtonClick() {

		finish();
	}

	@Override
	public void RightButtonClick() {
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		VideoBean bean = (VideoBean) imgsAdapter.getItem(position);
		String path = DownloadManager.FILE_ROOT
				+ TAStringUtils.getFileNameFromUrl(bean.getUrl());
		File file = new File(path);
		if (file.exists()) {
			Intent intent = new Intent(MyVideoActvity.this, VideoPlayActivity.class);
			intent.putExtra("uri", path);
			intent.putExtra("action", "main");
			// TODO 修改参数
			startActivity(intent);
		}
	}
	public void reFreshOrLoadComplete()
	{
		if(isLoadMore)
		{
			 gridView.onPullUpRefreshComplete();
	         gridView.setHasMoreData(true);
		}
		else if(isRefresh)
		{
			 gridView.onPullDownRefreshComplete();
			 gridView.setLastUpdatedLabel(com.example.casting.util.view.Util.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}
}
