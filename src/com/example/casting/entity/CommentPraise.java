package com.example.casting.entity;


/**
 * CommentPraise entity. @author MyEclipse Persistence Tools
 */

public class CommentPraise implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String commentId;
	private String dynamicId;
	private String content;
	private String praise;
	private String audition;
	private String commentDate;
	private String commentType;
	private String nickname;
	private String head_portrait;

	// Constructors

	/** default constructor */
	public CommentPraise() {
	}

	/** minimal constructor */
	public CommentPraise( String dynamicId, String commentType) {
		this.dynamicId = dynamicId;
		this.commentType = commentType;
	}

	/** full constructor */
	public CommentPraise(String dynamicId, String content,
			String praise, String audition, String commentDate,
			String commentType) {
		this.dynamicId = dynamicId;
		this.content = content;
		this.praise = praise;
		this.audition = audition;
		this.commentDate = commentDate;
		this.commentType = commentType;
	}

	// Property accessors
    
	public String getCommentId() {
		return this.commentId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHead_portrait() {
		return head_portrait;
	}

	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}


	public String getDynamicId() {
		return this.dynamicId;
	}

	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPraise() {
		return this.praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getAudition() {
		return this.audition;
	}

	public void setAudition(String audition) {
		this.audition = audition;
	}

	public String getCommentDate() {
		return this.commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentType() {
		return this.commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

}