package com.example.casting.publisheddynamic;

import android.os.Bundle;
import com.example.casting.util.ThinkAndroidBaseActivity;
import com.ta.util.http.*;
import org.json.JSONException;
import org.json.JSONObject;


public class LetterManage extends ThinkAndroidBaseActivity
{

    public LetterManage()
    {
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        send();
    }

    protected void onAfterOnCreate(Bundle bundle)
    {
    }

    protected void onAfterSetContentView()
    {
        super.onAfterSetContentView();
    }

    private void send()
    {
        JSONObject params = new JSONObject();
        RequestParams param = new RequestParams();
        try
        {
            params.put("from_id", "123");
            params.put("to_id", "124");
            params.put("content", "\u5475\u5475");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        param.put("str", params.toString());
        param.put("response", "application/json");
        asyncHttpClient.post("http://192.168.1.44:8080/casting/services/LetterManage/send", param, new AsyncHttpResponseHandler() {

            public void onSuccess(String content)
            {
                super.onSuccess(content);
            }

        });
    }


    private AsyncHttpClient asyncHttpClient;

}
