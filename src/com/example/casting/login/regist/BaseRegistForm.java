package com.example.casting.login.regist;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import com.example.casting.login.BaseTakePhoneForm;
import com.example.casting.login.regist.views.TextViewClickBean;
import com.example.casting.util.ConstantData;
import com.example.casting.util.ProvinceData;
import com.ta.common.Arrays;

public abstract class BaseRegistForm extends BaseTakePhoneForm implements
		OnClickListener, IMyWheelViewCallBackListener {

	public final int position_director = 0;
	public final int position_company = 1;
	ProvinceData Province;
	View popuview;
	PopupWindow pop;
	MyWheelView wheelview;
	protected static final int type_date = 1;
	protected static final int type_address = 0;
	private int wheelviewtype = type_date;

	protected void getWheelView(View v, int resid, int type) {
		wheelviewtype = type;
		wheelview = new MyWheelView(this,type, screen_width, resid, this);
		popuview = wheelview.getWheelView();
		if (Province == null) {
			Province = new ProvinceData(this);
		}
		List<String> list1;
		List<String> list2;
		List<String> list3;
		if (type == type_address) {
			list1 = getVal(0, 0, 0);
			list2 = getVal(1, 0, 0);
			list3 = getVal(2, 0, 0);

			wheelview.setWheelViewAdapter(0, list1);
			wheelview.setWheelViewAdapter(1, list2);
			wheelview.setWheelViewAdapter(2, list3);

		} else if (type == type_date) {
			list1 = Arrays.asList(ConstantData.getYears());
			list2 = Arrays.asList(ConstantData.months);
			list3 = Arrays.asList(ConstantData.getDays("", "", 0));
			wheelview.setWheelViewAdapter(0, list1);
			wheelview.setWheelViewAdapter(1, list2);
			wheelview.setWheelViewAdapter(2, list3);
		}
		pop = new PopupWindow(popuview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}

	private List<String> getVal(int type, int v1, int v2) {
		if (type == 0) {
			String[] provinces = Province.getProvince();
			return Arrays.asList(provinces);
		} else if (type == 1) {
			String[] cites = Province.getCities(v1);
			return Arrays.asList(cites);
		} else {
			String[] county = Province.getAreas(v1, v2);
			return Arrays.asList(county);
		}
	}

	@Override
	public void btn_OkClickListener(Object obj, int resid) {
		// TODO Auto-generated method stub
		pop.dismiss();
		changeText(obj.toString(), resid);
	}

	@Override
	public void btn_CancelClickListener() {
		// TODO Auto-generated method stub
		pop.dismiss();
	}

	@Override
	public void ItemNotify(Object obj) {
		// TODO Auto-generated method stub
		int resid = Integer.parseInt(obj.toString());
		if (resid == 0) {
			if (wheelviewtype == type_address) {
				int province = wheelview.getSelectedIndex(0);
				List<String> list2 = getVal(1, province, 0);
				List<String> list3 = getVal(2, province, 0);
				wheelview.setWheelViewAdapter(1, list2);
				wheelview.setWheelViewAdapter(2, list3);
			} else if (wheelviewtype == type_date) {
				List<String> list3;
				String year = wheelview.getSelectedItem(0);
				String month = wheelview.getSelectedItem(1);
				list3 = Arrays.asList(ConstantData.getDays(year, month, 1));
				wheelview.setWheelViewAdapter(2, list3);
			}
		} else if (resid == 1) {
			if (wheelviewtype == type_address) {
				int province = wheelview.getSelectedIndex(0);
				int city = wheelview.getSelectedIndex(1);
				List<String> list3 = getVal(2, province, city);
				wheelview.setWheelViewAdapter(2, list3);
			} else if (wheelviewtype == type_date) {
				List<String> list3;
				String year = wheelview.getSelectedItem(0);
				String month = wheelview.getSelectedItem(1);
				list3 = Arrays.asList(ConstantData.getDays(year, month, 1));
				wheelview.setWheelViewAdapter(2, list3);
			}
		} else if (resid == 2) {

		}
	}

	private String[] sexs = { "男", "女" };

	protected void showSexDialog(int resid) {
		Builder bd = new Builder(this);
		bd.setSingleChoiceItems(sexs, 1, new DialogClickListener(resid));
		bd.create().show();
	}

	Builder bd;
	EditText edt;

	protected void showEdtDialog(String name, String val, int resID) {
		edt = new EditText(this);
		edt.setText(val);
		bd = new Builder(this);
		bd.setTitle(name);
		bd.setView(edt);
		bd.setPositiveButton("确定", new DialogClickListener(resID));
		bd.setNegativeButton("取消", new DialogClickListener(resID));
		bd.setCancelable(false);
		bd.create().show();
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {
		int resid = 0;

		public DialogClickListener(int resid) {
			this.resid = resid;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == DialogInterface.BUTTON_POSITIVE) {
				// 确定
				String val = edt.getText().toString();
				changeText(val, resid);
			} else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {
				// 取消
			} else {
				String val = sexs[arg1];
				changeText(val, resid);
			}
			arg0.dismiss();
		}
	};

	protected void dealSubViewClick(TextViewClickBean bean) {
		if (bean != null) {
			int resid = bean.getResid();
			int type = bean.getClicktype();
			String value = bean.getKeyval();
			String name = bean.getKeyname();
			switch (type) {
			case TextViewClickBean.clicktype_address: {
				getWheelView(getWheelViewParent(), resid, type_address);
			}
				break;
			case TextViewClickBean.clicktype_date: {
				getWheelView(getWheelViewParent(), resid, type_date);
			}
				break;
			case TextViewClickBean.clicktype_edt: {
				showEdtDialog(name, value, resid);
			}
				break;
			case TextViewClickBean.clicktype_label: {
				showToast("功能未开发");
			}
				break;
			case TextViewClickBean.clicktype_sex: {
				showSexDialog(resid);
			}
				break;

			default:
				break;
			}
		}
	}

	public static final int setSubViewChange = 10011;

	public  View getWheelViewParent(){
		return getParentView();
	}

	public abstract void changeText(String val, int resid);


}
