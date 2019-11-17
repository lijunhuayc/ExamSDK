package com.ljh.custom.base_library.data_source.net;

import android.content.Context;
import android.widget.ImageView;

/**
 * Desc: Banner轮播图库图片加载接口
 * Created by ${junhua.li} on 2016/11/08 16:40.
 * Email: lijunhuayc@sina.com
 */
public class BannerImageLoader
//        extends ImageLoader
{
    //    @Override
    public void displayImage(Context mContext, Object path, ImageView imageView) {
        /**
         常用的图片加载库：
         Universal Image Loader：一个强大的图片加载库，包含各种各样的配置，最老牌，使用也最广泛。
         Picasso: Square出品，必属精品。和OkHttp搭配起来更配呦！
         Volley ImageLoader：Google官方出品，可惜不能加载本地图片~
         Fresco：Facebook出的，天生骄傲！不是一般的强大。
         Glide：Google推荐的图片加载库，专注于流畅的滚动。
         */
//        Glide.with(mContext)
//                .load(path)
//                .error(R.drawable.loading_img)
//                .placeholder(R.drawable.loading_img)
//                .override(ScreenUtils.dp2px(mContext, 173), ScreenUtils.getScreenWidth(mContext))
//                .crossFade()
//                .priority(Priority.HIGH)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        imageView.setImageDrawable(resource);
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        Timber.d("glide: load failed: e = %s", e.getMessage());
//                    }
//                });
    }

//    //提供createImageView 方法，如果不用可以不重写这个方法，方便fresco自定义ImageView
//    @Override
//    public ImageView createImageView(Context context) {
//        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
//        return simpleDraweeView;
//    }
}
