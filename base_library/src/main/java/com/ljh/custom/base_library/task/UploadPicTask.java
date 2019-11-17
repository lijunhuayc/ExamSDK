package com.ljh.custom.base_library.task;

import com.ljh.custom.base_library.domain.UseCase;
import com.ljh.custom.base_library.utils.FileUtils;
import com.ljh.custom.base_library.utils.Timber;

/**
 * Desc: 图片上传任务
 * Created by Junhua.Li
 * Date: 2018/04/16 19:20
 */
public class UploadPicTask extends UseCase<UploadPicTask.RequestValues, UploadPicTask.ResponseValues> {
    @Override
    protected void executeUseCase(RequestValues mRequestValues) {
        //图片上传
        Timber.d("UploadPicTask.executeUseCase: ");
        String fileUrl = mRequestValues.getFileUrl();

        final String uploadFileName = FileUtils.buildUploadFileName(fileUrl);//非"/"开头
        AliOssUploadTask task = new AliOssUploadTask();
        task.registerCallBack(new AliOssUploadTask.UploadCallBack() {
            @Override
            public void onSuccess(String result) {
                Timber.d("UploadPicTask.executeUseCase.AliOssUploadTask.onSuccess: result = %s", result);
                getUseCaseCallback().onSuccess(new ResponseValues(FileUtils.buildUploadFileUrl(uploadFileName)));
            }

            @Override
            public void onFailure(int status, String message) {
                Timber.d("UploadPicTask.executeUseCase.AliOssUploadTask.onFailure: mescccsage = %s", message);
                getUseCaseCallback().onError(message);
            }

//            @Override
//            public void onCancelled(CancelledException cex) {
//                Timber.d("UploadPicTask.executeUseCase.AliOssUploadTask.onCancelled: cex = %s", cex.getMessage());
//                getUseCaseCallback().onError(cex.getMessage());
//            }

            @Override
            public void onFinish() {
            }
        });
        task.execute(fileUrl, uploadFileName);
    }

    public static class RequestValues implements UseCase.RequestValues {
        String fileUrl;

        public RequestValues(String pFileUrl) {
            fileUrl = pFileUrl;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String pFileUrl) {
            fileUrl = pFileUrl;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        String fileUrl;

        public ResponseValues(String pFileUrl) {
            fileUrl = pFileUrl;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String pFileUrl) {
            fileUrl = pFileUrl;
        }
    }
}
