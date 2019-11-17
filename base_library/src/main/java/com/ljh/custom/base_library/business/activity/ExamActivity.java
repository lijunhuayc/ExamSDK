package com.ljh.custom.base_library.business.activity;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.base.activity.BaseActivity;
import com.ljh.custom.base_library.utils.ScreenUtils;
import com.ljh.custom.base_library.utils.Timber;

import java.io.IOException;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 13:46
 */
public class ExamActivity extends BaseActivity {
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    MediaRecorder mMediaRecorder;
    String fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath();
    boolean isFull = false;
    int downX = 0;
    int downY = 0;
    int smallLeft = 0;
    int smallTop = 0;
    int lastX = 0;
    int lastY = 0;
    boolean isRecording = false;
    ViewGroup rootLayout;
    SurfaceView mSurfaceView;
    int surfaceViewWidth;
    int surfaceViewHeight;
    boolean surfaceIsCreated = false;
    //

    @Override
    protected int initLayout() {
        return R.layout.library_activity_exam_layout;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUI() {
        rootLayout = findViewById(R.id.rootView);
        mSurfaceView = findViewById(R.id.surface_view);
        surfaceViewWidth = ScreenUtils.dp2px(mContext, 320);
        surfaceViewWidth = ScreenUtils.dp2px(mContext, 480);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreated() {
        findViewById(R.id.startRecord).setOnClickListener(v -> {
            if (!isRecording) {
                startRecord();
            }
        });
        findViewById(R.id.stopRecord).setOnClickListener(v -> {
            if (isRecording) {
                stopRecord();
            }
        });
        mSurfaceView.setOnClickListener(pView -> {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            if (isFull) {
                resetSurfaceViewSize(true);
                lp.width = surfaceViewWidth;
                lp.height = surfaceViewHeight;
                lp.leftMargin = smallLeft;
                lp.topMargin = smallTop;
                isFull = false;
            } else {
                resetSurfaceViewSize(false);
                lp.width = surfaceViewWidth;
                lp.height = surfaceViewHeight;
                lp.leftMargin = 0;
                lp.topMargin = 0;
                isFull = true;
            }
            mSurfaceView.setLayoutParams(lp);
        });
        mSurfaceView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getRawX();
                    downY = (int) event.getRawY();
                    lastX = downX;
                    lastY = downY;
                    break;
                case MotionEvent.ACTION_UP:
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    if (x == downX && y == downY) {
                        mSurfaceView.performClick();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isFull) {
                        //计算出需要移动的距离
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);
                        //将移动距离加上，现在本身距离边框的位置
                        int left = view.getLeft() + dx;
                        int top = view.getTop() + dy;
                        //获取到layoutParams然后改变属性，在设置回去
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    layoutParams.height = IMAGE_SIZE
//                    layoutParams.width = IMAGE_SIZE
                        smallLeft = left;
                        smallTop = top;
                        layoutParams.leftMargin = left;
                        layoutParams.topMargin = top;
                        view.setLayoutParams(layoutParams);
                        //记录最后一次移动的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                    }
                    break;
            }
            if (!isFull) {
                rootLayout.invalidate();
            }
            return true;
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Timber.d("surfaceCreated: ");
                surfaceIsCreated = true;
                try {
                    mCamera.setDisplayOrientation(getDisplayOrientation());
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    mCamera.startPreview();
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Timber.d("surfaceChanged: format = %s, width = %s, height = %s", format, width, height);
                try {
                    mCamera.stopPreview();
//                    setCameraParams(height);
                    mCamera.setDisplayOrientation(getDisplayOrientation());
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    mCamera.startPreview();
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Timber.d("surfaceDestroyed: ");
                mCamera.release();
            }
        });
    }

    private void resetSurfaceViewSize(boolean isSmallWindow) {
        if (isSmallWindow) {
            surfaceViewWidth = ScreenUtils.dp2px(mContext, 320);
            surfaceViewHeight = ScreenUtils.dp2px(mContext, 480);
        } else {
            surfaceViewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            surfaceViewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

//    private void setCameraParams(int height) {
//        Camera.Parameters params = mCamera.getParameters();
//        Camera.Size optimalSize = getOptimalPreviewSize(height);
//        params.setPreviewSize(optimalSize.width, optimalSize.height);
//        mCamera.setParameters(params);
//    }
//
//    /**
//     * 获取当前环境下最佳的camera size
//     *
//     * @param target 目标长宽
//     * @return Camera.Size 当前摄像头支持最佳大小
//     */
//    public Camera.Size getOptimalPreviewSize(int target) {
//        if (mCamera == null)
//            return null;
//
//        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        for (Camera.Size size : sizes) {
//            if (Math.abs(size.height - target) + Math.abs(size.width - target) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - target) + Math.abs(size.width - target);
//            }
//        }
//        return optimalSize;
//    }

    protected void startRecord() {
        mCamera.unlock();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setOnInfoListener((pmRecorder, what, extra) -> {
            switch (what) {
                case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                    // TODO: 2019/11/17  录制最大时长
                    break;
                case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                    // TODO: 2019/11/17  录制最大文件
                    break;
                case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                    // TODO: 2019/11/17  未知错误
                    break;
            }
        });
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        String filePath = fileDir + "/" + System.currentTimeMillis() + ".mp4";
        mMediaRecorder.setOutputFile(filePath);
        mMediaRecorder.setMaxDuration(30 * 1000);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
        } catch (IOException pE) {
            pE.printStackTrace();
            stopRecord();
        }
    }

    protected void stopRecord() {
        if (null != mMediaRecorder) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        mCamera.lock();
        isRecording = false;
    }

    private void stopCameraPreview() {
        mCamera.stopPreview();
        mCamera.release();
    }

    /**
     * 获取当前适合的渲染方向
     *
     * @return degree角度 0 - 360
     */
    public int getDisplayOrientation() {
        Display display = getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int result;
        result = (info.orientation - degrees + 180) % 360;
        Timber.d("getDisplayOrientation: info.orientation:" + info.orientation + " degrees:" + degrees + " result:" + result);
        return result;
    }

    /**
     * 设定摄像头方向
     */
    public void setCameraOrientation() {
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Tried to stop a non-existent preview, so ignore.
        }
        try {
//            mSurfaceView.setFocusable(true);
//            mSurfaceView.setFocusableInTouchMode(true);
            //设置
            mCamera.setDisplayOrientation(getDisplayOrientation());
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Timber.d("Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        try {
            mCamera.setDisplayOrientation(getDisplayOrientation());
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception pE) {
            pE.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecord();
        stopCameraPreview();
    }
}
