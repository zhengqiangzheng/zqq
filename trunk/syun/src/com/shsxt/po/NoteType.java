package com.shsxt.po;


import lombok.Getter;
import lombok.Setter;

/**
 * 云记类型表
 * @author Lisa Li
 *
 */
   	@Getter@Setter
public class NoteType {
 	/*typeId，主键*/
	private Integer typeId;
	/*类型名称*/
	private String typeName;
	private Integer userId; // 外键，用户表的主键
	

}
