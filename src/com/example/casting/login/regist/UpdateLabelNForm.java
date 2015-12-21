package com.example.casting.login.regist;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import com.example.casting.entity.ConditionBean;
import com.example.casting.login.BaseForm;
import com.example.casting.login.regist.adapter.LabelAdapter;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.plaza.getConditionsProcessor;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshListView;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting_android.R;

public class UpdateLabelNForm extends BaseForm {
	View titleLayout;
	LabelAdapter adapter;
	PullToRefreshListView lst_labels;
	ListView simpleListView;
	getConditionsProcessor conditionPro = new getConditionsProcessor();
	ArrayList<ConditionBean> mylist = new ArrayList<ConditionBean>();
	boolean isRefresh = false;
	boolean isLoadMore = false;
	int currentPageNum = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edt_labeln);
		initSubView();
		initRefreshListener();
	}
	public void initRefreshListener()
	{
		lst_labels.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = true;
				currentPageNum = 1;
				uploadConditions();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				System.out.println("---上拉加载更多------");
				isLoadMore = true;
				uploadConditions();
			}
			
		});
		lst_labels.setLastUpdatedLabel(com.example.casting.util.view.Util.getCurrentTime());
		lst_labels.doPullRefreshing(true, 500);
	}
	private void uploadConditions(){
		ConditionBean bean = new ConditionBean();
		bean.setType("2");
		if(isLoadMore)
			bean.setPagenum(currentPageNum+1+"");
		else {
			if(isRefresh)
				currentPageNum = 1;
			bean.setPagenum(currentPageNum+"");
		}
		HttpCall(false, conditionPro, bean);
	}
	private void initSubView() {
		titleLayout = (View) findViewById(R.id.titlelayout);
		lst_labels = (PullToRefreshListView)findViewById(R.id.lst_labels);
		lst_labels.setScrollLoadEnabled(true);
		simpleListView = lst_labels.getRefreshableView();
		adapter = new LabelAdapter(this,mylist);
		simpleListView.setAdapter(adapter);
		initView(titleLayout);
		setTitle("编辑标签");
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "保存");
		String labelstr = initData(getIntent().getExtras());
		adapter.initSelectedItems(labelstr);
	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		uploadConditions();
//	}


	private String initData(Bundle bd) {
		String defaultLabels = "";
		if (bd != null) {
			defaultLabels =  bd.getString(R.string.updatelabelnform+"");
		}
		return defaultLabels;
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		setBack( );
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		setBack( );
	}

	private String getText(){
		String value = adapter.getSelectedValue();
		return value;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setBack();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private void setBack() {
		String values = getText() ;
		Intent intent = new Intent();
		Bundle bd = new Bundle();
		bd.putString(R.string.updatelabelnform+"", values);
		intent.putExtras(bd);
		setResult(R.string.updatelabelnform,intent);
		finish();
		
	}
	
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		if (bean.getCode() == Errors.OK) {
				if (processor.getMethod().equals(conditionPro.getMethod())) {
				List<ConditionBean> condits = (List) bean.getObj();
				if(condits != null && condits.size()>0)
				{
					if(isLoadMore)
					{
						currentPageNum++;
					}
					else {
						mylist.clear();
					}
					updateConditions(condits);
				}
				else
				{
					if(isLoadMore)
						showToast(getString(R.string.no_data));
					else {
						showToast(getString(R.string.nodata));
					}
				}
			}

		}else{
			showToast(bean.getMessage());
		}
		reFreshOrLoadComplete();
	}
	private void updateConditions(List<ConditionBean> condits) {
		if(condits==null)return;
		mylist.addAll(condits);
		adapter.notifyDataSetChanged(mylist);
	}

	/**
	 * 刷新或加载完后调用此方法
	 */
	public void reFreshOrLoadComplete()
	{
		if(isLoadMore)
		{
			 lst_labels.onPullUpRefreshComplete();
			 lst_labels.setHasMoreData(true);
		}
		else if(isRefresh)
		{
			lst_labels.onPullDownRefreshComplete();
			lst_labels.setLastUpdatedLabel(com.example.casting.util.view.Util.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}
}
