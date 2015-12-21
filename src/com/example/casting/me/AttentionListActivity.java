package com.example.casting.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
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
import com.example.casting.processor.attention.DelAttentionProcessor;
import com.example.casting.processor.attention.GetAttentionProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting.util.view.refresh.PullToRefreshListView;
import com.example.casting_android.R;

public class AttentionListActivity extends SureDialogForm implements
		OnItemClickListener {
	View titleLayout;
	PullToRefreshListView lstview_attention;
	ListView simpleListView;
	GetAttentionProcessor attentionpro = new GetAttentionProcessor();
	ArrayList<AttentionBean> users = new ArrayList<AttentionBean>();
	AttentionAdapter adapter;
	DelAttentionProcessor delationPro = new DelAttentionProcessor();
	boolean isEdit = false;
	boolean isRefresh = false;
	boolean isLoadMore = false;
	AttentionBean currentAttentionBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attentionlist);
		titleLayout = (View) findViewById(R.id.titlelayout);
		lstview_attention = (PullToRefreshListView) findViewById(R.id.lstview_attention);
		simpleListView = lstview_attention.getRefreshableView();
		lstview_attention.setScrollLoadEnabled(true);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("全部关注");
		adapter = new AttentionAdapter(this, users) {
			@Override
			public void addAttention(AttentionBean bean) {
				currentAttentionBean = bean;
				setPopWindowTitle("是否取消关注？");
				showSelectPicDialog();
				// TODO Auto-generated method stub
//				if (isEdit) {
//					if (bean != null) {
//						HttpCall(delationPro, bean);
//					}
//				}
			}
		};
		simpleListView.setAdapter(adapter);
		simpleListView.setOnItemClickListener(this);
		initData(getIntent());
		initRefreshListener();
	}

	public void initRefreshListener() {
		lstview_attention
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

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
		lstview_attention
				.setLastUpdatedLabel(com.example.casting.util.view.Util
						.getCurrentTime());
		lstview_attention.doPullRefreshing(true, 500);
	}

	public void requestData(boolean isLoadMore) {
		BaseBean baseBean = bean;
		if (isLoadMore)
			baseBean.setPagenum(currentPageNum + 1 + "");
		else {
			if (isRefresh)
				currentPageNum = 1;
			baseBean.setPagenum(currentPageNum + "");
		}
		getAttention(baseBean);
	}

	BaseBean bean = null;

	private void initData(Intent intent) {
		if (intent != null && intent.getExtras() != null) {
			Bundle bd = intent.getExtras();
			if (bd.getSerializable(R.string.attentionlistactivity + "") != null) {
				bean = (BaseBean) bd
						.getSerializable(R.string.attentionlistactivity + "");
			}
		}
		if (bean != null
				&& bean.getId().equals(Session.getInstance().getUser_id())) {
			isEdit = true;
		}
	}

	private void getAttention(BaseBean bean) {
		if (bean != null)
			HttpCall(false, attentionpro, bean);
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (result.getCode() == Errors.OK) {
			if (processor.getMethod().equals(attentionpro.getMethod())) {
				List<AttentionBean> list = (List) result.getObj();
				if (list != null && list.size() > 0) {
					if (isLoadMore) {
						currentPageNum++;
					} else {
						users.clear();
					}
					users.addAll(list);
					adapter.notifyDataSetChanged(users);
				} else {
					if (isLoadMore) {
						showToast(getString(R.string.no_data));
					} else {
						showToast(getString(R.string.nodata));
						users.clear();
						adapter.notifyDataSetChanged(users);
					}
				}
			} else if (processor.getProcessorId().equals(
					delationPro.getProcessorId())) {
				getAttention(bean);
			}
		} else {
			showToast("失败");
		}
		reFreshOrLoadComplete();
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Object obj = arg0.getItemAtPosition(arg2);
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean atten = (AttentionBean) obj;
			RegistBean registbean = new RegistBean();
			registbean.setId(atten.getBy_follower_id());
			registbean.setType(atten.getType());
			registbean.setNickname(atten.getNickname());
			registbean.setHead_portrait(atten.getHead_portrait());
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

	int currentPageNum = 1;

	/**
	 * 刷新或加载完后调用此方法
	 */
	public void reFreshOrLoadComplete() {
		if (isLoadMore) {
			lstview_attention.onPullUpRefreshComplete();
			lstview_attention.setHasMoreData(true);
		} else if (isRefresh) {
			lstview_attention.onPullDownRefreshComplete();
			lstview_attention
					.setLastUpdatedLabel(com.example.casting.util.view.Util
							.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}

	@Override
	public void sure() {
		if (isEdit) {
			if (bean != null) {
				HttpCall(delationPro, currentAttentionBean);
			}
		}
	}

	@Override
	public void cancle() {
		dismissDialog();
	}

	@Override
	public View getParentView() {
		return lstview_attention;
	}
}
