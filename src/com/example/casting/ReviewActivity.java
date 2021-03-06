package com.example.casting;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.entity.CommentPraise;
import com.example.casting.login.BaseForm;
import com.example.casting.publisheddynamic.ExpressionImageAdapter;
import com.example.casting.publisheddynamic.MyOnPageChangeListener;
import com.example.casting.publisheddynamic.MyPagerAdapter;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.Expression;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReviewActivity extends BaseForm implements OnClickListener {
	// @TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	// @TAInjectView(id = R.id.editreview)
	EditText editText;
	// @TAInject
	private AsyncHttpClient asyncHttpClient;

	// --表情--//
	int columns = 6, rows = 3, pageExpressionCount = 3 * 6 - 1;
	ViewPager vp_id;
	 LinearLayout ll_expression;
	public LinearLayout ll_vp_selected_index;
	ArrayList<GridView> grids;
	MyPagerAdapter myPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.review, null);
		setContentView(view);
		titleLayout = findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "取消");
		setRightButtonAble(true, "发送");
		Intent intent = getIntent();
		String action = intent.getStringExtra("action");
		if (action != null) {
			setTitle(action);
		} else {
			setTitle("发评论");
		}
		asyncHttpClient = new AsyncHttpClient();
		init();
	}

	private void init() {
		editText = (EditText) findViewById(R.id.editreview);
		// 获取编辑框焦点
		editText.setFocusable(true);
		// 打开软键盘 对于刚跳到一个新的界面就要弹出软键盘的情况上述代码
		// 可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) editText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}

		}, 198);
		ImageButton atsend = (ImageButton) findViewById(R.id.atsend);
		atsend.setOnClickListener(this);

		editText.setOnClickListener(this);
		ImageButton expression = (ImageButton) findViewById(R.id.expression);
		expression.setOnClickListener(this);
		ll_vp_selected_index = (LinearLayout) findViewById(R.id.ll_vp_selected_index);
		ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
		vp_id = (ViewPager) findViewById(R.id.vp_id);
		vp_id.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void LeftButtonClick() {
		// Intent intent = new Intent(this, MainTab.class);
		// startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		// 需setDynamicId,setContent,setCommentType
		Intent intent = getIntent();
		String dynamicId = intent.getStringExtra("dynamicId");
		String content = editText.getText().toString();
		if (content.trim().equals("")) {
			Toast.makeText(this, "请输入评价内容！", Toast.LENGTH_LONG).show();
			return;
		}
		CommentPraise cp = new CommentPraise();
		cp.setCommentType("0");
		cp.setDynamicId(dynamicId);
		cp.setContent(content);
		String id = Session.getInstance().getUser_id();
		add(cp, id);// TODO
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
			String str = data.getStringExtra("Name");// str即为回传的值
			editText.append("@" + str+" ");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Dialog dialog;

	/**
	 * 等待框show
	 */
	public void showWaitDialog() {
		if (dialog == null) {
			dialog = CommonDialog.ProgressDialog(this);

			// dialog = new ProgressDialog(this);
			// dialog.setCancelable(false);
		}
		if (!dialog.isShowing())
			dialog.show();
	}

	/**
	 * 等待框 dismiss
	 */
	public void dismissDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	/**
	 * 我要评论或者试镜或者点赞，取消赞（点赞和取消赞是一样的）
	 * 
	 * @param cp
	 *            CommentPraise对象，需setDynamicId,setContent,setCommentType
	 *            评论类型：评论，赞，试镜，值：0/1/2
	 * @param id
	 */

	private void add(CommentPraise cp, String id) {
		showWaitDialog();
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		try {
			params.put("dynamic_id", cp.getDynamicId()); // params.put("dynamic_id",
															// "3");
			params.put("id", id); // params.put("id", "1");
			params.put("content", cp.getContent());
			params.put("comment_type", cp.getCommentType());// params.put("comment_type",
															// "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("str", params.toString());
		param.put("response", "application/json");
		asyncHttpClient.post(Server_path.addCommentPraise, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						super.onSuccess(content);
						dismissDialog();
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (action.equals("1")) {
							Toast.makeText(ReviewActivity.this, "恭喜您评论成功！",
									Toast.LENGTH_LONG).show();
							Intent intent=getIntent();
							String actionStr=intent.getStringExtra("actionstr");
							if(actionStr!=null&&actionStr.equals("text")){
								setResult(Activity.RESULT_OK);
							}
							finish();
						} else {
							Toast.makeText(ReviewActivity.this, "评论失败，请稍后重试！",
									Toast.LENGTH_LONG).show();
						}
					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.atsend:
			Intent intent = new Intent(this, RelationalshipActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.expression:
			if (ll_expression.getVisibility() == View.GONE) {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(this.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				ll_expression.setVisibility(View.VISIBLE);
			} else {
				ll_expression.setVisibility(View.GONE);
			}
			break;
		case R.id.editreview:
			if (ll_expression.getVisibility() == View.VISIBLE) {
				ll_expression.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (null == myPagerAdapter) {
			List<List<Expression>> lists = initGridViewData();// 填充GridView数据
			grids = new ArrayList<GridView>();
			int gv_padding_lr = (int) getResources().getDimension(
					R.dimen.chat_gv_padding_lr);
			int gv_padding_bt = (int) getResources().getDimension(
					R.dimen.chat_gv_padding_bt);
			int gv_spacing = (int) getResources().getDimension(
					R.dimen.chat_gv_spacing);
			int chat_dot_margin_lr = (int) getResources().getDimension(
					R.dimen.chat_dot_margin_lr);
			int chat_dot_wh = (int) getResources().getDimension(
					R.dimen.chat_dot_wh);
			for (int i = 0; i < lists.size(); i++) {
				List<Expression> l = lists.get(i);
				if (null != l) {
					// --生成当前GridView--//
					final GridView gv = new GridView(this);
					gv.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					gv.setNumColumns(columns);
					gv.setGravity(Gravity.CENTER);
					gv.setPadding(gv_padding_lr, gv_padding_bt, gv_padding_lr,
							0);
					gv.setHorizontalSpacing(gv_spacing);
					gv.setVerticalSpacing(gv_spacing);
					ExpressionImageAdapter expressionImageAdapter = new ExpressionImageAdapter(
							this, l);
					gv.setAdapter(expressionImageAdapter);
					// --点击列表事件处理--//
					gv.setOnItemClickListener(new OnItemClickListener() {
						/*
						 * (non-Javadoc)
						 * 
						 * @see android.widget.AdapterView.OnItemClickListener#
						 * onItemClick(android.widget.AdapterView,
						 * android.view.View, int, long)
						 */
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Expression e = (Expression) gv
									.getItemAtPosition(arg2);
							Log.v("_____________", "点击表情_" + e.code);
							int index = editText.getSelectionStart();
							Editable edit = editText.getEditableText();// 获取EditText的文字
							String content_all = edit.toString();
							String content_forward = content_all.substring(0,
									index);
							Log.v("_____________", "content_all = "
									+ content_all + "|content_forward = "
									+ content_forward + "|");
							String reg = "^[A-Za-z0-9]+$";// TODO 正则表达式
							reg = reg.replace("/", "");
							if (e.getDrableId() < 0) {// 点击删除按钮
								if (index > 0) {
									boolean delExpression = false;
									Pattern p = Pattern.compile(reg);
									Matcher matcher = p
											.matcher(content_forward);
									// 因为这里表情代码最长为5，所以这里减5
									boolean found = false;
									if (content_forward.length() >= 4) {// 如果光标前字符少于4个，说明不可能为表情
										if (content_forward.length() == 4) {
											found = matcher
													.find(content_forward
															.length() - 4);
										} else {
											found = matcher
													.find(content_forward
															.length() - 5);
										}
										if (found) {
											String flag = matcher.group();
											if (content_forward.substring(
													content_forward.length()
															- flag.length(),
													content_forward.length())
													.equals(flag)) {
												delExpression = true;
												edit.delete(
														index - flag.length(),
														index);
											}
										}
									}
									if (!delExpression) {
										edit.delete(index - 1, index);
									}
								}
							} else {
								ImageSpan imageSpan = new ImageSpan(
										ReviewActivity.this,
										e.drableId);
								SpannableString spannableString = new SpannableString(
										e.code);
								spannableString.setSpan(imageSpan, 0,
										e.code.length(),
										Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

								Log.v("_____________", "spannableString="
										+ spannableString.toString()
										+ "__index=" + index);

								if (index < 0 || index >= edit.length()) {
									edit.append(spannableString);
								} else {
									edit.insert(index, spannableString);
								}
							}
						}
					});
					grids.add(gv);
					// --生成索引图--//
					ImageView iv = new ImageView(this);
					android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
							chat_dot_wh, chat_dot_wh);
					lp.leftMargin = chat_dot_margin_lr;
					lp.rightMargin = chat_dot_margin_lr;
					iv.setLayoutParams(lp);
					if (i == 0) {
						iv.setBackgroundResource(R.drawable.page_focused);
					} else {
						iv.setBackgroundResource(R.drawable.page_unfocused);
					}
					ll_vp_selected_index.addView(iv);
				}
			}
			myPagerAdapter = new MyPagerAdapter(grids);
			vp_id.setAdapter(myPagerAdapter);
		}

		// tv_chat_title.setText(Html.fromHtml("<img src=\""+2130837523+"\" />",
		// imageGetter_resource, null));
	}

	/**
	 * 填充GridView所需要的数据
	 */
	private List<List<Expression>> initGridViewData() {
		List<List<Expression>> lists = new ArrayList<List<Expression>>();
		List<Expression> list = null;
		for (int i = 0; i < MainTab.expressionList.size(); i++) {
			if (i % pageExpressionCount == 0) {// 一页数据已填充完成
				if (null != list) {
					list.add(new Expression(-1, "backSpace"));// 添加删除键
					lists.add(list);
				}
				list = new ArrayList<Expression>();
			}
			list.add(MainTab.expressionList.get(i));
			// 最后一个表情，并且不是当前页最后一个表情时，后面添加删除键
			if (i >= MainTab.expressionList.size() - 1) {
				list.add(new Expression(-1, "backSpace"));// 添加删除键
				lists.add(list);
			}
		}
		return lists;
	}
}
