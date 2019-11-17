package com.ljh.custom.base_library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * @author shun
 * @version V1.0
 * @Title: //file realName
 * @Package com.cos.gdt.utils
 * @Description //TODO
 * @date 16/2/24 13:47
 */
public class FrescoUtils {
    /**
     * 清除所有缓存
     */
    public static void clearCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
    }

    public static void clearMemoryCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }

    /**
     * 清除一条内容
     *
     * @param uri
     */
    public static void delPipline(String uri) {
        if (uri == null || uri.length() == 0) {
            return;
        }
        Uri url = Uri.parse(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(url);
        imagePipeline.evictFromDiskCache(url);
    }

    /**
     * 设置内容
     *
     * @param draweeView
     * @param uri
     */
    public static void setDraweeViewUri(DraweeView draweeView, String uri) {
        if (uri == null || uri.length() == 0 || draweeView == null) {
            return;
        }
        //缓存一个url到view中，防止重复加载
        if (draweeView.getTag() instanceof String) {
            String tag = (String) draweeView.getTag();
            if (tag != null && tag.equals(uri))
                return;
        }

        draweeView.setTag(uri);
        draweeView.setController(buildDefaultController(draweeView, uri));
    }

    /**
     * 设置内容
     *
     * @param draweeView
     * @param uri
     */
    public static void setDraweeViewUriForTable(DraweeView draweeView, String uri, int width, int height) {
        if (uri == null || uri.length() == 0 || draweeView == null) {
            return;
        }
        //缓存一个url到view中，防止重复加载
        if (draweeView.getTag() instanceof String) {
            String tag = (String) draweeView.getTag();
            if (tag != null && tag.equals(uri))
                return;
        }

        draweeView.setTag(uri);
        draweeView.setController(showThumb(Uri.parse(uri), draweeView, width, height));
    }

    /**
     * 设置内容
     *
     * @param draweeView
     * @param res
     */
    public static void setDraweeViewRes(DraweeView draweeView, int res) {
        //缓存一个url到view中，防止重复加载
        if (draweeView.getTag() instanceof Integer) {
            Integer tag = (Integer) draweeView.getTag();
            if (tag != 0 && tag == res)
                return;
        }
        draweeView.setTag(new Integer(res));
        draweeView.setController(buildDefaultController(draweeView, res));
    }

    public static DraweeController buildDefaultController(DraweeView draweeView, String uri) {
        return buildDefaultController(buildDefaultRequest(draweeView, uri), draweeView);
    }

    public static DraweeController buildDefaultController(DraweeView draweeView, int res) {
        return buildDefaultController(buildDefaultRequest(draweeView, res), draweeView);
    }

    public static DraweeController buildDefaultController(ImageRequest request, DraweeView draweeView) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();
        return draweeController;
    }

    public static ImageRequest buildDefaultRequest(DraweeView draweeView, String uri) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//                .setResizeOptions(buildResizeOption(draweeView))
                .build();
        return imageRequest;
    }

    public static DraweeController showThumb(Uri uri, DraweeView draweeView, int width, int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        return controller;
    }

    private static ResizeOptions buildResizeOption(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width == 0 || height == 0) {
            width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(width, height);
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
        }
        Timber.d("width = " + width);
        Timber.d("height = " + height);
        return new ResizeOptions(width, height);
    }

    public static ImageRequest buildDefaultRequest(DraweeView draweeView, int res) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithResourceId(res)
//                .setResizeOptions(buildResizeOption(draweeView))
                .build();
        return imageRequest;
    }

    //加载图片
    public static void loadBitmap(Context context, String uri, OnLoadListener l) {
        if (uri == null || uri.length() == 0) {
            if (l != null)
                l.failure();
        }
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uri))
                .setProgressiveRenderingEnabled(true)
                .build();
        Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, context)
                .subscribe(new CustomBaseBitmapDataSubscriber(l), CallerThreadExecutor.getInstance());
    }

    public interface OnLoadListener {
        void success(Bitmap bmp);

        void failure();
    }

    private static class CustomBaseBitmapDataSubscriber extends BaseBitmapDataSubscriber {
        private OnLoadListener listener;

        public CustomBaseBitmapDataSubscriber(OnLoadListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
            dataSource.getFailureCause().printStackTrace();
            if (listener != null)
                listener.failure();
        }

        @Override
        protected void onNewResultImpl(Bitmap bitmap) {
            if (listener != null)
                listener.success(bitmap);
        }
    }
}
