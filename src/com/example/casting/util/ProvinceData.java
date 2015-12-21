package com.example.casting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;


/**
 * 用于 注册时 加载 地市
 * @author wf
 *
 */
public class ProvinceData {
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();
	private JSONObject mJsonObj;
	private String[] mProvinceDatas;
	
	public ProvinceData(Context mContext){
		initJsonData(mContext);
		initDatas();
		
	}
	public String[] getAreas(int CurrentP,int currentC)
	{
		String mCurrentProviceName = mProvinceDatas[CurrentP];
		String mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[currentC];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);
		if (areas == null)
		{
			areas = new String[] { "" };
		}
		return areas;
	}
	public String[] getCities(int pCurrent)
	{
		String mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null)
		{
			cities = new String[] { "" };
		}
		return cities;
	}
	public String[] getProvince(){
		return mProvinceDatas;
	}
	public void initDatas()
	{
		try
		{
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonP = jsonArray.getJSONObject(i);// ÿ��ʡ��json����
				String province = jsonP.getString("p");// ʡ����

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try
				{
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1)
				{
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++)
				{
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// ������
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try
					{
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e)
					{
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// ��ǰ�е�������
					for (int k = 0; k < jsonAreas.length(); k++)
					{
						String area = jsonAreas.getJSONObject(k).getString("s");// ��������
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		mJsonObj = null;
	}
	
	private void initJsonData(Context mcontext)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = mcontext.getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

}
