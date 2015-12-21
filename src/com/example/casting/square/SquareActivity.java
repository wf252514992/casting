package com.example.casting.square;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.ConditionBean;
import com.example.casting.entity.PlazaBean;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.attention.AddAttentionProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.plaza.PlazaProcessor;
import com.example.casting.processor.plaza.getConditionsProcessor;
import com.example.casting.search.SquareSearchActivity;
import com.example.casting.square.view.HotView;
import com.example.casting.square.view.LabelView;
import com.example.casting.square.view.HotView.HotViewListener;
import com.example.casting.square.view.LabelView.LabelViewListener;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.R.string;

public class SquareActivity extends BaseForm implements OnClickListener {

	HotView hotView;
	LabelView labelView;
	View titleLayout;
	EditText edt_search;
	LinearLayout layoutcontent;
	PlazaProcessor getprocessor = new PlazaProcessor();
	getConditionsProcessor conditionPro = new getConditionsProcessor();
	AddAttentionProcessor addationprocessor = new AddAttentionProcessor();
	ArrayList<ConditionBean> agelist = new ArrayList<ConditionBean>();
	List<ConditionBean> sexlist = new ArrayList<ConditionBean>();
	private final int notifyHotView = 1333;
	private int user_hot = 1;
	private int user_label = 2;
	private int searchConditionUser = user_hot;

	/**
	 * 下载查询 条件
	 */
	private void loadConditions(ConditionBean bean, boolean waitting) {
		HttpCall(waitting, conditionPro, bean);
	}

	/**
	 * 获取标签
	 * 
	 * @param page
	 */
	private void loadLabelConditions(int page) {
		searchConditionUser = user_label;
		ConditionBean bean = new ConditionBean();
		bean.setType("2");
		bean.setPagenum(page + "");
		loadConditions(bean, false);
	}

	/**
	 * 获取年龄段
	 */
	private void loadAgeConditions() {
		searchConditionUser = user_hot;
		ConditionBean bean = new ConditionBean();
		bean.setType("1");
		bean.setPagenum("1");
		loadConditions(bean, false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square);
		ConditionBean bean1 = new ConditionBean();
		bean1.setType("3");
		bean1.setSex("男");
		ConditionBean bean2 = new ConditionBean();
		bean2.setType("3");
		bean2.setSex("女");
		sexlist.add(bean1);
		sexlist.add(bean2);
		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (agelist.size() == 0)
			loadAgeConditions();

		hotView.freshCurrent(0);
	}

	protected void init() {
		titleLayout = (View) findViewById(R.id.titlelayout);
		edt_search = (EditText) findViewById(R.id.edt_search);
		layoutcontent = (LinearLayout) findViewById(R.id.layoutcontent);
		edt_search.setOnClickListener(this);
		initView(titleLayout);
		setLeftButtonAble(false, "返回");
		setTitle("榜单");
		hotView = new HotView(this, hotlistener);
		hotView.setSexConditions(sexlist);
		labelView = new LabelView(this, labellistener);
		layoutcontent.addView(hotView.getView());
	}

	ConditionBean conditBean;

	private void searchHot(ConditionBean value, boolean waiting) {
		if (value == null)
			return;
		conditBean = value;
		searchConditionUser = user_hot;
		if (value.getType().equals("2")) {
			// 搜索标签中的用户
			searchConditionUser = user_label;
		}
		PlazaBean bean = new PlazaBean();
		bean.setSearchtype(value.getType());
		bean.setId(Session.getInstance().getUser_id());
		bean.setMinage(value.getMinage());
		bean.setMaxage(value.getMaxage());
		bean.setSex(value.getSex());
		bean.setLabel(value.getLabel());
		bean.setPagenum(value.getPagenum());
		HttpCall(waiting, getprocessor, bean);
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		if (bean.getCode() == Errors.OK) {
			if (processor.getProcessorId()
					.equals(getprocessor.getProcessorId())) {
				List<PlazaBean> beans = (List) bean.getObj();
				Message msg = new Message();
				msg.what = listcomplet;
				msg.obj = beans;
				myHandler.sendMessage(msg);

			} else if (processor.getProcessorId().equals(
					conditionPro.getProcessorId())) {
				List<ConditionBean> condits = (List) bean.getObj();
				Message msg = new Message();
				msg.what = conditoncomplet;
				msg.obj = condits;
				myHandler.sendMessage(msg);
			} else if (processor.getMethod().equals(
					addationprocessor.getMethod())) {
				// 关注成功
				showToast("关注成功");
				// searchHot(conditBean, false);
				Message msg = new Message();
				msg.what = notifyHotView;
				myHandler.sendMessage(msg);
			}

		} else {
			if (processor.getProcessorId()
					.equals(getprocessor.getProcessorId())) {
				List<PlazaBean> beans = new ArrayList<PlazaBean>();
				Message msg = new Message();
				msg.what = listcomplet;
				msg.obj = beans;
				myHandler.sendMessage(msg);
			} else if (processor.getProcessorId().equals(
					conditionPro.getProcessorId())) {
				List<ConditionBean> condits = new ArrayList<ConditionBean>();
				Message msg = new Message();
				msg.what = conditoncomplet;
				msg.obj = condits;
				myHandler.sendMessage(msg);
			}
			// showToast(bean.getMessage());
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.edt_search) {
			startActivity(new Intent(this, SquareSearchActivity.class));
		}
	}

