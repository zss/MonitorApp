package com.oumiao.monitor.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.oumiao.monitor.utils.ImageUtils;
import com.oumiao.monitor.utils.StringUtils;
import com.oumiao.monitor.utils.TLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtil {
	private static final String TAG = "HttpUtil";
	private static final String CONTENT_TYPE = "text/html";
	public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
	public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	public static SyncHttpClient syncHttpClient = new SyncHttpClient();
	private static ErrorCode<String> codes = null;


	public static ErrorCode<String> getSyn(String url){
		codes = null;
		TLog.log("url:"+url);
		syncHttpClient.get(url, null, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1, String result) {
				codes = ErrorCode.getErrorCode(statusCode+"", null, result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] arg1, String arg2, Throwable arg3) {
				codes = ErrorCode.getErrorCode(statusCode+"", null, arg2);
			}
		});
		return codes;
	}
	
	public static ErrorCode<String> postSyn(String url, String stringEntity){
		return postSyn(null, url, stringEntity, CONTENT_TYPE);
	}
	
	public static ErrorCode<String> postSyn(String url, String stringEntity, String contentType){
		return postSyn(null, url, stringEntity, contentType);
	}

	public static ErrorCode<String> postSyn(Context context, String url, String stringEntity){
		return postSyn(context, url, stringEntity, CONTENT_TYPE);
	}

	public static void get(Context context, String url, ResponseHandlerInterface asyncHttpResponseHandler){
		TLog.log("url:" + url);
		asyncHttpClient.get(context,url, asyncHttpResponseHandler);
	}

	public static void get(String url, ResponseHandlerInterface asyncHttpResponseHandler){
		TLog.log("url:" + url);
		asyncHttpClient.get(url, asyncHttpResponseHandler);
	}

	
	public static void post(Context context, String url, AsyncHttpResponseHandler responseHandler){
		post(context, url, "", CONTENT_TYPE, responseHandler);
	}
	
	public static void post(Context context, String url, String stringEntity, AsyncHttpResponseHandler responseHandler){
		post(context, url, stringEntity, CONTENT_TYPE,responseHandler);
	}
	
	public static ErrorCode<String> postSyn(Context context, String url, String stringEntity, String contentType){
		TLog.log("reqParams:" + stringEntity);
		TLog.log("url:" + url);
		codes = null;
		StringEntity stringEntity2 = null;
		if(StringUtils.isNotEmpty(stringEntity)){
			stringEntity2 = new StringEntity(stringEntity,"UTF-8");
		}
		syncHttpClient.post(context, url, stringEntity2, contentType, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1, String result) {
				codes = ErrorCode.getErrorCode(statusCode + "", null, result);
			}

			@Override
			public void onFailure(int statusCode, Header[] arg1, String arg2, Throwable arg3) {
				codes = ErrorCode.getErrorCode(statusCode + "", null, arg2);
			}
		});
		return codes;
	}
	
	public static void post(Context context, String url, String stringEntity, String contentType, AsyncHttpResponseHandler responseHandler){
		try {
			TLog.log("url:" + url);
			TLog.log("reqParams:" + stringEntity);
			StringEntity stringEntity2 = null;
			if(StringUtils.isNotEmpty(stringEntity)){
				stringEntity2 = new StringEntity(stringEntity,"UTF-8");
			}
			asyncHttpClient.post(context,url,stringEntity2,contentType,responseHandler);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void post(String url, String stringEntity, String contentType, AsyncHttpResponseHandler responseHandler){
		post(null, url, stringEntity, contentType, responseHandler);
	}

	public static void post(String url, String stringEntity, AsyncHttpResponseHandler responseHandler){
		post(null, url, stringEntity,CONTENT_TYPE, responseHandler);
	}
	
	public static ErrorCode<String> upLoadImage(String uploadUrl, Bitmap bitmap){
		return upLoadImage(uploadUrl, bitmap, 300);
	}
	
	public static void cancelRequest(Context context){
		asyncHttpClient.cancelRequests(context, true);
	}
	
	public static void cancelAllRequest(){
		asyncHttpClient.cancelAllRequests(true);
	}
	
	public static ErrorCode<String> upLoadImage(String uploadUrl, Bitmap bitmap, int compressSize){
		HttpURLConnection conn = null;
        URL url;
		try {
			url = new URL(uploadUrl);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setRequestMethod("POST");
	        conn.setConnectTimeout(10000);
	        conn.setReadTimeout(10000);
	        conn.setRequestProperty("charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", "text/plain");
	        conn.setRequestProperty("connection", "keep-alive");
	        //压缩大小
	        byte[] byteArray = ImageUtils.compressImageToByte(bitmap, compressSize);
	        //压缩尺寸
	        byteArray = ImageUtils.compressImageResize(byteArray);
	        if(byteArray == null) return ErrorCode.getErrorCode("-1", "bitmap is null!", null);;
	        conn.connect();
	        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	        wr.write(byteArray);
	        wr.flush();
	        wr.close();
	        int statusCode = conn.getResponseCode();
	        String resResult="";
	        if(statusCode == 200){
		        InputStream inputStream = conn.getInputStream();
		        byte[] byteResult = readInputStream(inputStream);
		        if(byteResult != null){
		        	resResult = new String(byteResult,"utf-8");
		        }
	        }
	        return ErrorCode.getErrorCode(statusCode+"", "", resResult);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ErrorCode.getErrorCode("-1", e.getMessage(), null);
		} catch (IOException e) {
			e.printStackTrace();
			return ErrorCode.getErrorCode("-1", e.getMessage(), null);
		}finally{
			if(conn != null) conn.disconnect();
		}
	}
	
	
	
	private static byte[] readInputStream(InputStream inStream){
		try{
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while( (len = inStream.read(buffer)) !=-1 ){
	            outStream.write(buffer, 0, len);
	        }
	        byte[] data = outStream.toByteArray();//网页的二进制数据
	        outStream.close();
	        inStream.close();
	        return data;
		}catch(IOException e){
			return null;
		}
    }
	
	public static ErrorCode<String> postSyn(String url, RequestParams requestParams){
		codes = null;
		TLog.log("url:" + url);
		syncHttpClient.post(url, requestParams, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				codes = ErrorCode.getErrorCode(statusCode+"", null, responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				codes = ErrorCode.getErrorCode(statusCode+"", null, responseString);
			}
		});
		return codes;
	}

}
