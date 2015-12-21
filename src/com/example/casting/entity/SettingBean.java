package com.example.casting.entity;


/**
 * 设置对象
 * @author wf
 *
 */
public class SettingBean extends BaseBean{

	/**
	 * 所有的人、
	 */
	public static final int val1 = 0 ;
	/**
	 * 我关注的人
	 */
	public static final int val2 = 1 ;
	/**
	 * No
	 */
	public static final int val3 = 2 ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 评论权限：所有的人、我关注的人、禁止评论（0、1、2）
	 */
	private int comment = val1;
	/**
	 * 私信权限：所有的人、我关注的人、禁止私信（0、1、2）
	 */
	private int letter = val1;
	/**
	 * 评论提醒：所有的人、我关注的人、都不提醒（0、1、2）
	 */
	private int remind_comment = val1;
	/**
	 * 私信提醒：所有的人、我关注的人、都不提醒（0、1、2）
	 */
	private int remind_letter = val1;
	/**
	 * 被关注提醒：所有的人、我关注的人、都不提醒（0、1、2）
	 */
	private int remind_attention = val1;
	
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getLetter() {
		return letter;
	}
	public void setLetter(int letter) {
		this.letter = letter;
	}
	public int getRemind_comment() {
		return remind_comment;
	}
	public void setRemind_comment(int remind_comment) {
		this.remind_comment = remind_comment;
	}
	public int getRemind_letter() {
		return remind_letter;
	}
	public void setRemind_letter(int remind_letter) {
		this.remind_letter = remind_letter;
	}
	public int getRemind_attention() {
		return remind_attention;
	}
	public void setRemind_attention(int remind_attention) {
		this.remind_attention = remind_attention;
	}

	
	
}
