package com.geniuseoe2012.lazyloaderdemo.cache;

import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import android.content.Context;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}


	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {
		
		return ConstantData.getCastingDir()+"/"+Session.getInstance().getUser_id()+"/pic";
	}

}
