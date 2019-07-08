package com.ewu.core.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ewu.core.widget.MyProgressDialog;

import java.io.File;

/**
 * Created by Chentie on 2017/6/20.
 */

public class UploadFileTask extends AsyncTask<String, Void, String> {/**
 * 可变长的输入参数，与AsyncTask.exucute()对应
 */
    //private ProgressDialog pdialog;
    private MyProgressDialog progressDialog;
    private Activity context = null;

    public UploadFileTask(Activity ctx) {
        this.context = ctx;
        progressDialog = new MyProgressDialog(context);
        //pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容
        progressDialog.dismiss();
        //pdialog.dismiss();
        if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... params) {
        File file = new File(params[0]);
        return UploadUtils.uploadFile(file);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
