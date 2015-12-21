package com.example.casting.login.start;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StartFlashActivity extends BaseForm 
{

	/**
	 * alphaAnimation:明度渐变的动画
	 * alphaAnimation2：明度不变的动画
	 */
	AlphaAnimation alphaAnimation,alphaAnimation2;
	ViewPager viewPager;
	ViewPagerAdapter viewAdapter;
	/** * viewpager要显示的界面 */
	ArrayList<View> views = new ArrayList<View>();
	/**进入主界面按钮*/
	Button btn_tomain;
	/**ViewPager当前显示的Item*/
	int currentId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startflash);
		initImage();
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		/**初始化ViewPagerScorller，将其与viewpager绑定*/
		initViewPagerScroll();
//		initAnimation();
		viewAdapter= new ViewPagerAdapter(this, views);
		viewPager.setAdapter(viewAdapter);
		viewPager.setCurrentItem(0);
		
	}
	/**
	 * 初始化各个界面中的图片
	 */
	public void initImage()
	{
		for(int i = 0;i<3;i++)
		{
			View view = getLayoutInflater().inflate(R.layout.img, null);
			ImageView imgview = (ImageView) view.findViewById(R.id.img);
			if(i==0)
			{
				imgview.setImageResource(R.drawable.one);
			}
			else if(i==1)
			{
				imgview.setImageResource(R.drawable.two);
			}
			else if(i==2){
				btn_tomain = (Button) view.findViewById(R.id.btn_tomain);
				btn_tomain.setVisibility(View.VISIBLE);
				imgview.setImageResource(R.drawable.three);
			}
			views.add(view);
		}
	}
	/**
     * 设置ViewPager的滑动速度
     * 
     * */
    private void initViewPagerScroll( ){
    try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
            ViewPagerScorller scroller = new ViewPagerScorller( viewPager.getContext( ) );
            mScroller.set( viewPager, scroller);
        }catch(NoSuchFieldException e){
       
        }catch (IllegalArgumentException e){
       
        }catch (IllegalAccessException e){
       
        }
    }
    /**动画监听，界面的自动跳转也在此处实现*/
	public class MyanimationListener implements AnimationListener
	{

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if(currentId < 2)
			{
		    	currentId++;
		    	viewPager.startAnimation(alphaAnimation2);  
		    	viewPager.setCurrentItem(currentId, true);
			}
			else
			{
				viewPager.clearAnimation();
			}
			
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
	}
	public void initAnimation()
	{
		alphaAnimation = new AlphaAnimation(0.3f, 1.0f);  
		alphaAnimation.setDuration(3000);// 设置动画显示时间  
		viewPager.startAnimation(alphaAnimation);  
		alphaAnimation.setAnimationListener(new MyanimationListener());
		
		alphaAnimation2 = new AlphaAnimation(1.0f, 1.0f);  
		alphaAnimation2.setDuration(3000);// 设置动画显示时间  
		alphaAnimation2.setAnimationListener(new MyanimationListener());
	}
	public void toMainForm(View v) {
		if(v.getId() == R.id.btn_tomain)
		{
			doActivity(R.string.loginform);;
			finish();
		}
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}

}
