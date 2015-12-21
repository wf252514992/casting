package com.example.casting.login;

import com.example.casting.MainTabNew;
import com.example.casting.login.findpwd.FindPWDSuccessForm;
import com.example.casting.login.findpwd.FindPWDThirdForm;
import com.example.casting.login.regist.ChangeBgActivity;
import com.example.casting.login.regist.CompanyRegistActivity;
import com.example.casting.login.regist.RegistDetailActivity;
import com.example.casting.login.regist.UpdateIntrouction;
import com.example.casting.login.regist.UpdateLabelForm;
import com.example.casting.login.regist.UpdateLabelNForm;
import com.example.casting.login.regist.UserInfoActivity;
import com.example.casting.login.regist.UserInfoEditActivity;
import com.example.casting.login.regist.UserInfoShowActivity;
import com.example.casting.login.regist.winner.WinnerActivity;
import com.example.casting.login.regist.winner.WinnerAddActivity;
import com.example.casting.login.regist.winner.WinnerShowActivity;
import com.example.casting.login.regist.works.WorksActivity;
import com.example.casting.login.regist.works.WorksAddActivity;
import com.example.casting.login.regist.works.WorksShowActivity;
import com.example.casting.me.AttentionListActivity;
import com.example.casting.me.MeActivity;
import com.example.casting.me.MeDynamic;
import com.example.casting.me.MyDataActivity;
import com.example.casting.me.MyFansActivity;
import com.example.casting.me.MyInfoActivity;
import com.example.casting.me3.otherwatch.OtherWatchCompanyActivity;
import com.example.casting.me3.otherwatch.OtherWatchDirectorActivity;
import com.example.casting.me3.otherwatch.OtherWatchMeActivity;
import com.example.casting.square.SquareActivity;
import com.example.casting.xtsz.AboutMeActivity;
import com.example.casting.xtsz.FeedBackActivity;
import com.example.casting.xtsz.ManagerIDsActivity;
import com.example.casting.xtsz.NotifactionActivity;
import com.example.casting.xtsz.Set_AboutmeActivity;
import com.example.casting.xtsz.Set_AttentionActivity;
import com.example.casting.xtsz.Set_FansActivity;
import com.example.casting.xtsz.Set_ReviewActivity;
import com.example.casting.xtsz.XtszActivity;
import com.example.casting_android.R;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetworkStateReceiver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;

public abstract class CastingBaseForm extends Activity {

	private Boolean networkAvailable = false;
	TANetChangeObserver taNetChangeObserver = new TANetChangeObserver() {
		@Override
		public void onConnect(netType type) {
			networkAvailable = true;
			onNetConnect(type);
		}

		@Override
		public void onDisConnect() {
			networkAvailable = false;
			onNetDisConnect();

		}
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TANetworkStateReceiver.registerObserver(taNetChangeObserver);
	};

	/**
	 * 
	 * @param activity
	 *            需跳转的activity
	 * @param bd
	 *            参数
	 * @param requestcode
	 */
	public void doActivity(int activity, Bundle bd, int requestcode) {
		Intent intent = getIntent(activity);
		intent.putExtras(bd);
		startActivityForResult(intent, requestcode);
	}

	/**
	 * 
	 * @param activity
	 * @param requestcode
	 */
	public void doActivity(int activity, int requestcode) {
		Intent intent = getIntent(activity);
		startActivityForResult(intent, requestcode);
	}

	public void doActivity(int activity, Bundle bd) {
		Intent intent = getIntent(activity);
		intent.putExtras(bd);
		startActivity(intent);

	}

	public void doActivity(int activity) {
		Intent intent = getIntent(activity);
		startActivity(intent);
	}

