package com.example.casting;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.ConditionBean;
import com.example.casting.entity.PlazaBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.attention.AddAttentionProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.plaza.PlazaProcessor;
import com.example.casting.processor.plaza.getConditionsProcessor;
import com.example.casting.square.adapter.HotAdapter;
import com.example.casting.square.view.HotView.HotViewListener;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 已报名界面
 * 
 * @author chenjiaping
 * 
 */

public class SignupedActivity extends BaseForm {
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
//	@TAInjectView(id = R.id.signuped)
//	ListView listview;
	private HotAdapter hotAdapter;
	PlazaProcessor getprocessor = new PlazaProcessor();
	getConditionsProcessor conditionPro = new getConditionsProcessor();
	ArrayList<ConditionBean> agelist = new ArrayList<ConditionBean>();
	List<ConditionBean> sexlist = new ArrayList<ConditionBean>();

	AddAttentionProcessor addationprocessor = new AddAttentionProcessor();
	private AsyncHttpClient asyncHttpClient;
	private DownloadManager downloadManager = DownloadManager.getDownloadManager();
	private ListView listview;
	private boolean ing = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.signuped, null);
		setContentView(view);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("已报名");
		asyncHttpClient=new AsyncHttpClient();
		listview=(ListView) findViewById(R.id.signuped);
		Intent intent=getIntent();
		String dynamicId=intent.getStringExtra("dynamicId");
		String id=Session.getInstance().getUser_id();
		String action=getIntent().getStringExtra("action");
		if(action!=null){
			getListAsynPost(id,null,"1",Server_path.getAllListCommentPraise,Session.getInstance().getName_nick(),"2");
		}else{
		  getListAsynPost(id,dynamicId,"1",Server_path.getListCommentPraise,null,null);
		}
	}

	/**
     * 获取某动态的评论、赞、试镜
     * @param dynamic_id  动态id
     * @param pagenum  页数
     */
   private void getListAsynPost(String id,String dynamic_id,String pagenum,String path,String nickname,String type)
   {
	   
       RequestParams param = new RequestParams();
       param.put("id", id);
       if(dynamic_id!=null){
    	   param.put("dynamic_id", dynamic_id); //param.put("dynamic_id", "3");
       }
       if(type!=null){
    	   param.put("type", type);
       }
       param.put("pagenum", pagenum);           //param.put("pagenum", "1");
       if(nickname!=null){
    	   param.put("nickname", nickname);
       }
       asyncHttpClient.post(path, param, new AsyncHttpResponseHandler() {

           public void onSuccess(String content)
           {
               super.onSuccess(content);
               
               String action = null;
               try
               {
                   JSONObject jsonResult = new JSONObject(content);
                   action = jsonResult.getString("return");
               }
               catch(JSONException e)
               {
                   e.printStackTrace();
               }
               byte bitmapArray[] = Base64.decode(action, 0);
               String str=new String(bitmapArray);
               System.out.println(".......str:"+str);
               try { 
					JSONArray obj =new JSONArray(str);
					if (obj != null) {
      
//						[{"comment_id":"12","dynamic_id":"156","id":"1","content":"好好好好好好好好好好好",
//						"praise":"","audition":"","comment_date":"2015-02-11 17:20:53.0",
//						"comment_type":"0","nickname":"test","head_portrait":""},
//						 {"comment_id":"39","dynamic_id":"156","id":"1","content":"","praise":"",
//						"audition":"","comment_date":"2015-02-11 20:47:34.0",
//							"comment_type":"1","nickname":"test","head_portrait":""}]
						 ArrayList<PlazaBean>  pbs=new  ArrayList<PlazaBean> ();
						int length=obj.length();
						int i = 0; 
						while (i <= length-1) {
							    
								JSONObject item = obj
										.getJSONObject(i);
									   if(item.getString("comment_type").equals("2")){
										    PlazaBean pb=new PlazaBean();
										    pb.setFollowers_count(item.getString("introduction"));
										    String picPath = DownloadManager.FILE_ROOT
													+ TAStringUtils.getFileNameFromUrl(Server_path.serverfile_path + item.getString("head_portrait"));
										    ImageBean ib=new ImageBean(ImageBean.type_filepath,picPath);
										    pb.setImgbean(ib);
											pb.setNickname(item.getString("nickname"));
											pb.setState(item.getString("attention"));
											pb.setId(item.getString("id"));
											pb.setType("1");
										    pbs.add(pb);
									   }
								   String url=Server_path.serverfile_path
											+ item.getString("head_portrait");
									downloadManager.addHandler(url);
								    i++;
							}
						hotAdapter = new HotAdapter(SignupedActivity.this, pbs) {
							@Override
							public void addAttention(PlazaBean bean,int position) {
								// TODO Auto-generated method stub
								if (bean.getState() == 0
										&& !bean.getId().equals(
												Session.getInstance().getUser_id()))
									hotlistener.addAttention(bean);
							}
						};
						hotAdapter.setAction("已报名");
						listview.setAdapter(hotAdapter);
						listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {

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
												hotlistener.jump(resourceid, bd);
											} else if (registbean.getType().equals(
													ConstantData.Login_Type_Nomal)) {
												resourceid = R.string.otherwatchmeactivity;
												bd.putSerializable(resourceid + "", registbean);
												// doActivity(resourceid, bd);
												hotlistener.jump(resourceid, bd);
											} else if (registbean.getType().equals(
													ConstantData.Login_Type_Company)) {
												resourceid = R.string.otherwatchcompanyactivity;
												bd.putSerializable(resourceid + "", registbean);
												// doActivity(resourceid, bd);
												hotlistener.jump(resourceid, bd);
											}
										}
									} 
//									else if (obj != null && obj instanceof ConditionBean) {
//										// 标签
//										ConditionBean bean = (ConditionBean) obj;
//										hotlistener.changeView(bean);
//									}
									ing = false;
								}

							
								
							}
							
						});
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
           }

       });
   }
	
	private HotViewListener hotlistener = new HotViewListener() {

		@Override
		public void search(ConditionBean conditbean, boolean wait) {}

		@Override
		public void loadConditions(int page) {}

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
				showToast("参数错误");
			}
		}

		@Override
		public void changeView(ConditionBean bean) {}

		@Override
		public void jump(int nextid, Bundle bd) {
			// TODO Auto-generated method stub
			doActivity(nextid, bd);
		}
	};
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		if (bean.getCode() == Errors.OK) {
			 if (processor.getMethod().equals(
					addationprocessor.getMethod())) {
				// 关注成功
				showToast("关注成功");
			}

		} 
	}
	@Override
	public void LeftButtonClick() {
//		Intent intent=new Intent(this,MainTab.class);
//		startActivity(intent);
		finish();
		
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}
	
}
