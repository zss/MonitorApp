package com.oumiao.monitor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.oumiao.monitor.base.BaseApplication;


public class NetworkUtils {

	public static boolean hasInternet() {
		ConnectivityManager connectivityManager = ((ConnectivityManager) BaseApplication.context().getSystemService("connectivity"));
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isAvailable();
	}

	public static WifiInfo getWifiInfo(Context paramContext)
	{
		WifiManager localWifiManager = (WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE);
		if (localWifiManager != null)
			return localWifiManager.getConnectionInfo();
		return null;
	}

	public static boolean isWifiConnection(Context var0) {
		ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo var2 = var1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (var2 != null && var2.isConnected()) {
			TLog.log("net", "wifi is connected");
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobileConnection(Context var0) {
		ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo var2 = var1.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (var2 != null && var2.isConnected()) {
			TLog.log("net", "wifi is connected");
			return true;
		} else {
			return false;
		}
	}

	public static String getWifiSSID(Context paramContext)
	{
		WifiInfo localWifiInfo = getWifiInfo(paramContext);
		if (localWifiInfo != null) {
			String ssid = localWifiInfo.getSSID();

			TLog.log("SSID:" + ssid + "\n BSSID:" + localWifiInfo.getBSSID()
					+ "\n IP:" + localWifiInfo.getIpAddress()
					+ "\n Mac:" + localWifiInfo.getMacAddress());
			return removeDoubleQuotes(ssid);
		}
		return "";
	}

	/**
	 * 获取当前连接WIFI路由器mac地址
	 * XX:XX:XX:XX:XX:XX
	 *
	 * @param paramContext
	 * @return
	 */
	public static String getWifiRouterMacAddr(Context paramContext) {
		WifiInfo localWifiInfo = getWifiInfo(paramContext);
		if (localWifiInfo != null) {
			return localWifiInfo.getBSSID();
		}
		return "";
	}

	public static String intToIp(int ip) {
		return android.text.format.Formatter.formatIpAddress(ip);
	}

	/**
	 * XX:XX:XX:XX:XX:XX -> XXXXXXXXXXXX
	 * XX-XX-XX-XX-XX-XX -> XXXXXXXXXXXX
	 *
	 * @return
	 * @throws IllegalAccessException
	 */
	public static String formatMacAddress(String macAddress) {
		if (TextUtils.isEmpty(macAddress)) return null;
		if (macAddress.contains(":") || macAddress.contains("：")) {
			return macAddress.replace(":", "").replace("：", "");
		} else if (macAddress.contains("-") || macAddress.contains("-")) {
			return macAddress.replace("-", "").replace("-", "");
		}
		return macAddress;
	}

	/**
	 * 用于格式化WIFI SSID信息
	 *
	 * @param paramString
	 * @return
	 */
	public static String removeDoubleQuotes(String paramString) {
		if (paramString != null) {
			int i = paramString.length();
			if ((i > 1) && (paramString.charAt(0) == '"')
					&& (paramString.charAt(i - 1) == '"'))
				return paramString.substring(1, i - 1);
		}
		return paramString;
	}

	public static NetworkInfo getWifiNetworkInfo(Context paramContext)
	{
		ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (localConnectivityManager != null)
			return localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return null;
	}

	public static String getNetworkType(Context var0) {
		ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo var2 = var1.getActiveNetworkInfo();
		if (var2 != null && var2.isAvailable()) {
			int var3 = var2.getType();
			if (var3 == 1) {
				return "WIFI";
			} else {
				TelephonyManager var4 = (TelephonyManager) var0.getSystemService(Context.TELEPHONY_SERVICE);
				switch (var4.getNetworkType()) {
					case 1:
					case 2:
					case 4:
					case 7:
					case 11:
						return "2G";
					case 3:
					case 5:
					case 6:
					case 8:
					case 9:
					case 10:
					case 12:
					case 14:
					case 15:
						return "3G";
					case 13:
						return "4G";
					default:
						return "unkonw network";
				}
			}
		} else {
			return "no network";
		}
	}
}
