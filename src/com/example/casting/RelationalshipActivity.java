package com.example.casting;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.entity.ContactBean;
import com.example.casting.login.BaseForm;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 联系人 界面      @到联系人
 * @author chenjiaping
 *
 */
public class RelationalshipActivity extends BaseForm {
//	@TAInject
	private AsyncHttpClient asyncHttpClient;
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private ContactHomeAdapter adapter;
	private List<ContactBean> list;
	private EditText edit;
	private ListView listView;
	private ListView contactList;
//	private ReviewlistAdapter reviewlistAdapter;
	private QuickAlphabeticBar alpha;
	private DownloadManager downloadManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO  联系人字母选择控件
		View view = View.inflate(this, R.layout.relationalship, null);
		setContentView(view);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("联系人");
		init();
		asyncHttpClient=new AsyncHttpClient();
		String id=Session.getInstance().getUser_id();
		getUserList(id, "1");
	}
	private void init(){
		
		listView = (ListView) findViewById(R.id.contact_list);
		contactList = (ListView)findViewById(R.id.acbuwa_list);
		edit = (EditText) findViewById(R.id.edit);
		alpha = (QuickAlphabeticBar)findViewById(R.id.fast_scroller);
		edit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != adapter&&list!=null){
					Filter filter = adapter.getFilter(); //得到一个过滤器
			        filter.filter(s);  //为该过滤器设置约束条件
			        setAdapter(list);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	}
	private void setAdapter(final List<ContactBean> list) {
		
		adapter = new ContactHomeAdapter(this, list,alpha);
		contactList.setAdapter(adapter);
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				Intent intent = new Intent();
				intent.putExtra("Name", list.get(position).getName());
				setResult(Activity.RESULT_OK, intent);
				finish();//结束之后会将结果传回From
			
				
				
			}
			
		});
		alpha.init(RelationalshipActivity.this);
		alpha.setListView(listView);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
		
		contactList.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//					if(bohaopan.getVisibility() == View.VISIBLE){
//						bohaopan.setVisibility(View.GONE);
//						keyboard_show_ll.setVisibility(View.VISIBLE);
//					}
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
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
	private void getUserList(String id, String pagenum) {
		showWaitDialog();
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getAttent,
						param, new AsyncHttpResponseHandler() {

							public void onSuccess(String content) {
								super.onSuccess(content);
								downloadManager = DownloadManager.getDownloadManager();
								dismissDialog();
								String action = null;
								try {
									JSONObject jsonResult = new JSONObject(
											content);
									action = jsonResult.getString("return");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								byte bitmapArray[] = Base64.decode(action, 0);
//								[{"attent_id":"1","followers_id":"1" ,"followers_name":"",
//									"by_follower_id":"97","by_followers_name":"",
//								"focus_time": "2015-01-27 14:39:40.0","nickname":"哈","head_portrait":""}]
							try {
								JSONArray obj =new JSONArray(new String(bitmapArray));
								if (obj != null) {
									list= new ArrayList<ContactBean>();
									int length=obj.length();
									int i = 0; 
									while (i <= length-1) {
										    
											
												JSONObject item = obj
														.getJSONObject(i);
												    ContactBean cb=new ContactBean();
												    cb.setName(item.getString("nickname"));
												    cb.setPic(item.getString("head_portrait"));
												    String url=Server_path.serverfile_path
															+ item.getString("head_portrait");
													downloadManager.addHandler(url);
													String nickName=item.getString("nickname");
													if(nickName!=null&&nickName.length()>0){
														 String fore=nickName.substring(0, 1);
														    String sortKey=getPinYinHeadChar(fore);
														    cb.setSortKey(sortKey);
														    list.add(cb);
													}
												   
												    i++;
									}
									setAdapter(list);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}			    
							}

						});
	}
	/**
     * 得到中文首字母
     * 
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }     
        }
        convert=convert.toUpperCase();
        return convert;
    }
	class ReviewlistAdapter extends BaseAdapter{
		Context context;
		List<ContactBean> arrayList;

		public ReviewlistAdapter(Context context,List<ContactBean> arrayList){
			super();
			this.context=context;
			this.arrayList=arrayList;
		
		}
		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int span) {
			return span;
		}

		@Override
		public View getView(int position, View v, ViewGroup group) {
			LayoutInflater inflater=LayoutInflater.from(context);
			v=inflater.inflate(R.layout.relationalship_item, null);
			
			ImageView pic=(ImageView) v.findViewById(R.id.pic);
			TextView name=(TextView) v.findViewById(R.id.name);
			if(arrayList.get(position).getPic()!=null){
                //TODO 框架图片加载
				
			}
			name.setText(arrayList.get(position).getName());
		
			return v;
		}
		public Filter getFilter() {
			Filter filter = new Filter() {
				protected void publishResults(CharSequence constraint, FilterResults results) {
					list = (ArrayList<ContactBean>) results.values;
					
					if (results.count > 0) {
		//TODO				
						notifyDataSetChanged();
						
					} else {
						notifyDataSetInvalidated();
					}
				}
				protected FilterResults performFiltering(CharSequence s) {
					String str = s.toString();
					FilterResults results = new FilterResults();
					ArrayList<ContactBean> contactList = new ArrayList<ContactBean>();
					if (list != null && list.size() != 0) {
						for(ContactBean cb : list){
					
							if(cb.getName().indexOf(str)>=0){
								contactList.add(cb); 
							}
						}
					}
					results.values = contactList;
					results.count = contactList.size();
					return results;
				}
			};
			return filter;
		}

	}
	@Override
	public void LeftButtonClick() {
		finish();
	}
	@Override
	public void RightButtonClick() {
		
	}
	
}
