package com.example.casting.upload;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.example.casting.me.MeImgs.UploadFileTask;

public class UploadImgTaskManager {

	  private static final String TAG="UploadImgTaskManager";  
	    // UI请求队列  
	    private LinkedList<UploadFileTask> uploadFileTasks;  
	    // 任务不能重复  
	    private Set<String> taskIdSet;  
	  
	    private static UploadImgTaskManager downloadTaskMananger;  
	  
	    private UploadImgTaskManager() {  
	  
	        uploadFileTasks = new LinkedList<UploadFileTask>();  
	        taskIdSet = new HashSet<String>();  
	          
	    }  
	  
	    public static synchronized UploadImgTaskManager getInstance() {  
	        if (downloadTaskMananger == null) {  
	            downloadTaskMananger = new UploadImgTaskManager();  
	        }  
	        return downloadTaskMananger;  
	    }  
	  
	    //1.先执行  
	    public void addDownloadTask(UploadFileTask uploadFileTask) {  
	        synchronized (uploadFileTasks) {  
	            if (!isTaskRepeat(uploadFileTask.getFileUrl())) {  
	                // 增加下载任务  
	                uploadFileTasks.addLast(uploadFileTask);   
	            }  
	        }  
	  
	    }  
	    public boolean isTaskRepeat(String fileId) {  
	        synchronized (taskIdSet) {  
	            if (taskIdSet.contains(fileId)) {  
	                return true;  
	            } else {  
	                System.out.println("下载管理器增加下载任务："+ fileId);  
	                taskIdSet.add(fileId);  
	                return false;  
	            }  
	        }  
	    }  
	      
	    public UploadFileTask getUploadTask() {  
	        synchronized (uploadFileTasks) {  
	            if (uploadFileTasks.size() > 0) {  
	                System.out.println("下载管理器增加下载任务："+"取出任务");  
	                UploadFileTask uploadFileTask = uploadFileTasks.removeFirst();  
	                return uploadFileTask;  
	            }  
	        }  
	        return null;  
	    }  
}
