package com.example.casting.publisheddynamic;

import android.os.Bundle;
import android.util.Base64;
import com.example.casting.entity.CommentPraise;
import com.example.casting.util.Server_path;
import com.example.casting.util.ThinkAndroidBaseActivity;
import com.ta.util.http.*;
import org.json.JSONException;
import org.json.JSONObject;


public class CommentAndPraiseManage extends ThinkAndroidBaseActivity
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void onAfterOnCreate(Bundle savedInstanceState)
    {
    }

    protected void onAfterSetContentView()
    {
//        getAllCountAsynPost();
        super.onAfterSetContentView();
    }
    /**
     * 我要评论或者试镜或者点赞，取消赞（点赞和取消赞是一样的）
     * @param cp  CommentPraise对象，需setDynamicId,setContent,setCommentType
     * @param id
     */

    private void add(CommentPraise cp,String id)
    {
        JSONObject params = new JSONObject();
        RequestParams param = new RequestParams();
        try
        {
        	params.put("dynamic_id", cp.getDynamicId()); //params.put("dynamic_id", "3");
            params.put("id", id);   //params.put("id", "1");
            params.put("content", cp.getContent());
            params.put("comment_type", cp.getCommentType());//params.put("comment_type", "2");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        param.put("str", params.toString());
        param.put("response", "application/json");
        asyncHttpClient.post(Server_path.addCommentPraise, param, new AsyncHttpResponseHandler() {

            public void onSuccess(String content)
            {
                super.onSuccess(content);
                //TODO showWebView(content);
            }

        });
    }
     /**
      * 获取某动态的评论、赞、试镜
      * @param dynamic_id  动态id
      * @param pagenum  页数
      */
    private void getListAsynPost(String dynamic_id,String pagenum)
    {
        RequestParams param = new RequestParams();
        param.put("dynamic_id", dynamic_id); //param.put("dynamic_id", "3");
        param.put("pagenum", pagenum);           //param.put("pagenum", "1");
        asyncHttpClient.post(Server_path.getListCommentPraise, param, new AsyncHttpResponseHandler() {

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
                //TODO showWebView(new String(bitmapArray));
            }

        });
    }

    /**
     * 获取某条动态评论、赞和报名试镜的数量
     * @param dynamic_id
     */
     
    private void getAllCountAsynPost(String dynamic_id)
    {
        RequestParams param = new RequestParams();
        param.put("dynamic_id", dynamic_id); //param.put("dynamic_id", "3");
        asyncHttpClient.post(Server_path.getAllCountCommentPraise, param, new AsyncHttpResponseHandler() {

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
                try
                {
                    JSONObject jsonResult = new JSONObject(new String(bitmapArray));
                    String comments = jsonResult.getString("comments");
                    String praises = jsonResult.getString("praises");
                    String recruits = jsonResult.getString("recruits");
                    String forwardeds = jsonResult.getString("forwardeds");
                   //TODO  showWebView((new StringBuilder(String.valueOf(comments))).append(" ").append(praises).append(" ").append(recruits).append(" ").append(forwardeds).toString());
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

        });
    }
 
    /**
     * 获取某用户的评论、赞、试镜
     * @param id  - 用户id
     * @param type - 类型：评论，赞，试镜，值：0/1/2
     * @param pagenum  - 页数
     */
   private void getCommentList(String id,String type,String pagenum)
   {
       RequestParams param = new RequestParams();
       param.put("id", id); //param.put("dynamic_id", "3");
       param.put("type", type);
       param.put("pagenum", pagenum);           //param.put("pagenum", "1");
       asyncHttpClient.post(Server_path.getCommentListCommentPraise, param, new AsyncHttpResponseHandler() {

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
               //TODO showWebView(new String(bitmapArray));
           }

       });
   }

   //TODO
   /**
    * 获取某用户的评论、赞、试镜的数量
    * @param dynamic_id
    */
    
//   private void getAllCountAsynPost(String dynamic_id)
//   {
//       RequestParams param = new RequestParams();
//       param.put("dynamic_id", dynamic_id); //param.put("dynamic_id", "3");
//       asyncHttpClient.post(Server_path.getAllCountCommentPraise, param, new AsyncHttpResponseHandler() {
//
//           public void onSuccess(String content)
//           {
//               super.onSuccess(content);
//               String action = null;
//               try
//               {
//                   JSONObject jsonResult = new JSONObject(content);
//                   action = jsonResult.getString("return");
//               }
//               catch(JSONException e)
//               {
//                   e.printStackTrace();
//               }
//               byte bitmapArray[] = Base64.decode(action, 0);
//               try
//               {
//                   JSONObject jsonResult = new JSONObject(new String(bitmapArray));
//                   String comments = jsonResult.getString("comments");
//                   String praises = jsonResult.getString("praises");
//                   String recruits = jsonResult.getString("recruits");
//                   String forwardeds = jsonResult.getString("forwardeds");
//                  //TODO  showWebView((new StringBuilder(String.valueOf(comments))).append(" ").append(praises).append(" ").append(recruits).append(" ").append(forwardeds).toString());
//               }
//               catch(JSONException e)
//               {
//                   e.printStackTrace();
//               }
//           }
//
//       });
//   }
   

    private AsyncHttpClient asyncHttpClient;

}
