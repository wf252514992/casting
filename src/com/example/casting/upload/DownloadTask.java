package com.example.casting.upload;

import com.example.casting.login.BaseForm;

public class DownloadTask implements Runnable{
	public String name;  
    public DownloadTask(String name){  
        this.name=name;  
    }  
    @Override  
    public void run() {  
        try {  
            Thread.sleep(1500);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        System.out.println(name + " executed OK!");  
    }  
    public String getFileId(){  
        return name;  
    }  

}
