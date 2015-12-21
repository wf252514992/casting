package com.example.casting.me;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.adapter.MyAdapter;
import com.example.casting.me.view.MyAchievementsView;
import com.example.casting.me.view.MyBaseInfoView;
import com.example.casting.me.view.MyWorksView;
import com.example.casting.me.view.MyselfPlayView;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.attention.GetCountProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.winners.WinnerGetListProcessor;
import com.example.casting.processor.works.GetWorksProcessor;
import com.example.casting.util.Session;
import com.example.casting_android.R;

public class MyInfoActivity extends BaseForm implements OnClickListener,
		OnPageChangeListener {
	View titleLayout;
	ViewPager viewPager;
	ImageView imgbtn_edtInfo;
	ImageView imgbtn_left;
	ImageView imgbtn_right;
	LinearLayout bottonlayout;
	/** 装分页显示的view的数组 */
	private ArrayList<View> pageViews = new ArrayList<View>();;
	MyWorksView workview;
	MyBaseInfoView infoview;
	MyAchievementsView achievementview;
	MyselfPlayView selfplayview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo);
		init();
	}
	protected void init( ) {
		titleLayout = (View)findViewById(R.id.titlelayout);
		viewPager = (ViewPager)findViewById(R.id.viewFilpper);
		imgbtn_edtInfo = (ImageView)findViewById(R.id.btn_edtInfo);
		imgbtn_left = (ImageView)findViewById(R.id.btn_left);
		imgbtn_right = (ImageView)findViewById(R.id.btn_right);
		bottonlayout = (LinearLayout)findViewById(R.id.bottonlayout);
		initView(titleLayout);
		setRightButtonAble(true, "设置");
		setLeftButtonAble(false, "");
		setTitle("我");
		initView();
	}

	private void initView() {
		imgbtn_left.setOnClickListener(this);
		imgbtn_right.setOnClickListener(this);
		imgbtn_edtInfo.setOnClickListener(this);
		infoview = new MyBaseInfoView(this,baseInfolistener);
		workview = new MyWorksView(this);
		selfplayview = new MyselfPlayView(this);
		achievementview = new MyAchievementsView(this);
		initPages() ;
		// 设置Adapter
		viewPager.setAdapter(new MyAdapter(pageViews));
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);

	}
	MyBaseInfoView.OnBaseInfoClick baseInfolistener = new MyBaseInfoView.OnBaseInfoClick() {
		
		public void onAttentionLayoutClick() {
			doActivity(R.string.attentionlistactivity);
		};
		@Override
		public void onfansLayoutClick() {
			// TODO Auto-generated method stub
			doActivity(R.string.myfansactivity);
		}
	};
	

	private void initPages(){
		getMyInfofromService();
		pageViews.add(infoview.getView());
		pageViews.add(workview.getView());
		pageViews.add(achievementview.getView());
		pageViews.add(selfplayview.getView());
		addTips();
	}
	private void addTips() {
		LinearLayout ll = new LinearLayout(this);
		int k = viewPager.getCurrentItem();
		for (int i = 0; i < pageViews.size(); i++) {
			ImageView image = new ImageView(this);
			if (i == k)
				image.setImageResource(R.drawable.mine_page_dothigh);
			else
				image.setImageResource(R.drawable.mine_page_dot);
			image.setPadding(3, 0, 3, 0);
			ll.addView(image);
		}
		bottonlayout.removeAllViews();
		bottonlayout.addView(ll);
	}
	UserGetProcessor usergetPro;
	GetWorksProcessor getworkPro;
	GetCountProcessor countPro;
	WinnerGetListProcessor getwinnerPro;
	/**
	 * 获取我的资料
	 */
	private void getMyInfofromService(){
		//获取关注fans数
		RegistBean registbean = new RegistBean();
		registbean.setId(Session.getInstance().getUser_id());
		usergetPro = new UserGetProcessor();
		getworkPro = new GetWorksProcessor();
		countPro = new GetCountProcessor();
		getwinnerPro = new WinnerGetListProcessor();
		/**
		 * 获取 动态数，粉丝数，
		 */
		HttpCall(false,countPro,registbean);
		/**
		 * 获取用户基本信息
		 */
		HttpCall(false,usergetPro, registbean);
		/**
		 * 获取用户作品信息
		 */
		HttpCall(false,getworkPro, registbean);
		/**
		 * 用户获奖经历
		 */
		HttpCall(getwinnerPro, registbean);
	}
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if(result.getCode() == Errors.OK){
			if(processor.getMethod().equals(usergetPro.getMethod())){
				RegistBean regist = (RegistBean)result.getObj();
				infoview.updateView(regist);
			}else if(processor.getMethod().equals(countPro.getMethod())){
				MyCountBean countbean = (MyCountBean)result.getObj();
				infoview.updateCount(countbean);
			}else if(processor.getMethod().equals(getworkPro.getMethod())){
				List<WorkBean> works = (List)result.getObj();
				workview.updateView(works);
			}else if(processor.getMethod().equals(getwinnerPro.getMethod())){
				List<WinnerBean> winners = (List)result.getObj();
				achievementview.updateView(winners);
			}
		}else{
			showToast(result.getMessage());
		}
		
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		// 系统设置
		doActivity(R.string.xtszactivity);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		doActivity(R.string.mydataactivity);
		if(arg0.getId() == R.id.btn_left){
			int k = viewPager.getCurrentItem();
			if(k>0){
				k=k-1;
			}
			viewPager.setCurrentItem(k);
		}else if(arg0.getId() == R.id.btn_right){
			int k = viewPager.getCurrentItem();
			if(k<pageViews.size()-1){
				k=k+1;
			}
			viewPager.setCurrentItem(k);
		}
		getMyInfofromService();
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		addTips();
	}
}
