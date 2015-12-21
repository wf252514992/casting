package com.example.casting.upload;

import com.example.casting.me.MeImgs.UploadFileTask;

public class Test {

	public static void main(String[] args) {  
        //1.new一个线程管理队列  
        UploadImgTaskManager.getInstance();  
        //2.new一个线程池，并启动  
        UploadImgTaskManagerThread downloadTaskManagerThread = new UploadImgTaskManagerThread();  
        new Thread(downloadTaskManagerThread).start();  
  
        //3.请求下载  
        String []items=new String[]{"向晨宇1","向晨宇2","向晨宇3","向晨宇4","向晨宇5","向晨宇6","向晨宇7","向晨宇1","向晨宇2"};  
  
//        for(int i=0;i<items.length;i++){  
//        	UploadImgTaskManager downloadTaskMananger = UploadImgTaskManager  
//                    .getInstance();  
//            downloadTaskMananger.addDownloadTask(new UploadFileTask(items[i]));  
//            try {  
//                Thread.sleep(2000);  
//            } catch (InterruptedException e) {  
//                e.printStackTrace();  
//            }  
//        }  
    }  

}
