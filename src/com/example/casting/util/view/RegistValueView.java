package com.example.casting.util.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.example.casting.processor.ProcessorID;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewDebug.ExportedProperty;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegistValueView extends LinearLayout {

	ImageLoader imgloader;
	private Context mContxt;
	private TextView txt_name;
	private LinearLayout layoutcontent;
	/**
	 * 注册数据类型
	 */
	private String type = type_input_py;
	/**
	 * 展示标头
	 */
	private String title = "";
	/*
	 * 8 提示（主要是针对单位）
	 */
	private String units = "";

	/**
	 * 文本大小
	 */
	private int txtsize = 16;
	/**
	 * 文字颜色
	 */
	private int txtcolor = R.color.black;
	/**
	 * 默认值
	 */
	private String defaultvalue = "";
	/**
	 * 默认显示图片
	 */
	private Drawable defaultsrc;

	public RegistValueView(Context context) {
		super(context);
		initData(context, null);
		// TODO Auto-generated constructor stub
	}

	public RegistValueView(Context context, AttributeSet set) {
		super(context, set);
		initData(context, set);
	}

	public RegistValueView(Context context, AttributeSet set, int rsid) {
		super(context, set, rsid);
		initData(context, set);
	}

	private void initData(Context context, AttributeSet set) {
		mContxt = context;
		imgloader = new ImageLoader(mContxt);
		if (set != null) {
			TypedArray a = context.obtainStyledAttributes(set,
					R.styleable.RegistView);
			this.type = a.getString(R.styleable.RegistView_type);
			this.title = a.getString(R.styleable.RegistView_title);
			this.units = a.getString(R.styleable.RegistView_units);
			// this.txtsize = a.getDimensionPixelSize(
			// R.styleable.RegistView_txtsize, txtsize);
			// this.txtcolor = a.getColor(R.styleable.RegistView_txtcolor,
			// R.color.black);
			this.defaultvalue = a
					.getString(R.styleable.RegistView_defaultvalue);
			if (type == type_img)
				this.defaultsrc = a
						.getDrawable(R.styleable.RegistView_defaultsrc);
			a.recycle();

		}
		initView();
	}

	private String value = "";

	/**
	 * 获取 值
	 * 
	 * @return
	 */
	public String getTextValue() {
		return value;
	}

	/**
	 * Textview 设置值
	 * 
	 * @param v
	 */
	public void setValue(String v) {
		if (v.equals(value))
			return;
		value = v;
		if (this.type.equals(type_img)) {
		} else if (this.type.equals(type_label)) {
		} else {
			if (value.length() > 0 && value.indexOf(units) == -1)
				txtview.setText(value + units);
			else
				txtview.setText(value);
		}
	}

	/**
	 * Textview 设置图片，特意为 认证预留
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void setRegistimg() {
		if (img != null) {
			BitmapDrawable left = new BitmapDrawable(
					BitmapFactory.decodeResource(getResources(),
							R.drawable.mine_special2));
			img.setImageDrawable(left);
		}

	}

	/**
	 * 设置label值
	 * 
	 * @param labels
	 */
	public void setLabel(ArrayList<String> labels) {
		value = labels.toString();
		if (this.type.equals(type_label) && labels != null) {
			if (labels.size() == 1) {
				String v = labels.get(0);
				setLabel(v);
				return;
			} else if (labels.size() > 1) {
				String v = labels.get(0);
				String v2 = labels.get(1);
				label.setText(v);
				label2.setText(v2);
				return;
			}
		}

		label.setText("");
		label2.setText("");
	}

	private void setLabel(String labelval) {
		value = labelval;
		if (this.type.equals(type_label) && labelval.length() > 0) {
			label.setText(labelval);
			label2.setText("");
			return;
		}
		label.setText("");
		label2.setText("");
	}

	/**
	 * 显示图片
	 * 
	 * @param url
	 */
	private void setImageUrl(String url) {
		value = url;
		if (this.type.equals(type_img)) {
			if (url.length() > 0) {
				imgloader.DisplayImage(url, img, false);
			} else {
				setImageDefault();
			}

		}
	}

	/**
	 * 设置图片的显示
	 * 
	 * @param imagbean
	 */
	public void setImage(ImageBean imagbean) {
		try {
			value = "";
			if (this.type.equals(type_img) && imagbean != null) {
				int imgtype = imagbean.getRestype();
				if (imgtype == ImageBean.type_url) {
					value = ProcessorID.uri_headphoto + imagbean.getImg_res();
					setImageUrl(value);
					return;
				} else if (imgtype == ImageBean.type_filepath) {
					value = imagbean.getImg_res();
					img.setImageBitmap(decodeFile(value));
					return;
				} else if (imgtype == ImageBean.type_res) {
					img.setImageResource(Integer.parseInt(imagbean.getImg_res()));
					return;
				}
			}
			setImageDefault();
		} catch (Exception ex) {
			setImageDefault();
		}

	}

	private Bitmap decodeFile(String filepath) {
		try {
			File f = new File(filepath);

			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/**
	 * 显示默认图片
	 */
	private void setImageDefault() {
		value = "";
		if (this.type.equals(type_img)) {
			if (defaultsrc != null)
				img.setImageDrawable(defaultsrc);
			else
				img.setImageResource(R.drawable.me_zhanshidownloadz);
		}
	}

	public void setOnTextChangeListener(TextWatcher watcher) {
		if (this.type.equals(type_img)) {
		} else if (this.type.equals(type_label)) {
		} else {
			txtview.addTextChangedListener(watcher);
		}
	}

	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);;
	LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.MATCH_PARENT);
	LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT, 1);;
	LayoutParams params3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	private void initView() {
		LayoutInflater.from(mContxt).inflate(R.layout.registitemlayout, this,
				true);
		txt_name = (TextView) findViewById(R.id.txt_name);
		layoutcontent = (LinearLayout) findViewById(R.id.layout_content);
		if (units == null)
			units = "";
		txt_name.setText(title);
		if (this.type.equals(type_img)) {
			LinearLayout layout = new LinearLayout(mContxt);
			img = new ImageView(mContxt);
			img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			img.setLayoutParams(params3);
			layout.addView(img, params1);
			layoutcontent.addView(layout, params);
			if (defaultsrc != null) {
				img.setImageDrawable(defaultsrc);
			}
		} else if (this.type.equals(type_label)) {
			LinearLayout layout = new LinearLayout(mContxt);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			label = new LabelView(mContxt);
			label.setText(defaultvalue);
			label2 = new LabelView(mContxt);
			label2.setText(defaultvalue);
			layout.addView(label, params2);
			layout.addView(label2, params2);
			layoutcontent.addView(layout, params);
		} else {
			txtview = new TextView(mContxt);
			txtview.setTag(title);
			txtview.setTextColor(txtcolor);
			txtview.setTextSize(txtsize);
			if (defaultvalue != null)
				txtview.setText(defaultvalue + units);
			txtview.setSingleLine(true);
			txtview.setEllipsize(TruncateAt.END);
			txtview.setGravity(Gravity.CENTER_VERTICAL);
			layoutcontent.addView(txtview, params);
		}
	}

	public boolean isNull() {
		if (value == null || value.length() == 0)
			return true;
		return false;
	}

	TextView txtview;
	ImageView img;
	LabelView label;
	LabelView label2;

	@Override
	@ExportedProperty
	public Object getTag() {
		// TODO Auto-generated method stub
		return title;
	}

	/**
	 * 字符输入
	 */
	public static final String type_input_py = "10";
	/**
	 * 数字输入
	 */
	public static final String type_input_num = "9";
	/**
	 * 汉字输入
	 */
	public static final String type_input_hz = "8";
	/**
	 * 图片输入
	 */
	public static final String type_img = "20";
	/**
	 * 标签输入
	 */
	public static final String type_label = "30";

}