	private Intent getIntent(int activity) {
		Intent intent = new Intent();
		switch (activity) {
		case R.string.castingfirstform:
			intent.setClass(this, CastingFirstForm.class);
			break;
		case R.string.userinfoactivity:
			intent.setClass(this, UserInfoActivity.class);
			break;
		case R.string.xtszactivity:
			intent.setClass(this, XtszActivity.class);
			break;
		case R.string.findpwdthirdform:
			intent.setClass(this, FindPWDThirdForm.class);
			break;
		case R.string.findpwdsuccessform:
			intent.setClass(this, FindPWDSuccessForm.class);
			break;
		case R.string.manageridsactivity:
			intent.setClass(this, ManagerIDsActivity.class);
			break;
		case R.string.loginform:
			intent.setClass(this, LoginForm.class);
			break;
		case R.string.notifactionactivity:
			intent.setClass(this, NotifactionActivity.class);
			break;
		case R.string.feedbackactivity:
			intent.setClass(this, FeedBackActivity.class);
			break;
		case R.string.aboutmeactivity:
			intent.setClass(this, AboutMeActivity.class);
			break;
		case R.string.set_aboutmeactivity:
			intent.setClass(this, Set_AboutmeActivity.class);
			break;
		case R.string.set_attentionactivity:
			intent.setClass(this, Set_AttentionActivity.class);
			break;
		case R.string.set_fansactivity:
			intent.setClass(this, Set_FansActivity.class);
			break;
		case R.string.set_reviewactivity:
			intent.setClass(this, Set_ReviewActivity.class);
			break;
		case R.string.squareactivity:
			intent.setClass(this, SquareActivity.class);
			break;
		case R.string.meactivity:
			intent.setClass(this, MeActivity.class);
			break;
		case R.string.myinfoactivity:
			intent.setClass(this, MyInfoActivity.class);
			break;
		case R.string.mydataactivity:
			intent.setClass(this, MyDataActivity.class);
			break;
		case R.string.attentionlistactivity:
			intent.setClass(this, AttentionListActivity.class);
			break;
		case R.string.updateintrouction:
			intent.setClass(this, UpdateIntrouction.class);
			break;
		case R.string.updatelabelform:
			intent.setClass(this, UpdateLabelForm.class);
			break;
		case R.string.registdetailactivity:
			intent.setClass(this, RegistDetailActivity.class);
			break;
		case R.string.companyregistactivity:
			intent.setClass(this, CompanyRegistActivity.class);
			break;
		case R.string.myfansactivity:
			intent.setClass(this, MyFansActivity.class);
			break;
		case R.string.MeDynamic:
			intent.setClass(this, MeDynamic.class);
			break;
		case R.string.worksactivity:
			intent.setClass(this, WorksActivity.class);
			break;
		case R.string.worksaddactivity:
			intent.setClass(this, WorksAddActivity.class);
			break;
		case R.string.winneractivity:
			intent.setClass(this, WinnerActivity.class);
			break;
		case R.string.winneraddactivity:
			intent.setClass(this, WinnerAddActivity.class);
			break;
		case R.string.maintabnew:
			intent.setClass(this, MainTabNew.class);
			break;
		case R.string.userinfoeditactivity:
			intent.setClass(this, UserInfoEditActivity.class);
			break;
		case R.string.userinfoshowactivity:
			intent.setClass(this, UserInfoShowActivity.class);
			break;
		case R.string.otherwatchmeactivity:
			intent.setClass(this, OtherWatchMeActivity.class);
			break;
		case R.string.otherwatchdirectoractivity:
			intent.setClass(this, OtherWatchDirectorActivity.class);
			break;
		case R.string.otherwatchcompanyactivity:
			intent.setClass(this, OtherWatchCompanyActivity.class);
			break;
		case R.string.changebgactivity:
			intent.setClass(this, ChangeBgActivity.class);
			break;
		case R.string.updatelabelnform:
			intent.setClass(this, UpdateLabelNForm.class);
			break;
		case R.string.loginaddidform:
			intent.setClass(this, LoginAddIDForm.class);
			break;
		case R.string.worksshowactivity:
			intent.setClass(this, WorksShowActivity.class);
			break;
		case R.string.winnershowactivity:
			intent.setClass(this, WinnerShowActivity.class);
			break;
		default:
			intent.setClass(this, LoginForm.class);
			break;
		}
		return intent;
	}

	/**
	 * 退出系统
	 * 
	 * @param isBackground
	 */
	public void existApp(Boolean isBackground) {
		try {
			ActivityManager activityMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			activityMgr.restartPackage(getPackageName());
		} catch (Exception e) {

		} finally {
			// 注意，如果您有后台程序运行，请不要支持此句子
			if (!isBackground) {
				System.exit(0);
			}
		}
	}

	public void onNetConnect(netType type) {
	};

	public void onNetDisConnect() {
	};
}
