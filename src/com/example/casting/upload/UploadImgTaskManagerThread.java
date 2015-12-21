package com.example.casting.upload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.casting.me.MeImgs.UploadFileTask;

public class UploadImgTaskManagerThread implements Runnable{
	private UploadImgTaskManager UploadImgTaskManager;  
	  
    // 创建一个可重用固定线程数的线程池  
    private ExecutorService pool;  
    // 线程池大小  
    private final int POOL_SIZE = 1;  
    // 轮询时间  
    private final int SLEEP_TIME = 1000;  
    // 是否停止  
    private boolean isStop = false;  
  
    public UploadImgTaskManagerThread() {  
        UploadImgTaskManager = UploadImgTaskManager.getInstance();  
        pool = Executors.newFixedThreadPool(POOL_SIZE);  
  
    }  
  
    @Override  
    public void run() {  
        // TODO Auto-generated method stub  
        while (!isStop) {  
            UploadFileTask uploadTask = UploadImgTaskManager.getUploadTask();  
            if (uploadTask != null) {  
                pool.execute(uploadTask);  
            } 
//            else {  //如果当前未有downloadTask在任务队列中  
//                try {  
//                    // 查询任务完成失败的,重新加载任务队列  
//                    // 轮询,  
//                    Thread.sleep(SLEEP_TIME);  
//                } catch (InterruptedException e) {  
//                    e.printStackTrace();  
//                }  
//            }  
  
        }  
        if (isStop) {  
            pool.shutdown();  
        }  
  
    }  
    /** 
     * @param isStop 
     *            the isStop to set 
     */  
    public void setStop(boolean isStop) {  
        this.isStop = isStop;  
    }  
}
