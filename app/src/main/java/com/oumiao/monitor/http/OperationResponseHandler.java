package com.oumiao.monitor.http;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class OperationResponseHandler extends AsyncHttpResponseHandler {

	private Object[] args;

	public OperationResponseHandler(Looper looper, Object... args) {
		super(looper);
		this.args = args;
	}

	@Override
	public void onStart() {
		onStart(args);
	}

	public void onStart(Object... args){

	}

	public OperationResponseHandler(Object... args) {
		this.args = args;
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		onFailure(arg0, arg3.getMessage(), args);
	}

	public void onFailure(int code, String errorMessage, Object[] args) {
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		onSuccess(arg0, TextHttpResponseHandler.getResponseString(arg2,TextHttpResponseHandler.DEFAULT_CHARSET), args);
	}

	public void onSuccess(int code, String responseString, Object[] args) {

	}
}
