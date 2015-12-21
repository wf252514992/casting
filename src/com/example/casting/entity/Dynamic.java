package com.example.casting.entity;


public class Dynamic implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private String id;
	private String usertype="";
	private static final long serialVersionUID = 1L;
	private String dynamicId;
	private String title;
	private String content;
	private String fileType;
	private boolean isForwarding;
	private String forwarding_content;
	private String forwardingId;
	private String imageUrl;
	private String videoUrl;
	private String voiceUrl;
	private String SImageUrl;
	private String upToDate;
	private String type;
	private String dynamicTime;
	private String praises;
	private String comments;
	private String forwardeds;
	private String recruits;
	private String duration;
	private boolean is_praise;
	private String nickname;
	private String head_portrait;
	private String commentDate;
	private String commentContent;
	private String recruit_state;
	private String location;
	private String open_permissions;
	private String commenter_head_portrait;
	private String commenter_nickname;
	private String certification;

	// Constructors

	/** default constructor */
	public Dynamic() {
	}

	/** minimal constructor */
	public Dynamic( String type, String dynamicTime) {
		this.type = type;
		this.dynamicTime = dynamicTime;
	}

	/** full constructor */
	public Dynamic(String title, String content,
			String fileType, boolean isForwarding,String forwarding_content, String forwardingId,
			String imageUrl, String videoUrl, String voiceUrl,
			String SImageUrl, String upToDate, String type,
			String dynamicTime) {
		this.title = title;
		this.content = content;
		this.fileType = fileType;
		this.isForwarding = isForwarding;
		this.forwarding_content=forwarding_content;
		this.forwardingId = forwardingId;
		this.imageUrl = imageUrl;
		this.videoUrl = videoUrl;
		this.voiceUrl = voiceUrl;
		this.SImageUrl = SImageUrl;
		this.upToDate = upToDate;
		this.type = type;
		this.dynamicTime = dynamicTime;
	}

	// Property accessors
    
	public String getDynamicId() {
		return this.dynamicId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}


	public String getHead_portrait() {
		return head_portrait;
	}

	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean getIsForwarding() {
		return this.isForwarding;
	}

	public void setIsForwarding(boolean isForwarding) {
		this.isForwarding = isForwarding;
	}
    
	public String getForwarding_content() {
		return forwarding_content;
	}

	public void setForwarding_content(String forwarding_content) {
		this.forwarding_content = forwarding_content;
	}


	public String getForwardingId() {
		return this.forwardingId;
	}

	public void setForwardingId(String forwardingId) {
		this.forwardingId = forwardingId;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVideoUrl() {
		return this.videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVoiceUrl() {
		return this.voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String getSImageUrl() {
		return this.SImageUrl;
	}

	public void setSImageUrl(String SImageUrl) {
		this.SImageUrl = SImageUrl;
	}

	public String getUpToDate() {
		return this.upToDate;
	}

	public void setUpToDate(String upToDate) {
		this.upToDate = upToDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDynamicTime() {
		return dynamicTime;
	}

	public void setDynamicTime(String dynamicTime) {
		this.dynamicTime = dynamicTime;
	}

	public String getPraises() {
		return praises;
	}

	public void setPraises(String praises) {
		this.praises = praises;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getForwardeds() {
		return forwardeds;
	}

	public void setForwardeds(String forwardeds) {
		this.forwardeds = forwardeds;
	}

	public String getRecruits() {
		return recruits;
	}

	public void setRecruits(String recruits) {
		this.recruits = recruits;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public boolean isIs_praise() {
		return is_praise;
	}

	public void setIs_praise(boolean is_praise) {
		this.is_praise = is_praise;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setForwarding(boolean isForwarding) {
		this.isForwarding = isForwarding;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getRecruit_state() {
		return recruit_state;
	}

	public void setRecruit_state(String recruit_state) {
		this.recruit_state = recruit_state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOpen_permissions() {
		return open_permissions;
	}

	public void setOpen_permissions(String open_permissions) {
		this.open_permissions = open_permissions;
	}

	public String getCommenter_head_portrait() {
		return commenter_head_portrait;
	}

	public void setCommenter_head_portrait(String commenter_head_portrait) {
		this.commenter_head_portrait = commenter_head_portrait;
	}

	public String getCommenter_nickname() {
		return commenter_nickname;
	}

	public void setCommenter_nickname(String commenter_nickname) {
		this.commenter_nickname = commenter_nickname;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}
	

}