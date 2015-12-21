package com.example.casting.processor.data;

import org.json.JSONObject;

import android.util.Log;

public class CastMap {

	private final String TAG = "CastMap";
	public CastMap(JSONObject json){
		this.json = json;
	}
	private JSONObject json ;
	/**
	 * @param key
	 * @return 根据key返回String
	 */
	public String get(String key) {
		try {
			return this.json.getString(key);
		} catch (Exception ex) {
			Log.e(TAG, ex.toString());
			return "";
		}
	}

}
