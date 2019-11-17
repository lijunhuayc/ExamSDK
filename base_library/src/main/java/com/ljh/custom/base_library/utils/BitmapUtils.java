package com.ljh.custom.base_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Desc: Bitmap 工具类 adapter
 * Created by ${junhua.li} on 2016/05/10 10:51.
 * Email: lijunhuayc@sina.com
 */
public class BitmapUtils {
    private final static int compressVal = 75;

    /**
     * 将指定路径图片压缩并旋转正位置后返回Bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap$Rotate(String filePath) {
        Bitmap bm = getSmallBitmapOnly(filePath);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        return rotateBitmapOnly(bm, degree);
    }

    /**
     * 将指定路径图片压缩并旋转正位置后返回Bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap$Rotate(String filePath, int reqWidth, int reqHeight) {
        Bitmap bm = getSmallBitmapOnly(filePath, reqWidth, reqHeight);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        return rotateBitmapOnly(bm, degree);
    }

    /**
     * 将指定路径图片压缩并旋转正位置后保存到原来位置
     *
     * @param filePath
     */
    public static String smallBitmap$Rotate(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        Bitmap bm = getSmallBitmapOnly(filePath);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmapOnly(bm, degree);
        String cacheD = FileUtils.getExternalCacheDirChildPath(FileUtils.UPLOAD_CACHE_DIR);
        String cachePath = cacheD + File.separator + UUID.randomUUID().toString() + "." + FileUtils.getFileExtension(filePath);
        saveBitmap2FilePath(bm, cachePath);
        return cachePath;
    }

    /**
     * 保存bitmap 到文件
     *
     * @param bitmap
     * @param savePath
     */
    public static void saveBitmap2FilePath(Bitmap bitmap, String savePath) {
        Timber.d("saveBitmap2FilePath: bitmap=%s, savePath=%s", null == bitmap, savePath);
        if (null == bitmap || TextUtils.isEmpty(savePath)) {
            Timber.d("bitmap or savePath is null.");
            return;
        }
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            Timber.d("saveBitmap2FilePath: 创建文件");
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Timber.d("saveBitmap2FilePath: 文件已存在");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bitmap.recycle();
                System.gc();
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 仅仅压缩图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmapOnly(String filePath) {
        return getSmallBitmapOnly(filePath, 480, 800);
    }

    /**
     * 仅仅压缩图片 [需要考虑长宽和分辨率比例]
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmapOnly(String filePath, int reqWidth, int reqHeight) {
        if (null == filePath) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);// Calculate inSampleSize
        options.inJustDecodeBounds = false;// Decode bitmap with inSampleSize set
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 按指定尺寸缩放图片[直接缩放为指定宽高]
     * Tips: 此方法还未验证
     *
     * @param fromFile
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getZoomBitmap(String fromFile, int reqWidth, int reqHeight) {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 缩放图片的尺寸
        float scaleWidth = (float) reqWidth / bitmapWidth;
        float scaleHeight = (float) reqHeight / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 产生缩放后的Bitmap对象
        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
    }

    /**
     * 计算图片的缩放值 如果图片的原始高度或者宽度大与我们期望的宽度和高度，我们需要计算出缩放比例的数值。否则就不缩放。
     * heightRatio是图片原始高度与压缩后高度的倍数， widthRatio是图片原始宽度与压缩后宽度的倍数。
     * inSampleSize就是缩放值 ，取heightRatio与widthRatio中最小的值。
     * inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2(图片为原1/4)。
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转 Bitmap 并返回旋转后的Bitmap
     *
     * @param bitmap
     * @param orientationDegree
     * @return
     */
    public static Bitmap rotateBitmapOnly(Bitmap bitmap, int orientationDegree) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(orientationDegree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * Desc: 旋转
     *
     * @param bm
     * @param orientationDegree
     * @return
     * @author lijunhua
     * @see #rotateBitmapOnly(Bitmap, int)
     */
    private Bitmap rotateBitmapOnly2(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * @param pic
     * @return
     * @author ljh @desc 把string转换成bitmap
     */
    public static Bitmap stringToBitmap(String pic) {
        byte[] b = Base64.decode(pic, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return BitmapFactory.decodeStream(bais);
    }

    /**
     * 把bitmap转换成String 将图片保存到本地
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * Desc: 显示本地选择图片到 ImageView
     *
     * @param path    本地图片path
     * @param picView 显示图片的View
     */
    public static void showPic2ImageView(String path, ImageView picView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);//拿到图片尺寸
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        int newWidth = 140;
        int newHeight = oldHeight * newWidth / oldWidth;
        options.inSampleSize = oldWidth / newWidth;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.outWidth = newWidth;
        options.outHeight = newHeight;
        Bitmap bmpReal = BitmapFactory.decodeFile(path, options);
        picView.setImageBitmap(bmpReal);
    }

    /**
     * 编码存储转码后的文件
     *
     * @param fileIn
     * @param fileOut
     */
    public static String compress2JpegFileV2(String fileIn, String fileOut, int targetW, int targetH) {
        if (!needBuildSmallBitmap(fileIn, targetW, targetH)) {
            return fileIn;
        }
        saveBitmap2FilePath(getSmallBitmap$Rotate(fileIn, targetW, targetH), fileOut);
        return fileOut;
    }

    private static boolean needBuildSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        int _cache = calculateInSampleSize(options, width, height);
        return _cache != 1;
    }
}
