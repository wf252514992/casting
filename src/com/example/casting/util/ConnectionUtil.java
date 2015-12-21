package com.example.casting.util;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtil {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); 
		if (connectivity == null) { 
			return false; 
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo(); 
			if (info != null) { 
				for (int i = 0; i < info.length; i++) { 
					if (info[i].isConnected()) { 
						return true; 
					}
				}
			}
		}
		return false;
	}

	/*public static void httpTest(final Context ctx, String title, String msg) {
		if (!isNetworkAvailable(ctx)) {
			AlertDialog.Builder builders = new AlertDialog.Builder(ctx);
			builders.setTitle(title);
			builders.setMessage(msg);
			builders.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// alert.dismiss();
						}
					});
			AlertDialog alert = builders.create();
			alert.show();
		}
	}*/

}
