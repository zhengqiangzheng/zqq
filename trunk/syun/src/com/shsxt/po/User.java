package com.shsxt.po;

import lombok.Data;

/**
 * 用户表
 * @author Lisa Li
 *
 */
@Data
public class User {

	private Integer userId; // 主键，ID
	private String uname; // 用户名
	private String upwd; // 用户密码
	private String nick; // 昵称
	private String head; // 头像
	private String mood; // 心情
	

}
