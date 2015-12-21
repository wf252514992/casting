package com.example.casting.xtsz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.casting.MainTabNew;
import com.example.casting.db.UserManager.UserDBBusiness;
import com.example.casting.entity.LoginBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.login.LoginAddIDForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.LoginProcess;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.ui.baoyz.swipemenulistview.SwipeMenu;
import com.ui.baoyz.swipemenulistview.SwipeMenuCreator;
import com.ui.baoyz.swipemenulistview.SwipeMenuItem;
import com.ui.baoyz.swipemenulistview.SwipeMenuListView;

public class ManagerIDsActivity extends BaseForm implements
		OnItemClickListener, CloseAllListener,OnClickListener,
		SwipeMenuListView.OnMenuItemClickListener {
	LoginProcess loginpro = new LoginProcess();
	View titleLayout;
	SwipeMenuListView lstview_ids;
	ManagerIDsAdapter adapter;
	ArrayList<RegistBean> users = new ArrayList<RegistBean>();
	RegistBean defaultbean = new RegistBean();
	UserDBBusiness userbusiness;
	ImageView imgview_photo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_idmanager);
		titleLayout = (View) findViewById(R.id.titlelayout);
		lstview_ids = (SwipeMenuListView) findViewById(R.id.lstview_ids);
		imgview_photo =  (ImageView)findViewById(R.id.imgview_photo);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		adapter = new ManagerIDsAdapter(this, users);
//		lstview_ids.setFreshlist(false);
		lstview_ids.setAdapter(adapter);
		lstview_ids.setOnItemClickListener(this);
		defaultbean.setNickname("添加账号");
		defaultbean.setId("-231");
		ImageBean imgbean = new ImageBean(ImageBean.type_res,
				R.drawable.castadd_idmanager);
		defaultbean.setUserimp(imgbean);
		setTitle("账号管理");
		initList();
		ListenerManager.addCloseListener(this);
		imgview_photo.setOnClickListener(this);
		
	}

	private void initList() {
		lstview_ids.setMenuCreator(swicreator);
		lstview_ids.setOnMenuItemClickListener(this);
		refreshList();
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object obj = arg0.getItemAtPosition(arg2);
		if (obj != null && obj instanceof RegistBean) {
			RegistBean bean = (RegistBean) obj;
			if (bean.getId().equals(Session.getInstance().getUser_id())) {
				return;
			} else if (bean.getId().equals(defaultbean.getId())) {
				// 新增账号管理
				doActivity(R.string.loginaddidform);
			} else {
				// 切换账号 重新登录
				Logining(bean);
			}
		}
	}

	@Override
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.manageridsactivity;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ListenerManager.removeCloseListener(this);
	}

	private void Logining(RegistBean registbean) {
		LoginBean bean = new LoginBean();
		bean.setPass(registbean.getPassword());
		bean.setPhone(registbean.getPhone());
		HttpCall(loginpro, bean);
	}

	public void OnReturn(String result, IBaseProcess proid) {
		super.OnReturn(result, proid);
		ResultBean bean = loginpro.json2Bean(result);
		if (bean.getCode() == Errors.OK) {
			try {
				ListenerManager.notifyCloseActivity();
				JSONObject json = (JSONObject) bean.getObj();
				String id = json.getString("id");
				String phone = json.getString("phone");
				String nickname = json.getString("nickname");
				String usertype = json.getString("type");
				String headportait = json.getString("head_portrait");
				Session.getInstance().setUser_id(id);
				Session.getInstance().setPhone(phone);
				Session.getInstance().setHeadportait(headportait);
				Session.getInstance().setName_nick(nickname);
				Session.getInstance().setUsertype(usertype);
				Session.getInstance().Save(this);
				Intent intent = new Intent(ManagerIDsActivity.this,
						MainTabNew.class);
				startActivity(intent);
				

			} catch (Exception e) {
				e.printStackTrace();
				showToast("登录失败");
			}

		} else {
			showToast(bean.getMessage());
		}
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
	public void onMenuItemClick(int position, SwipeMenu menu, int index) {
		//删除
		userbusiness.deleteUserById(users.get(index));
		refreshList();
	
	}
	public void refreshList()
	{
		users.clear();
	    userbusiness = new UserDBBusiness(this);
		List<RegistBean> list = userbusiness.getAllUser();
		if (list != null) {
			users.addAll(list);
		}
//		users.add(defaultbean);
		adapter.notifyDataSetChanged(users);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 新增账号管理
		doActivity(R.string.loginaddidform);
	}
}
