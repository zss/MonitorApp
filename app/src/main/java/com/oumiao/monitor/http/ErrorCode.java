package com.oumiao.monitor.http;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Map;

public class ErrorCode<T> implements Serializable {
	public static final String CODE_SUCCESS = "200";
	public static final String CODE_NOT_REQUEST = "404";
	public static final String CODE_FORWORD_REQUEST = "302";
	public static final String CODE_REQUEST_ERROR = "503";
	private static final long serialVersionUID = 4418416282894231647L;
	
	private String errcode;
	private String msg;
	private T retval;
	private boolean success = false;
	private ErrorCode(String code, String msg, T retval){
		this.errcode = code;
		this.msg = msg;
		this.retval = retval;
		this.success = TextUtils.equals(code, CODE_SUCCESS);
	}


	public static ErrorCode NOT_FOUND = new ErrorCode(CODE_NOT_REQUEST, "请求路径不存在！", null);
	public static ErrorCode DATEERROR = new ErrorCode(CODE_REQUEST_ERROR, "网络请求有误！", null);
	public static ErrorCode SUCCESS = new ErrorCode(CODE_SUCCESS, null, null);
	
	
	@Override
	public boolean equals(Object another){
		if(another == null || !(another instanceof ErrorCode)) return false;
		return this.errcode== ((ErrorCode)another).errcode;
	}
	@Override
	public int hashCode() {
		return (this.success + this.errcode + this.msg).hashCode();
	}

	public boolean isSuccess(){
		return success;
	}
	
	public static ErrorCode getErrorCode(String code, String msg, Object retval){
		if(TextUtils.equals(code, CODE_SUCCESS) || TextUtils.equals(code, CODE_FORWORD_REQUEST)) return new ErrorCode(code, msg, retval);
		else if(TextUtils.equals(code, CODE_NOT_REQUEST)) return NOT_FOUND;
		else return DATEERROR;
	}
	
	
	
	public T getRetval() {
		return retval;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setRetval(T retval) {
		this.retval = retval;
	}
	public String getMsg() {
		return msg;
	}
	public void put(Object key, Object value){
		((Map)retval).put(key, value);
	}
	
	public String getErrcode() {
		return errcode;
	}
}

