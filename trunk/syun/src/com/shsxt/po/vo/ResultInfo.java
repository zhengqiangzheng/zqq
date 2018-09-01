package com.shsxt.po.vo;


import lombok.Data;

/**
 *
 */
@Data
public class ResultInfo<T> {

	private Integer code; // 状态码  1=成功，0=失败
	private String msg; // 返回的提示信息
	private T result; // 返回的对象（实体类、集合）






}
