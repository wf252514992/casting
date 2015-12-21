package com.example.casting.login.regist.winner;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
import com.example.casting.login.regist.winner.adapter.WinnersAdapter;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.winners.DelWinnerProcessor;
import com.example.casting.processor.winners.WinnerGetListProcessor;
import com.example.casting.util.Session;
import com.example.casting.util.view.MyListView.OnLoadListener;
import com.example.casting.util.view.MyListView.OnRefreshListener;
import com.example.casting_android.R;
import com.ui.baoyz.swipemenulistview.SwipeMenu;
import com.ui.baoyz.swipemenulistview.SwipeMenuCreator;
import com.ui.baoyz.swipemenulistview.SwipeMenuItem;
import com.ui.baoyz.swipemenulistview.SwipeMenuListView;

public class WinnerActivity extends BaseForm implements OnItemClickListener,
		SwipeMenuListView.OnMenuItemClickListener, OnRefreshListener,
		OnLoadListener {
	View titleLayout;
	SwipeMenuListView lst_winners;
	WinnersAdapter adapter = null;
	WinnerGetListProcessor getwinnerPro = new WinnerGetListProcessor();
	ArrayList<WinnerBean> winnerlist = new ArrayList<WinnerBean>();
	RegistBean winnerBean;
	boolean isEdit = false;
	boolean isLoadMore=false,isRefresh=false;
	int currenPageNum = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_works_show);
		initSubView();
	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		titleLayout = (View) findViewById(R.id.titlelayout);
		lst_winners = (SwipeMenuListView) findViewById(R.id.lst_works);
		lst_winners.setOnItemClickListener(this);
		adapter = new WinnersAdapter(this, winnerlist,true);
		lst_winners.setAdapter(adapter);
		lst_winners.setOnRefreshListener(this);
		lst_winners.setOnLoadListener(this);
		initView(titleLayout);
		setTitle("获奖经历");
		setLeftButtonAble(true, "返回");
		initData(getIntent());

	}
	/**
	 * 获取用户获奖信息
	 */
	public void requestData()
	{
		BaseBean bean = new BaseBean();
		if(isLoadMore)
		{
		   	bean.setPagenum(currenPageNum+1+"");
		}
		else {
			if(isRefresh)
			{
				currenPageNum = 1;
			}
			bean.setPagenum(currenPageNum+"");
		}
		bean.setId(Session.getInstance().getUser_id());
		HttpCall(false,getwinnerPro, bean);
	}

	private void initData(Intent itent) {
		if (itent != null) {
			Bundle bd = itent.getExtras();
			if (bd.getSerializable(R.string.winneractivity + "") != null) {
				winnerBean =(RegistBean)  bd
						.getSerializable(R.string.winneractivity + "");
//			} else {
//				/**
//				 * 获取用户获奖信息
//				 */
//				BaseBean bean = new BaseBean();
//				bean.setId(Session.getInstance().getUser_id());
//				HttpCall(getwinnerPro, bean);
			}
		}
		
		if(winnerBean==null)return;
		winnerlist.addAll(winnerBean.getWinners());
		adapter.notifyDataSetChanged(winnerlist);
		if(winnerBean.getId().equals(Session.getInstance().getUser_id())){
			setRightButtonAble(true, "添加");
			lst_winners.setMenuCreator(swicreator);
			lst_winners.setOnMenuItemClickListener(this);
			isEdit = true;
		}
		
	}

	void setBack() {
		Intent intent = new Intent();
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.winneractivity + "", winnerlist);
		intent.putExtras(bd);
		setResult(R.string.winneractivity, intent);
		finish();

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		setBack();
	}

	private final int AddWinner = 11000;
	private final int SetWinner = 12000;

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		doActivity(R.string.winneraddactivity, AddWinner);
	}

	SwipeMenuCreator swicreator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {
			// TODO Auto-generated method stub
			SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
			// set item background
			openItem.setBackground(R.drawable.bg_edit_red_orange_8);
			// set item width
			openItem.setWidth(dp2px(80));
			// set item title
			openItem.setTitle("删除");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);
		}
	};

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (processor.getProcessorId().equals(getwinnerPro.getProcessorId())) {
			if(result.getCode() == Errors.OK)
			{
				List<WinnerBean> winners = (List) result.getObj();
				if(winners != null && winners.size()>0)
				{
					if(isLoadMore)
					{
						currenPageNum ++;
					}
					else {
						winnerlist.clear();
					}
					winnerlist.addAll(winners);
					adapter.notifyDataSetChanged(winnerlist);
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
			else
			{
			     showToast(result.getMessage());
			}
			refreshOrLoadMoreComplete();
			
			
		} else if (processor.getProcessorId()
				.equals(delWinPro.getProcessorId())) {

			if (result.getCode() == Errors.OK && currentPst > -1) {
				winnerlist.remove(currentPst);
				adapter.notifyDataSetChanged(winnerlist);
			} else {
				showToast("失败");
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AddWinner) {
			if (data != null) {
				Bundle bd = data.getExtras();
				if (resultCode == R.string.winneraddactivity) {
					WinnerBean winner = (WinnerBean) bd
							.getSerializable(resultCode + "");
					winnerlist.add(winner);
					adapter.notifyDataSetChanged(winnerlist);
				}
			}
		} else if (requestCode == SetWinner) {
			BaseBean bean = new BaseBean();
			bean.setId(Session.getInstance().getUser_id());
			HttpCall(getwinnerPro, bean);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object obj = arg0.getItemAtPosition(arg2);
		if (obj != null && obj instanceof WinnerBean) {
			WinnerBean bean = (WinnerBean)obj;
			if (isEdit) {
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.winneractivity + "", bean);
				doActivity(R.string.winneraddactivity, bd, SetWinner);

			} else {
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.winnershowactivity + "", bean);
				doActivity(R.string.winnershowactivity, bd);

			}
		}
	}

	DelWinnerProcessor delWinPro = new DelWinnerProcessor();
	int currentPst = -1;

	@Override
	public void onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		currentPst = position;
		WinnerBean win = winnerlist.get(position);
		HttpCall(delWinPro, win.getWinners_id(), "str");
	}

	@Override
	public void onLoad() {
		isLoadMore = true;
		requestData();
	}

	@Override
	public void onRefresh() {
		isRefresh=true;
		requestData();
	}
	public void refreshOrLoadMoreComplete()
	{
		if(isRefresh)
		{
			lst_winners.onRefreshComplete();
			
		}
		else if(isLoadMore)
		{
			lst_winners.onLoadComplete("");
		}
		isRefresh = false;
		isLoadMore = false;
	}
}
