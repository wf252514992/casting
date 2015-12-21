package com.example.casting.me2;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Me2Activity extends BaseForm implements OnClickListener,
OnPageChangeListener{
	View titleLayout;
	ViewPager viewPager;
	TextView txt_dynamic;
	TextView txt_attention;
	TextView txt_fans;
	TextView txt_edit;
	ImageView img_pic ;
	UserGetProcessor usergetPro;
	GetWorksProcessor getworkPro;
	GetCountProcessor countPro;
	WinnerGetListProcessor getwinnerPro;
	MyWorksView workview;
	MyBaseInfoView infoview;
	MyAchievementsView achievementview;
	MyselfPlayView selfplayview;
	
	/** 装分页显示的view的数组 */
	private ArrayList<View> pageViews = new ArrayList<View>();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me2);
		init();
	}
	protected void init( ) {
		titleLayout = (View)findViewById(R.id.titlelayout);
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		img_pic = (ImageView)findViewById(R.id.img_pic);
		txt_dynamic = (TextView)findViewById(R.id.txt_dynamic);
		txt_attention = (TextView)findViewById(R.id.txt_attention);
		txt_edit = (TextView)findViewById(R.id.txt_edit);
		txt_fans = (TextView)findViewById(R.id.txt_fans);
		initView(titleLayout);
		setRightButtonAble(true, "设置");
		setLeftButtonAble(false, "");
		setTitle("我");
		initView();
	}
	private void initView() {
		img_pic.setOnClickListener(this);
		txt_dynamic.setOnClickListener(this);
		txt_attention.setOnClickListener(this);
		txt_edit.setOnClickListener(this);
		txt_fans.setOnClickListener(this);
		infoview = new MyBaseInfoView(this,null);
		workview = new MyWorksView(this);
		selfplayview = new MyselfPlayView(this);
		achievementview = new MyAchievementsView(this);
		initPages() ;
		// 设置Adapter
		viewPager.setAdapter(new MyAdapter(pageViews));
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);

	}
	
	private void initPages(){
		getMyInfofromService();
		pageViews.add(infoview.getView());
		pageViews.add(workview.getView());
		pageViews.add(achievementview.getView());
		pageViews.add(selfplayview.getView());
	}
	
	
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
		HttpCall(false,countPro, Session.getInstance().getUser_id());
		/**
		 * 获取用户基本信息
		 */
		HttpCall(false,usergetPro, registbean);
		/**
		 * 获取用户作品信息
		 */
		HttpCall(false,getworkPro, 1);
		/**
		 * 用户获奖经历
		 */
		HttpCall(getwinnerPro, 1);
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
		doActivity(R.string.xtszactivity);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
