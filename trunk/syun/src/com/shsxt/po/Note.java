package com.shsxt.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/*
 * 云记表
 */
@Getter @Setter
public class Note {

	private Integer noteId; // 主键，云记ID
	private String title; // 标题
	private String content; // 内容
	private Date pubTime; // 发布时间
	private Integer typeId; // 外键，类型表的主键
    private String typeName;
	

	
}
