package com.example.casting.login.regist.works;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.PlazaBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
import com.example.casting.login.regist.works.adapter.WorksAdapter;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.works.DelWorksProcessor;
import com.example.casting.processor.works.GetWorksProcessor;
import com.example.casting.util.Session;
import com.example.casting.util.view.MyListView.OnLoadListener;
import com.example.casting.util.view.MyListView.OnRefreshListener;
import com.example.casting_android.R;
import com.ui.baoyz.swipemenulistview.SwipeMenu;
import com.ui.baoyz.swipemenulistview.SwipeMenuCreator;
import com.ui.baoyz.swipemenulistview.SwipeMenuItem;
import com.ui.baoyz.swipemenulistview.SwipeMenuListView;

public class WorksActivity extends BaseForm implements OnItemClickListener,
		SwipeMenuListView.OnMenuItemClickListener , OnRefreshListener, OnLoadListener{
	View titleLayout;
	GetWorksProcessor getworkPro = new GetWorksProcessor();
	SwipeMenuListView lst_works;
	WorksAdapter adapter = null;
	RegistBean worksbean;
	ArrayList<WorkBean> works = new ArrayList<WorkBean>();
	boolean isEdit = false;
	boolean isLoadMore=false,isRefresh=false;
	int currenPageNum = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_works_show);
		initSubView();
		if(worksbean == null)
			worksbean = new RegistBean();

	}

	private void initData(Intent itent) {
		if (itent != null) {
			Bundle bd = itent.getExtras();
			if (bd.getSerializable(R.string.worksactivity + "") != null) {
				worksbean =(RegistBean) bd
						.getSerializable(R.string.worksactivity + "");
			} else {
				/**
				 * 获取用户作品信息
				 */
//				BaseBean bean = new BaseBean();
//				bean.setId(Session.getInstance().getUser_id());
//				HttpCall(getworkPro, bean);

			}
			if(worksbean==null)return;
			works.addAll(worksbean.getWorks());
			adapter.notifyDataSetChanged(works);
			if(worksbean.getId().equals(Session.getInstance().getUser_id())){
				lst_works.setMenuCreator(swicreator);
				lst_works.setOnMenuItemClickListener(this);
				isEdit = true;
				if (works.size() == 0)
					RightButtonClick();
			}
		}
	}
	/**
	 * 获取用户作品信息
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
		bean.setId(worksbean.getId());
		HttpCall(false,getworkPro, bean);
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

	protected void initSubView() {
		// TODO Auto-generated method stub
		titleLayout = (View) findViewById(R.id.titlelayout);
		lst_works = (SwipeMenuListView) findViewById(R.id.lst_works);
		adapter = new WorksAdapter(this, works,true);
		lst_works.setAdapter(adapter);
		lst_works.setOnItemClickListener(this);
		lst_works.setOnRefreshListener(this);
		lst_works.setOnLoadListener(this);
		initView(titleLayout);
		initData(getIntent());
		setTitle("个人作品");
		setLeftButtonAble(true, "返回");
		if (isEdit) {
			setRightButtonAble(true, "添加");
		}
	}

	void setBack() {
		Intent intent = new Intent();
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.worksactivity + "", works);
		intent.putExtras(bd);
		setResult(R.string.worksactivity, intent);
		finish();

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		setBack();
	}

	private final int AddWorks = 11000;
	private final int SetWinner = 11001;

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

		doActivity(R.string.worksaddactivity, AddWorks);
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (processor.getProcessorId().equals(getworkPro.getProcessorId())) {
			if(result.getCode() == Errors.OK)
			{
				ArrayList<WorkBean> worklist = (ArrayList) result.getObj();
				if(worklist != null && worklist.size()>0)
				{
					if(isLoadMore)
					{
						currenPageNum ++;
					}
					else {
						works.clear();
					}
					works.addAll(worklist);
					adapter.notifyDataSetChanged(works);
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
			
		} else if (processor.getProcessorId().equals(delPro.getProcessorId())) {
			if (result.getCode() == Errors.OK && currentPst > -1) {
				works.remove(currentPst);
				adapter.notifyDataSetChanged(works);
			} else {
				showToast("失败");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AddWorks) {
			if (data != null) {
				Bundle bd = data.getExtras();
				if (resultCode == R.string.worksaddactivity) {
					WorkBean work = (WorkBean) bd.getSerializable(resultCode
							+ "");
					works.add(work);
					adapter.notifyDataSetChanged(works);
				}
			}
		} else if (requestCode == SetWinner) {
			BaseBean bean = new BaseBean();
			bean.setId(Session.getInstance().getUser_id());
			HttpCall(getworkPro, bean);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Object obj = arg0.getItemAtPosition(arg2);
		if (obj != null && obj instanceof WorkBean) {
			WorkBean bean = (WorkBean)obj;
			if (isEdit) {
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.worksactivity + "", bean);
				doActivity(R.string.worksaddactivity, bd, SetWinner);
			} else {
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.worksshowactivity + "", bean);
				doActivity(R.string.worksshowactivity, bd);
			}
		}

	}

	DelWorksProcessor delPro = new DelWorksProcessor();

	int currentPst = -1;

	@Override
	public void onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		currentPst = position;
		WorkBean bean = works.get(position);
		HttpCall(delPro, bean.getWorks_id(), "str");

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
			lst_works.onRefreshComplete();
			
		}
		else if(isLoadMore)
		{
			lst_works.onLoadComplete("");
		}
		isRefresh = false;
		isLoadMore = false;
	}
}
