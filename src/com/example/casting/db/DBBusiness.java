package com.example.casting.db;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBBusiness {


	CastingDBHelper dbhelp ;
	public DBBusiness(Context context){
		dbhelp = CastingDBHelper.getInstance(context);
	}
	
	/**
	 * 根据sql语句查询数据集
	 * @param sql
	 * @return 将字段以键值对形式存放hashmap中
	 */
	public ArrayList<HashMap<String, String>> getArrayListBySQl(String sql) {
		return treatmentResult(dbhelp.querySQL(sql));
	}
	
	public ArrayList<HashMap<String, String>> getArrayListBySQL(String sql ,String[] whereArg){
		return treatmentResult(dbhelp.querySQL(sql,whereArg));
	}
	/***
	 * 根据参数查询数据集
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return 将字段以键值对形式存放hashmap中
	 */
	public ArrayList<HashMap<String, String>> getArrayList(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		return treatmentResult(dbhelp.executeQuery(table, columns, selection, selectionArgs, groupBy, having, orderBy));
	}
	
	/***
	 * 进行数据处理封装在map中
	 * @param cursor
	 * @return
	 */
	private ArrayList<HashMap<String, String>> treatmentResult(Cursor cursor) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					try {
						HashMap<String, String> m = new HashMap<String, String>();
						int cols = cursor.getColumnCount();
						for (int i = 0; i < cols; i++) {
							String cn = cursor.getColumnName(i);
							String cv = cursor.getString(i);
							m.put(cn, cv);
						}
						list.add(m);
						cursor.moveToNext();
					} catch (Exception ex) {
						break;
					}
				}
			}
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return list;
	}
	
	public boolean executeSQL (String sql ){
		return dbhelp.executeSQL(sql);
	}
	
	public long executeInsert(String table, String nullColumnHack, ContentValues values){
		return dbhelp.executeInsert(table, nullColumnHack, values) ;
	}	
	public long executeDelete(String table, String whereClause, String[] whereArgs){
		return dbhelp.executeDelete(table, whereClause, whereArgs);
	}
	public long executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs){
		return dbhelp.executeUpdate(table, values, whereClause, whereArgs);
	}
	public boolean executeDelete(String sql){
		return dbhelp.executeSQL(sql);
	}


	
}
