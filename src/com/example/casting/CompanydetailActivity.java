package com.example.casting;

import com.example.casting_android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

/**
 * 用户认证主页
 * @author chenjiaping
 *
 */
public class CompanydetailActivity extends FragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	View view = View.inflate(this, R.layout.companydetail, null);
    	setContentView(view);
    	showFragmentContent(companydetailitem1.getInstance(),"companydetailitem1");
    }
    /**
   	 * 切换列表Fragment显示
   	 * 
   	 * @param fragment
   	 */
   	public void showFragmentContent(Fragment fragment,String fragmentName) {
   		final FragmentManager manager = getSupportFragmentManager();
   		manager.popBackStack();
   		FragmentTransaction ft = manager.beginTransaction();
   		Log.e("mjq", "ft.toString() " + ft.toString());
   		ft.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
   		if (!fragment.isAdded()) {
   			ft.add(R.id.fragment_layot, fragment,fragmentName);
   		}
   		if(fragment == companydetailitem1.getInstance()){
   			ft.show(fragment).commit();
   		}
//   			else if(fragment == MyAccountActivity.getInstance()){
//   			ft.hide(mainPageFragment.getInstance())
//   			.hide(OrderDetail4Activity.getInstance())
//   			.hide(SetActivity.getInstance()).show(fragment).commit();
//   		}else if(fragment == OrderDetail4Activity.getInstance()){
//   			ft.hide(MyAccountActivity.getInstance())
//   			.hide(mainPageFragment.getInstance())
//   			.hide(SetActivity.getInstance()).show(fragment).commit();
//   		}else if(fragment == SetActivity.getInstance()){
//   			ft.hide(MyAccountActivity.getInstance())
//   			.hide(OrderDetail4Activity.getInstance())
//   			.hide(mainPageFragment.getInstance()).show(fragment).commit();
//   		}else if(fragment == SetActivity.getInstance()){
//   			ft.hide(MyAccountActivity.getInstance())
//   			.hide(OrderDetail4Activity.getInstance())
//   			.hide(mainPageFragment.getInstance()).show(fragment).commit();
//   		}
//   		ft.hide(mainPageFragment.getInstance())
//   				.hide(MyAccountActivity.getInstance())
//   				.hide(OrderDetail4Activity.getInstance())
//   				.hide(SetActivity.getInstance()).show(fragment).commit();
   	}
    
}