	private final int listcomplet = 1111;
	private final int conditoncomplet = 1222;
	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case listcomplet: {
				List<PlazaBean> list = (List<PlazaBean>) msg.obj;
				if (searchConditionUser == user_hot) {
					hotView.setHotList(list);
				} else if (searchConditionUser == user_label) {
					labelView.setHotList(list);
				}
				break;

			}
			case conditoncomplet: {
				List<ConditionBean> list = (List<ConditionBean>) msg.obj;
				if (searchConditionUser == user_hot) {
					agelist.clear();
					if (list != null)
						agelist.addAll(agelist);
					hotView.setAgeConditions(list);
				} else if (searchConditionUser == user_label) {
					hotView.setLabelConditions(list);
				}
			}
				break;
			case notifyHotView:
				if (searchConditionUser == user_hot) {
					hotView.notifyDataChage();
				} else if (searchConditionUser == user_label) {
					labelView.notifyDataChage();
				}
				break;
			default:
				break;
			}
		};
	};

	private LabelViewListener labellistener = new LabelViewListener() {

		@Override
		public void search(ConditionBean bean, boolean wait) {
			// TODO Auto-generated method stub
			searchHot(bean, wait);
		}

		@Override
		public void jump(int nextid, Bundle bd) {
			// TODO Auto-generated method stub
			doActivity(nextid, bd);
		}

		@Override
		public void changeView() {
			// TODO Auto-generated method stub
			if (hotView != null) {
				layoutcontent.removeAllViews();
				layoutcontent.addView(hotView.getView());
			}
		}

		@Override
		public void addAttention(PlazaBean bean) {
			// TODO Auto-generated method stub
			if (bean != null) {
				AttentionBean attentionbean = new AttentionBean();
				attentionbean.setFollowers_id(Session.getInstance()
						.getUser_id());
				attentionbean.setBy_follower_id(bean.getId());
				HttpCall(addationprocessor, attentionbean, "str");
			} else {
				// showToast("参数错误");
			}
		}
	};
	private HotViewListener hotlistener = new HotViewListener() {

		@Override
		public void search(ConditionBean conditbean, boolean wait) {
			// TODO Auto-generated method stub
			searchHot(conditbean, wait);
		}

		@Override
		public void loadConditions(int page) {
			// TODO Auto-generated method stub
			loadLabelConditions(page);
		}

		@Override
		public void addAttention(PlazaBean bean) {
			// TODO Auto-generated method stub
			if (bean != null) {
				AttentionBean attentionbean = new AttentionBean();
				attentionbean.setFollowers_id(Session.getInstance()
						.getUser_id());
				attentionbean.setBy_follower_id(bean.getId());
				HttpCall(addationprocessor, attentionbean, "str");
			} else {
				// showToast("参数错误");
			}
		}

		@Override
		public void changeView(ConditionBean bean) {
			// TODO Auto-generated method stub
			if (labelView != null) {
				layoutcontent.removeAllViews();
				layoutcontent.addView(labelView.getView(bean));
			}
		}

		@Override
		public void jump(int nextid, Bundle bd) {
			// TODO Auto-generated method stub
			doActivity(nextid, bd);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else
			return super.onKeyDown(keyCode, event);
	}
}
