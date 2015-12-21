package com.example.casting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.example.casting_android.R;
import com.example.casting.entity.ContactBean;
import com.example.casting.util.Server_path;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ContactHomeAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private List<ContactBean> list;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private Context ctx;
	
	public ContactHomeAdapter(Context context, List<ContactBean> list,QuickAlphabeticBar alpha) {
		
		this.ctx = context;
		this.inflater = LayoutInflater.from(ctx);
		this.list = list; 
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i =0; i <list.size(); i++) {
			String name = getAlpha(list.get(i).getSortKey());
			if(!alphaIndexer.containsKey(name)){ 
				alphaIndexer.put(name, i);
			}
		}
		
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
		alpha.setAlphaIndexer(alphaIndexer);
	}

	
	public int getCount() {
		return list.size();
	}

	
	public Object getItem(int position) {
		return list.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}
	
	public void remove(int position){
		list.remove(position);
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.home_contact_list_item, null);
			holder = new ViewHolder();
			holder.qcb = (QuickContactBadge) convertView.findViewById(R.id.qcb);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ContactBean cb = list.get(position);
		String name = cb.getName();
		String pic = cb.getPic();
		holder.name.setText(name);
		String url=Server_path.serverfile_path+pic;
		   String path=DownloadManager.FILE_ROOT+TAStringUtils.getFileNameFromUrl(url);
	    holder.qcb.setImageBitmap(BitmapFactory.decodeFile(path));
		//利用反射机制给QuickContactBadge.mOverlay复制为null  
		try {  
		    Field f = holder.qcb.getClass().getDeclaredField("mOverlay");  
		    f.setAccessible(true);   
		    f.set(holder.qcb,null);   
		} catch (Exception e) {  
		    e.printStackTrace();  
		}   
		String currentStr = getAlpha(cb.getSortKey());
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getSortKey()) : " ";
		if (!previewStr.equals(currentStr)) { 
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		QuickContactBadge qcb;
		TextView alpha;
		TextView name;
	}
	
	
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); 
		} else {
			return "#";
		}
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
