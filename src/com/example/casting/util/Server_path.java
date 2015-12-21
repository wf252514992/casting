package com.example.casting.util;

public class Server_path {
	public static final String SavePath="/mnt/sdcard/casting/";
	public static final String SaveVoicePath="/mnt/sdcard/casting_record/";
	public static final String SaveVideoPath="/mnt/sdcard/casting_video/";
//	public static final String serverfile_path="http://192.16 8.1.44:8080/casting/";
	public static final String serverfile_path="http://121.41.35.226:8080/casting/";
	//http://kornan.nat123.net:23424/
	//http://192.168.1.44:8080/
//	public static final String server_path="http://192.168.1.44:8080/casting/services/";
	public static final String server_path="http://121.41.35.226:8080/casting/services/";
//	public static final String server_path="http://meimx.nat123.net:40179/casting/services/";
	/**  
	 * 我要评论或者试镜或者点赞，取消赞
	 */
   public static final String addCommentPraise=server_path+"CommentAndPraiseManage/add?response=application/json";
   /**
    * 获取某动态的评论、赞、试镜
    */
   public static final String getListCommentPraise=server_path+"CommentAndPraiseManage/getList?response=application/json";
   /**
    * 获取某用户的评论、赞、试镜
    */
   public static final String getCommentListCommentPraise=server_path+"CommentAndPraiseManage/getCommentList?response=application/json";
   
   public static final String getAllListCommentPraise=server_path+"CommentAndPraiseManage/getAllList?response=application/json";
   

   /**
    * 获取某条动态评论、赞和报名试镜的数量
    */
   public static final String getAllCountCommentPraise=server_path+"CommentAndPraiseManage/getAllCount?response=application/json";
   /**
    * 发布动态
    */
   public static final String addDynamicManage=server_path+"DynamicManage/add?response=application/json";
   /**
    * 删除某条动态
    */
   public static final String deleteDynamicManage=server_path+"DynamicManage/del";
   /**
    * 获取动态详情
    */
   public static final String getDynamicManage=server_path+"DynamicManage/get?response=application/json";
   /**
    * 获取我动态的数量
    */
   public static final String getCountDynamicManage=server_path+"DynamicManage/getCount?response=application/json";
   /**
    * 获取指定动态的转发列表
    */
   public static final String getForwardListDynamicManage=server_path+"DynamicManage/getForwardList?response=application/json";
   /**
    * 获取我和我关注的动态列表
    */
   public static final String getListDynamicManage=server_path+"DynamicManage/getList?response=application/json";
   /**
    * 获取指定用户的动态列表
    */
   public static final String getUserListDynamicManage=server_path+"DynamicManage/getUserList?response=application/json";
   /**
    * 用户上传文件，文件限制为"jpg,gif,png,mp4,wav"。
    */
   public static final String uploadFile=server_path+"FileManage/uploadJson?response=application/json";

   /**
    * 获取我的关注
    */
   public static final String getAttent=server_path+"AttentionManage/getAttent?response=application/json";
   /**
    * 获取@到我的动态
    */
   public static final String getAtDynamic=server_path+"MentionedManage/getDynamic?response=application/json";
  /**
   * 获取我的视频，动态里上传过的视频 
   */
   public static final String getVideo=server_path+"VideoManage/getList?response=application/json";
   /**
    * 获取我的视频，动态里上传过的的数量
    */
    public static final String getVideoCount=server_path+"VideoManage/getCount?response=application/json";
    /**
     * 获取我的相册，动态里上传过的照片 
     */
     public static final String getPhoto=server_path+"PhotoManage/getList?response=application/json";
     /**
      * 获取我的相册，动态里上传过的照片的数量
      */
      public static final String getPhotoCount=server_path+"PhotoManage/getCount?response=application/json";
      /**
       * 发送私信
       */
      public static final String sendLetter=server_path+"LetterManage/send?response=application/json";
      /**
       * 获取广告
       */
      public static final String getAdmanage=server_path+"AdManage/get?response=application/json";
      /**
       * 上传视频缩略图
       */
      public static final String uploadVideoFile=server_path+"DynamicManage/setVideoImage?response=application/json";
}
