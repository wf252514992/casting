package com.example.casting.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CastingDBHelper {
	private static CastingDBHelper dbHelper = null;
	SQLiteDatabase database = null;

	private final String castingdb = "casting.db";
	public static CastingDBHelper getInstance(Context ctx) {
		if (dbHelper == null) {
			dbHelper = new CastingDBHelper(ctx);
		}
		return dbHelper;
	}
	
	
	/***
	 * 数据库copy
	 * 
	 * @param context
	 */
	private void initialize(Context context) {
		try {
			String path = database.getPath();
			FileInputStream is = new FileInputStream(new File(path));
			File file = new File(Util.getDBDir()+ "/casting.db");
			file.delete();
			if (!file.exists())
				file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, false);
			byte[] buf = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread <= 0)
					break;
				fos.write(buf, 0, numread);
			} while (true);
			fos.flush();
			fos.close();

			is.close();
			is = null;

		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		database.close();
		dbHelper = null;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 */
	public boolean executeSQL(String sql) {
		try{
			database.execSQL(sql);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @return 游标
	 */
	public Cursor querySQL(String sql) {
		try {
			return database.rawQuery(sql, null);
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 查询
	 * 
	 * @param sql
	 * @return 游标
	 */
	public Cursor querySQL(String sql , String[] whereArg) {
		try {
			return database.rawQuery(sql, whereArg);
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 根据参数进行数据新增
	 * 
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	public long executeInsert(String table, String nullColumnHack,
			ContentValues values) {
		long id = database.insert(table, null, values);
		return id;
	}

	/**
	 * 根据参数进行数据删除
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long executeDelete(String table, String whereClause,
			String[] whereArgs) {
		long id = database.delete(table, whereClause, whereArgs);
		return id;
	}

	/**
	 * 根据参数进行数据查询
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor executeQuery(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor cursor = database.query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy);
		return cursor;
	}

	/***
	 * 根据参数进行数据更新
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long executeUpdate(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		long id = database.update(table, values, whereClause, whereArgs);
		return id;
	}

	private CastingDBHelper(Context context) {
		open(context);
	}
	
	/**
	 * 打开数据库
	 * 
	 * @return 数据库处理器实例
	 */
	private void open( Context context) {
		if (database == null) {
			File file = new File(context.getFilesDir()
					+ "/"
					+castingdb);
			if( !file.exists()){
				copyDB(context);
			}
			if(file.exists()){
				database = SQLiteDatabase.openOrCreateDatabase(file, null);
			}
		}
		
		if( database!=null){
			initialize(context );			
		}
		
	}
	
	private boolean copyDB(Context context) {
		boolean flag = true;
		InputStream in = context.getResources().openRawResource(R.raw.casting);
		ZipInputStream zins = new ZipInputStream(in);
		byte ch[] = new byte[8192];
		try {
			while ((zins.getNextEntry()) != null) {
				FileOutputStream out = context.openFileOutput(castingdb,
						Context.MODE_PRIVATE);
				int i;
				while ((i = zins.read(ch)) != -1)
					out.write(ch, 0, i);
				zins.closeEntry();
				out.close();
			}
			
			in.close();
			zins.close();
		} catch (FileNotFoundException e) {
			Log.e("CastingDB", "文件拷贝错误" + e.getMessage());
			e.printStackTrace();
			flag = false;
		} catch (Exception e) {
			Log.e("CastingDB", "文件拷贝错误" + e.getMessage());
			e.printStackTrace();
			flag = false;
		}
		return flag ;
	}

}
