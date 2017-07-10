package com.oumiao.monitor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;

import com.oumiao.monitor.MonitorApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ImageUtils {
	public static final int MAX_WIDTH = 1600;
	public static final int MAX_HEIGHT = 1200;
	/*
	* 图片压缩
	* @param image
	* @return
	*/
	public static Bitmap compressImage(Bitmap image) {
		byte[] temp = compressImageToByte(image);
		ByteArrayInputStream isBm = new ByteArrayInputStream(temp);//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 图片压缩 to byte
	 * @param image
	 * @return
	 */
	public static byte[] compressImageToByte(Bitmap image) {
		return compressImageToByte(image, 300);
	}

	/**
	 *
	 * @param image
	 * @param size 图片压缩大小（单位Kb）
	 * @return
	 */
	public static byte[] compressImageToByte(Bitmap image, int size) {
		if(image == null) return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ((baos.toByteArray().length / 1024) > size) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		}
//        if(!image.isRecycled()){
//        	image.recycle();//释放图片内存
//        	image = null;
//        }
		return baos.toByteArray();
	}

	public static String saveBitmapToFile(Bitmap bitmap){
		try {
			File file = getTempFile();
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.close();
			return file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static File getTempFile() {

		SimpleDateFormat sdf = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		File tempFileDir = new File(MonitorApp.getInstance().getCacheDir(),
				"temp");
		if (!tempFileDir.exists())
			tempFileDir.mkdir();
		File file = new File(tempFileDir, sdf.format(new Date()) + ".jpg");
		return file;
	}

	public static byte[] bitmapToBytes(Bitmap bm) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap bytesToBimap(byte[] b) {
		try {
			if (b.length != 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}


	public static Bitmap drawableToBitmap(Drawable drawable){
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() == PixelFormat.OPAQUE? Bitmap.Config.RGB_565: Bitmap.Config.ARGB_8888;

		Bitmap bitmap = Bitmap.createBitmap(width,height,config);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * 缩小尺寸
	 * @param data
	 * @return
	 */
	public static byte[] compressImageResize(byte[] data){
		ByteArrayInputStream isBm = new ByteArrayInputStream(data);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是1280*1920分辨率，所以高和宽我们设置为
		float hh = 1920f;//这里设置高度为1280f
		float ww = 1080f;//这里设置宽度为720f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0){//如果小于0，代表不需要缩小,直接返回原尺寸
			return data;
		}
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(data);
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float pixels) {
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output == null ? bitmap : output;
	}


	/**
	 * bitmap 缩放
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null)
			return null;
		Bitmap newbmp = null;
		if(width == 0 || height == 0) return bitmap;
		if (MonitorApp.isMethodsCompat(Build.VERSION_CODES.FROYO)) {
			newbmp = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		} else {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) width / w);
			float scaleHeight = ((float) height / h);
			matrix.postScale(scaleWidth, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		}
		return newbmp;
	}


	public static Bitmap uriToBitmap(Context context, Uri uri){
		try {
			return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过文件路径获取图片
	 * @param filePath
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap getFileBitmap(String filePath, int maxWidth, int maxHeight){
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if ((maxWidth == 0) && (maxHeight == 0)) {
			decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeFile(filePath,decodeOptions);
			return bitmap;
		} else {
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			int desiredWidth = getResizedDimension(maxWidth, maxHeight,actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(maxHeight, maxWidth,actualHeight, actualWidth);

			decodeOptions.inJustDecodeBounds = false;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,actualHeight, desiredWidth, desiredHeight);

			Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, decodeOptions);

			if ((tempBitmap != null)
					&& (((tempBitmap.getWidth() > desiredWidth) || (tempBitmap.getHeight() > desiredHeight)))) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
				tempBitmap.recycle();
				return bitmap;
			} else {
				int height = MonitorApp.getInstance().getDisplaySize()[1];
				if((tempBitmap != null) && tempBitmap.getWidth() < maxWidth && desiredHeight <height){
					bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
					if(bitmap != tempBitmap) tempBitmap.recycle();
					return bitmap;
				}else{
					bitmap = tempBitmap;
					return bitmap;
				}
			}
		}
	}


	/**
	 * 自动缩放-按照比例
	 * @param paramsBitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap parsePhoto(Bitmap paramsBitmap, int maxWidth, int maxHeight) {
		byte[] data = bitmapToBytes(paramsBitmap);
		return parsePhoto(data, maxWidth, maxHeight);
	}

	public static Bitmap parsePhoto(byte[] data, int maxWidth, int maxHeight){
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if ((maxWidth == 0) && (maxHeight == 0)) {
			decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					decodeOptions);
		} else {
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			int desiredWidth = getResizedDimension(maxWidth, maxHeight,actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(maxHeight, maxWidth,actualHeight, actualWidth);

			decodeOptions.inJustDecodeBounds = false;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,actualHeight, desiredWidth, desiredHeight);

			Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,data.length, decodeOptions);

			if ((tempBitmap != null)
					&& (((tempBitmap.getWidth() > desiredWidth) || (tempBitmap
					.getHeight() > desiredHeight)))) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
						desiredHeight, true);
				tempBitmap.recycle();
			} else {
				int height = MonitorApp.getInstance().getDisplaySize()[1];
				if(tempBitmap != null && tempBitmap.getWidth() < maxWidth && desiredHeight <height){
					bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
							desiredHeight, true);
					tempBitmap.recycle();
				}else{
					bitmap = tempBitmap;
				}
			}
		}
		return bitmap;
	}

	private static int findBestSampleSize(int actualWidth, int actualHeight,
										  int desiredWidth, int desiredHeight) {
		double wr = actualWidth / desiredWidth;
		double hr = actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0F;
		while (n * 2.0F <= ratio) {
			n *= 2.0F;
		}
		return (int) n;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
										   int actualPrimary, int actualSecondary) {
		if ((maxPrimary == 0) && (maxSecondary == 0)) {
			return actualPrimary;
		}

		if (maxPrimary == 0) {
			double ratio = maxSecondary / (actualSecondary*1.0);
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = actualSecondary / (actualPrimary*1.0);
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	/**
	 * drawable 缩放
	 *
	 * @param drawable
	 * @param width
	 * @param height
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int width, int height) {
		if (drawable == null)
			return null;
		// drawable转换成bitmap
		Bitmap bitmap = drawableToBitmap(drawable);
		Bitmap newBitMap = zoomBitmap(bitmap, width, height);
		return new BitmapDrawable(newBitMap);
	}
}
