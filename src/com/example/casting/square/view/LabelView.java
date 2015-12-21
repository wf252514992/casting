package com.example.casting.square.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.gui.PullToRefreshView;

import com.example.casting.entity.ConditionBean;
import com.example.casting.entity.PlazaBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.square.adapter.HotAdapter;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshListView;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting_android.R;

public class LabelView implements OnClickListener, OnItemClickListener {
	HotAdapter hotAdapter;
	View baseView;
	Context mContext;
	ImageView img_left;
	TextView txt_show;
	PullToRefreshListView lstview_label;
	ListView simpleListView;
	ArrayList<PlazaBean> users = new ArrayList<PlazaBean>();
	ConditionBean condition;
	LabelViewListener mylistener;
	boolean isRefresh = false;
	boolean isLoadMore = false;
	static int current_position = -1;
	public LabelView(Context ctx, LabelViewListener listener) {
		this.mContext = ctx;
		this.mylistener = listener;
		initView();
	}

	public View getView(ConditionBean bean) {
		condition = bean;
		if (bean != null) {
			txt_show.setText(condition.getLabel());
			mylistener.search(bean, true);
		}
		return baseView;

	}

	public void initRefreshListener() {
		lstview_label.setOnRefreshListener(new OnRefreshListener<ListView>() {

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
		lstview_label.setLastUpdatedLabel(com.example.casting.util.view.Util
				.getCurrentTime());
		lstview_label.doPullRefreshing(true, 500);
	}

	public void setHotList(List<PlazaBean> beanlist) {
		// 加载更多的时候，不清除已有数据
		if (beanlist != null && beanlist.size() > 0) {
			// 加载更多的时候，不清除已有数据
			if (isLoadMore) {
				currentPageNum++;
			} else {
				users.clear();
			}
			users.addAll(beanlist);
			hotAdapter.notifyDataSetChanged();
		} else {
			if (isLoadMore) {
				Toast.makeText(mContext, mContext.getString(R.string.no_data),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.nodata),
						Toast.LENGTH_SHORT).show();
			}
		}
		reFreshOrLoadComplete();
	}

	private void initView() {
		baseView = LayoutInflater.from(mContext).inflate(R.layout.square_label,
				null);
		img_left = (ImageView) baseView.findViewById(R.id.img_left);
		txt_show = (TextView) baseView.findViewById(R.id.txt_show);
		lstview_label = (PullToRefreshListView) baseView
				.findViewById(R.id.lstview_label);
		simpleListView = lstview_label.getRefreshableView();
		lstview_label.setScrollLoadEnabled(true);
		hotAdapter = new HotAdapter(mContext, users) {

			@Override
			public void addAttention(PlazaBean bean,int position) {
				// TODO Auto-generated method stub
				if (bean.getState() == 0
						&& !bean.getId().equals(
								Session.getInstance().getUser_id()))
					mylistener.addAttention(bean);
				    current_position = position;
			}
		};
		simpleListView.setAdapter(hotAdapter);
		simpleListView.setOnItemClickListener(this);
		// lstview_label.setOnRefreshListener(this);
		// lstview_label.setOnLoadListener(this);
		img_left.setOnClickListener(this);
		initRefreshListener();

	}

	boolean isLoading = false, isrefresh = false;
	int currentPageNum = 1;

	/** 请求数据 isLoadMore:是否是加载更多的请求 */
	public void requestData(boolean isLoadMore) {
		ConditionBean bean;
		if (condition != null)
			bean = condition;
		else
			bean = new ConditionBean();
		if (isLoadMore) {
			bean.setPagenum(currentPageNum + 1 + "");
		} else {
			bean.setPagenum(currentPageNum + "");
		}
		mylistener.search(condition, false);
	}

	/** 上拉加载更多 */
	// @Override
	// public void onLoad() {
	// if (isLoading)
	// return;
	// isLoading = true;
	// currentPageNum++;
	// condition.setPagenum(currentPageNum+"");
	// mylistener.search(condition, false);
	//
	// }
	//
	// /** 下拉刷新 */
	// @Override
	// public void onRefresh() {
	// if (isrefresh)
	// return;
	// currentPageNum = 1;
	// isrefresh = true;
	// condition.setPagenum(currentPageNum+"");
	// mylistener.search(condition, false);
	// }
	private boolean ing = false;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (!ing) {
			ing = true;
			Object obj = arg0.getItemAtPosition(arg2);
			if (obj != null && obj instanceof PlazaBean) {
				PlazaBean user = (PlazaBean) obj;
				RegistBean registbean = new RegistBean();
				registbean.setId(user.getId());
				registbean.setType(user.getType());
				registbean.setNickname(user.getNickname());
				Bundle bd = new Bundle();
				if (user.getId().equals(Session.getInstance().getUser_id())) {
					ListenerManager.notifyTabHostChange();
				} else {
					int resourceid = R.string.otherwatchmeactivity;
					if (registbean.getType().equals(
							ConstantData.Login_Type_Director)) {
						resourceid = R.string.otherwatchdirectoractivity;
						bd.putSerializable(resourceid + "", registbean);
						// doActivity(resourceid, bd);
						mylistener.jump(resourceid, bd);
					} else if (registbean.getType().equals(
							ConstantData.Login_Type_Nomal)) {
						resourceid = R.string.otherwatchmeactivity;
						bd.putSerializable(resourceid + "", registbean);
						// doActivity(resourceid, bd);
						mylistener.jump(resourceid, bd);
					} else if (registbean.getType().equals(
							ConstantData.Login_Type_Company)) {
						resourceid = R.string.otherwatchcompanyactivity;
						bd.putSerializable(resourceid + "", registbean);
						// doActivity(resourceid, bd);
						mylistener.jump(resourceid, bd);
					}
				}
			}
			ing = false;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.img_left) {
			mylistener.changeView();
		}
	}

	public interface LabelViewListener {
		/**
		 * 更改 布局
		 */
		public void changeView();

		/**
		 * 搜索最新
		 * 
		 * @param bean
		 * @param wait
		 */
		public void search(ConditionBean bean, boolean wait);

		/**
		 * 跳转下一个页面
		 * 
		 * @param nextid
		 * @param bd
		 */
		public void jump(int nextid, Bundle bd);

		/**
		 * 新增关注
		 * 
		 * @param bean
		 */
		public void addAttention(PlazaBean bean);

	}

	/**
	 * 刷新或加载完后调用此方法
	 */
	public void reFreshOrLoadComplete() {
		if (isLoadMore) {
			lstview_label.onPullUpRefreshComplete();
			lstview_label.setHasMoreData(true);
		} else if (isRefresh) {
			lstview_label.onPullDownRefreshComplete();
			lstview_label
					.setLastUpdatedLabel(com.example.casting.util.view.Util
							.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}
		public void notifyDataChage()
		{
			PlazaBean bean = users.get(current_position);
			bean.setState("1");
			users.remove(current_position);
			users.add(current_position,bean);
			hotAdapter.notifyDataSetChanged();
		}
}
