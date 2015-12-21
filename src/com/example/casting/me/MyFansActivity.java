package com.example.casting.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.login.SureDialogForm;
import com.example.casting.me.adapter.AttentionAdapter;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.attention.AddAttentionProcessor;
import com.example.casting.processor.attention.DelAttentionProcessor;
import com.example.casting.processor.attention.GetFansProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting.util.view.refresh.PullToRefreshListView;
import com.example.casting_android.R;

public  class MyFansActivity extends SureDialogForm implements OnItemClickListener {
	PullToRefreshListView lst_fans;
	ListView simpleListView;
	View titleLayout;
	ArrayList<AttentionBean> fans = new ArrayList<AttentionBean>();
	AttentionAdapter adapter;
	AddAttentionProcessor addationprocessor = new AddAttentionProcessor();
	DelAttentionProcessor delationPro = new DelAttentionProcessor();
	boolean isEdit = false;
	boolean isRefresh = false;
	boolean isLoadMore = false;
	AttentionBean current_attentionBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fanslist);
		titleLayout = (View) findViewById(R.id.titlelayout);
		lst_fans = (PullToRefreshListView) findViewById(R.id.lst_fans);
		lst_fans.setScrollLoadEnabled(true);
		simpleListView = lst_fans.getRefreshableView();
		initView(titleLayout);
		setTitle("粉丝");
		setLeftButtonAble(true, "返回");
		adapter = new AttentionAdapter(this, fans) {

			@Override
			public void addAttention(AttentionBean bean) {
				// TODO Auto-generated method stub
				if (isEdit) {
					if (bean != null) {
						//status:0未关注 1：已关注 2：互相关注
						if(bean.getStatue() == 0)
						{
							AttentionBean attentionbean = new AttentionBean();
							attentionbean.setFollowers_id(bean.getBy_follower_id());
							attentionbean.setBy_follower_id(bean.getFollowers_id());
							HttpCall(addationprocessor, attentionbean, "str");
						}
						else {
							current_attentionBean=bean;
							setPopWindowTitle("是否取消关注?");
							showSelectPicDialog();
							
						}
					}
				}

			}

		};
		simpleListView.setAdapter(adapter);
		simpleListView.setOnItemClickListener(this);
		initData(getIntent());
		initRefreshListener();
	}

	public void initRefreshListener() {
		lst_fans.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = true;
				currentPageNum = 1;
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				System.out.println("---上拉加载更多------");
				isLoadMore = true;
				requestData(true);
			}

		});
		lst_fans.setLastUpdatedLabel(com.example.casting.util.view.Util
				.getCurrentTime());
		lst_fans.doPullRefreshing(true, 500);
	}

	BaseBean bean = null;

	private void initData(Intent intent) {
		if (intent != null && intent.getExtras() != null) {
			Bundle bd = intent.getExtras();
			if (bd.getSerializable(R.string.myfansactivity + "") != null) {
				bean = (BaseBean) bd.getSerializable(R.string.myfansactivity
						+ "");
			}
		}
		if (bean != null
				&& bean.getId().equals(Session.getInstance().getUser_id())) {
			isEdit = true;
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getFans(bean);
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {

	}

	public void requestData(boolean isLoadMore) {

		BaseBean baseBean = bean;
		if (isLoadMore) {
			baseBean.setPagenum(currentPageNum + 1 + "");
			getFans(baseBean);
		} else {
			if (isRefresh) {
				currentPageNum = 1;
			}
			baseBean.setPagenum(currentPageNum + "");
			getFans(baseBean);
		}
	}

	GetFansProcessor fanspro = new GetFansProcessor();

	private void getFans(BaseBean bean) {
		if (bean == null) {
			showToast("失败");
			return;
		}
		HttpCall(false, fanspro, bean);
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (result.getCode() == Errors.OK) {
			if (processor.getMethod().equals(fanspro.getMethod())) {
				List<AttentionBean> list = (List) result.getObj();
				if (list != null && list.size() > 0) {
					if (isLoadMore) {
						currentPageNum++;
					} else {
						fans.clear();
					}
					fans.addAll(list);
					adapter.notifyDataSetChanged(fans);
				} else {
					if (isLoadMore)
						showToast(getString(R.string.no_data));
					else
					{
						showToast(getString(R.string.nodata));
						fans.clear();
						adapter.notifyDataSetChanged(fans);
					}
				}

			} else if (processor.getMethod().equals(
					addationprocessor.getMethod())) {
				// 关注成功
				showToast("关注成功");
				getFans(bean);
			}
			else if (processor.getProcessorId().equals(
					delationPro.getProcessorId())) {
				requestData(false);
			}
		} else {
			showToast("失败");
		}
		reFreshOrLoadComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Object obj = arg0.getItemAtPosition(arg2);
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean atten = (AttentionBean) obj;
			RegistBean registbean = new RegistBean();
			registbean.setId(atten.getFollowers_id());
			registbean.setType(atten.getType());
			registbean.setNickname(atten.getNickname());
			Bundle bd = new Bundle();
			if (registbean.getId().equals(Session.getInstance().getUser_id())) {
				ListenerManager.notifyTabHostChange();
			} else {
				int resourceid = R.string.otherwatchmeactivity;
				if (registbean.getType().equals(
						ConstantData.Login_Type_Director)) {
					resourceid = R.string.otherwatchdirectoractivity;
					bd.putSerializable(resourceid + "", registbean);
					doActivity(resourceid, bd);

				} else if (registbean.getType().equals(
						ConstantData.Login_Type_Nomal)) {
					resourceid = R.string.otherwatchmeactivity;
					bd.putSerializable(resourceid + "", registbean);
					doActivity(resourceid, bd);
				} else if (registbean.getType().equals(
						ConstantData.Login_Type_Company)) {
					resourceid = R.string.otherwatchcompanyactivity;
					bd.putSerializable(resourceid + "", registbean);
					doActivity(resourceid, bd);
				}
			}
		}

	}

	// boolean isLoading = false, isrefresh = false;
	int currentPageNum = 1;

	/**
	 * 刷新或加载完后调用此方法
	 */
	public void reFreshOrLoadComplete() {
		if (isLoadMore) {
			lst_fans.onPullUpRefreshComplete();
			lst_fans.setHasMoreData(true);
		} else if (isRefresh) {
			lst_fans.onPullDownRefreshComplete();
			lst_fans.setLastUpdatedLabel(com.example.casting.util.view.Util
					.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}
	public void delAttention(AttentionBean bean)
	{
		//取消关注
		AttentionBean attentionbean = new AttentionBean();
		attentionbean.setFollowers_id(bean.getBy_follower_id());
		attentionbean.setBy_follower_id(bean.getFollowers_id());
		HttpCall(delationPro, attentionbean);
	}

	@Override
	public void sure() {
		//取消关注
		AttentionBean attentionbean = new AttentionBean();
		attentionbean.setFollowers_id(current_attentionBean.getBy_follower_id());
		attentionbean.setBy_follower_id(current_attentionBean.getFollowers_id());
		HttpCall(delationPro, attentionbean);
	}

	@Override
	public void cancle() {
		dismissDialog();
	}

	@Override
	public View getParentView() {
		return lst_fans;
	}
}
