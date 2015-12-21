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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casting.entity.ConditionBean;
import com.example.casting.entity.PlazaBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.square.adapter.HotAdapter;
import com.example.casting.square.adapter.LabelAdapter;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting.util.view.Util;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting.util.view.refresh.PullToRefreshListView;
import com.example.casting_android.R;

public class HotView implements OnClickListener, OnCheckedChangeListener,
		OnItemClickListener {
	HotAdapter hotAdapter;
	LabelAdapter labelsAdapter;
	View baseView;
	Context mContext;
	PullToRefreshListView lstview_hot;
	ListView simpleListView;
	RadioGroup group_hot;
	RadioButton rdo_hot;
	RadioButton rdo_age;
	RadioButton rdo_sex;
	RadioButton rdo_label;
	TextView txt_show;
	LinearLayout subhotlayout;
	ImageView img_right, img_left;
	ArrayList<ConditionBean> agelist = new ArrayList<ConditionBean>();
	ArrayList<ConditionBean> sexlist = new ArrayList<ConditionBean>();
	ArrayList<ConditionBean> labellist = new ArrayList<ConditionBean>();
	ArrayList<PlazaBean> users = new ArrayList<PlazaBean>();
	HotViewListener mylistener;

	boolean isRefresh = false;
	boolean isLoadMore = false;

	static int current_position = -1;
	public HotView(Context ctx, HotViewListener listener) {
		this.mContext = ctx;
		this.mylistener = listener;
		initView();
	}

	public View getView() {
		return baseView;
	}

	private void initView() {
		baseView = LayoutInflater.from(mContext).inflate(R.layout.square_hot,
				null);
		img_left = (ImageView) baseView.findViewById(R.id.img_left);
		txt_show = (TextView) baseView.findViewById(R.id.txt_show);
		group_hot = (RadioGroup) baseView.findViewById(R.id.group_hot);
		rdo_hot = (RadioButton) baseView.findViewById(R.id.rdo_hot);
		rdo_age = (RadioButton) baseView.findViewById(R.id.rdo_age);
		rdo_sex = (RadioButton) baseView.findViewById(R.id.rdo_sex);
		rdo_label = (RadioButton) baseView.findViewById(R.id.rdo_label);
		
		rdo_hot.setCompoundDrawables(Util.setDrawable(mContext,R.drawable.clist_icon_hot,20, 20),null, null, null);
		rdo_age.setCompoundDrawables(Util.setDrawable(mContext,R.drawable.clist_icon_age,20, 20),null, null, null);
		rdo_sex.setCompoundDrawables(Util.setDrawable(mContext,R.drawable.clist_icon_sex,20, 20),null, null, null);
		rdo_label.setCompoundDrawables(Util.setDrawable(mContext,R.drawable.clist_icon_sign,20, 20),null, null, null);
		
		lstview_hot = (PullToRefreshListView) baseView
				.findViewById(R.id.lstview_hot);
		lstview_hot.setScrollLoadEnabled(true);

		subhotlayout = (LinearLayout) baseView.findViewById(R.id.subhotlayout);
		txt_show = (TextView) baseView.findViewById(R.id.txt_show);
		img_right = (ImageView) baseView.findViewById(R.id.img_right);
		img_left = (ImageView) baseView.findViewById(R.id.img_left);
		img_right.setOnClickListener(this);
		img_left.setOnClickListener(this);
		group_hot.setOnCheckedChangeListener(this);
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
		simpleListView = lstview_hot.getRefreshableView();
		simpleListView.setAdapter(hotAdapter);
		labelsAdapter = new LabelAdapter(mContext, labellist);
		simpleListView.setOnItemClickListener(this);
		initRefreshListener();

	}

	public void initRefreshListener() {
		lstview_hot.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = true;
				currentPageNum = 1;
				requestData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				System.out.println("---上拉加载更多------");
				isLoadMore = true;
				requestData();
			}

		});
		lstview_hot.setLastUpdatedLabel(com.example.casting.util.view.Util
				.getCurrentTime());
		freshCurrent(500);
	}

	/** 请求数据 isLoadMore:是否是加载更多的请求 */
	public void requestData() {
		if (checkedid == R.id.rdo_label) {
			if (isLoadMore) {
				mylistener.loadConditions(currentPageNum + 1);
			} else {
				mylistener.loadConditions(currentPageNum);
			}

		} else {
			mylistener.search(getConditions(), false);
		}
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.img_left) {
			if (checkedid == R.id.rdo_age) {
				if (positions[0] > 0) {
					positions[0] = positions[0] - 1;
				}
			} else if (checkedid == R.id.rdo_sex) {
				if (positions[1] > 0) {
					positions[1] = positions[1] - 1;
				}
			}
			setConditionsText();
			ConditionBean value = getConditions();
			mylistener.search(value, false);
		} else if (arg0.getId() == R.id.img_right) {
			if (checkedid == R.id.rdo_age) {
				if (positions[0] < agelist.size() - 1) {
					positions[0] = positions[0] + 1;
				}
			} else if (checkedid == R.id.rdo_sex) {
				if (positions[1] < sexlist.size() - 1) {
					positions[1] = positions[1] + 1;
				}
			}
			setConditionsText();
			ConditionBean value = getConditions();
			mylistener.search(value, false);
		}
	}

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
				registbean.setHead_portrait(user.getHead_portrait());
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
			} else if (obj != null && obj instanceof ConditionBean) {
				// 标签
				ConditionBean bean = (ConditionBean) obj;
				mylistener.changeView(bean);
			}
			ing = false;
		}

	}

	int currentPageNum = 1;
	int checkedid = R.id.rdo_hot;
	int[] positions = { 0, 0 };

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		currentPageNum = 1;
		checkedid = arg0.getCheckedRadioButtonId();
		if (checkedid == R.id.rdo_age) {
			subhotlayout.setVisibility(View.VISIBLE);
		} else if (checkedid == R.id.rdo_sex) {
			subhotlayout.setVisibility(View.VISIBLE);
		} else if (checkedid == R.id.rdo_label) {
			simpleListView.setAdapter(labelsAdapter);
			subhotlayout.setVisibility(View.GONE);
			freshCurrent(500);
			// mylistener.loadConditions(currentPageNum);
			return;
		} else if (checkedid == R.id.rdo_hot) {
			subhotlayout.setVisibility(View.GONE);
		}
		simpleListView.setAdapter(hotAdapter);
		setConditionsText();
		ConditionBean value = getConditions();
		freshCurrent(0);
	}

	public void freshCurrent(int time){
		lstview_hot.doPullRefreshing(true, time);
	}
	private void refresh() {
		if (checkedid == R.id.rdo_label) {
			mylistener.loadConditions(currentPageNum);
		} else {
			mylistener.search(getConditions(), false);
		}

	}

	// boolean isLoading = false, isrefresh = false;

	// /** 上拉加载更多 */
	// @Override
	// public void onLoad() {
	// if (isLoading)
	// return;
	// currentPageNum++;
	// refresh();
	// isLoading = true;
	// }
	//
	// /** 下拉刷新 */
	// @Override
	// public void onRefresh() {
	// if (isrefresh)
	// return;
	// currentPageNum = 1;
	// isrefresh = true;
	// refresh();
	// }

	/** 请求数据成功时刷新列表 */
	public void setHotList(List<PlazaBean> beanlist) {
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

	/**
	 * 展示 查询 选择项
	 */
	private void setConditionsText() {
		String values = "";
		ConditionBean bean = getConditions();
		if (bean != null) {
			if (bean.getType().equals("1")) {
				values = bean.getLabel();
			} else if (bean.getType().equals("3")) {
				values = bean.getSex();
			} else if (bean.getType().equals("2")) {
				values = bean.getLabel();
			}
		}
		txt_show.setText(values);
	}

	private ConditionBean getConditions() {
		ConditionBean bean = null;
		if (checkedid == R.id.rdo_age) {
			// 年龄
			if (agelist.size() > positions[0]) {
				bean = agelist.get(positions[0]);
			}
		} else if (checkedid == R.id.rdo_sex) {
			// 性别
			if (sexlist.size() > positions[1]) {
				bean = sexlist.get(positions[1]);
			}
		} else if (checkedid == R.id.rdo_label) {
			// 标签
		} else {
			// 热度
			bean = new ConditionBean();
		}
		if (bean != null)
			if (isLoadMore)
				bean.setPagenum(currentPageNum + 1 + "");
			else
				bean.setPagenum(currentPageNum + "");
		return bean;
	}

	/**
	 * 设置标签列表
	 * 
	 * @param condits
	 */
	public void setLabelConditions(List<ConditionBean> condits) {
		// if (!isLoading)
		// labellist.clear();
		// for (ConditionBean bean : condits) {
		// /**
		// * ages时间段(1)labels标签(2),不传为所有
		// */
		// if (bean.getType().equals("2")) {
		// labellist.add(bean);
		// }
		// }
		// labelsAdapter.notifyDataSetChanged(labellist);

		if (condits != null && condits.size() > 0) {
			// 加载更多的时候，不清除已有数据
			if (isLoadMore) {
				currentPageNum++;
			} else {
				labellist.clear();
			}
			for (ConditionBean bean : condits) {
				/**
				 * ages时间段(1)labels标签(2),不传为所有
				 */
				if (bean.getType().equals("2")) {
					labellist.add(bean);
				}
			}
			labelsAdapter.notifyDataSetChanged(labellist);
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

	public void setAgeConditions(List<ConditionBean> condits) {
		agelist.clear();
		if (condits != null)
			agelist.addAll(condits);
		setConditionsText();
	}

	public void setSexConditions(List<ConditionBean> condits) {
		sexlist.clear();
		if (condits != null)
			sexlist.addAll(condits);
		setConditionsText();
	}

	public interface HotViewListener {
		/**
		 * 查询 条件下的 用户
		 * 
		 * @param conditbean
		 */
		public void search(ConditionBean conditbean, boolean wait);

		/**
		 * 查询 标签
		 */
		public void loadConditions(int page);

		/**
		 * 新增关注
		 * 
		 * @param bean
		 */
		public void addAttention(PlazaBean bean);

		/**
		 * 标签和榜单切换
		 */
		public void changeView(ConditionBean bean);

		/**
		 * 下一个界面
		 */
		public void jump(int nextid, Bundle bd);
	}

	/**
	 * 刷新或加载完后调用此方法
	 */
	public void reFreshOrLoadComplete() {
		if (isLoadMore) {
			lstview_hot.onPullUpRefreshComplete();
			lstview_hot.setHasMoreData(true);
		} else if (isRefresh) {
			lstview_hot.onPullDownRefreshComplete();
			lstview_hot.setLastUpdatedLabel(com.example.casting.util.view.Util
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
