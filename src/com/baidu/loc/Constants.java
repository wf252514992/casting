package com.baidu.loc;

import com.baidu.location.BDGeofence;

/**

 * @param GEOFENCE_ID 			: 	
 * @param GEOFENCE_COORD_TYPE 	:	
 * @param GEOFENCE_LONGTITUDE	: 	
 * @param GEOFENCE_LATITUDE		:	
 * @param GEOFENCE_RADIUS_TYPE	:	
 * @param GEOFENCE_EXPIRATION	:	
 */
public class Constants {
	public static final String GEOFENCE_ID = "��ƴ���";
	public static final String GEOFENCE_COORD_TYPE = BDGeofence.COORD_TYPE_GCJ;
	public static final double GEOFENCE_LONGTITUDE = 116.30677;
	public static final double GEOFENCE_LATITUDE = 40.04173;
	public static final int GEOFENCE_RADIUS_TYPE = 1;
	public static final long GEOFENCE_EXPIRATION = 10L * (3600 * 1000);
}
