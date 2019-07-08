package com.frxs.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ewu.core.utils.BitmapUtil;
import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.takephoto.app.TakePhoto;
import com.ewu.core.widget.takephoto.app.TakePhotoImpl;
import com.ewu.core.widget.takephoto.compress.CompressConfig;
import com.ewu.core.widget.takephoto.model.InvokeParam;
import com.ewu.core.widget.takephoto.model.TContextWrap;
import com.ewu.core.widget.takephoto.model.TImage;
import com.ewu.core.widget.takephoto.model.TResult;
import com.ewu.core.widget.takephoto.model.TakePhotoOptions;
import com.ewu.core.widget.takephoto.permission.InvokeListener;
import com.ewu.core.widget.takephoto.permission.PermissionManager;
import com.ewu.core.widget.takephoto.permission.TakePhotoInvocationHandler;
import com.frxs.order.comms.Config;
import com.frxs.order.model.ShopImgPath;
import com.frxs.order.rest.RestClient;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.rest.service.SimpleCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * Created by ewu on 2016/3/24.
 */
public abstract class UploadPictureActivity extends FrxsActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private TakePhoto takePhoto;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private InvokeParam invokeParam;
    private int maxPictureNum = 1;  //可选择的照片张数
    protected int imgType = 0;

    private View parentView;
    protected List<String> imgPath = new ArrayList<String>(); // 上传图片后服务器返回的图片url

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        /**
         * 创建popwindow
         */
        parentView = getLayoutInflater().inflate(getLayoutId(), null);
        pop = new PopupWindow(UploadPictureActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        initPopWindow(view);
    }

    public int getMaxPictureNum() {
        return maxPictureNum;
    }

    public void setMaxPictureNum(int maxPictureNum) {
        this.maxPictureNum = maxPictureNum;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void takeSuccess(TResult result) {
        LogUtils.i("takeSuccess：" + result.getImage().getCompressPath());
//        imgPath.clear();
    }



    @Override
    public void takeFail(TResult result, String msg) {
        LogUtils.i("takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        LogUtils.i(getResources().getString(com.ewu.core.R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 102400;
        int width = 800;
        int height = 800;
        boolean showProgressBar = false;
        boolean enableRawFile = true;

        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 初始化选择相机或相册的popWindow
     */
    private void initPopWindow(View view) {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(UploadPictureActivity.this, R.anim.activity_translate_in));
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        TextView cancelTv = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        String[] backReason = getResources().getStringArray(R.array.selector_pick_type);
        ListView selectorLv = (ListView) view.findViewById(R.id.lv_selector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_selector_reason, R.id.item_reason, backReason);
        selectorLv.setAdapter(adapter);
        selectorLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                Uri imageUri = Uri.fromFile(file);
                configCompress(takePhoto);
                configTakePhotoOption(takePhoto);
                switch (position) {
                    case 0://相机
                        takePhoto.onPickFromCapture(imageUri);
                        break;

                    case 1:// 相册
                        takePhoto.onPickMultiple(maxPictureNum);
                        break;

                    default:
                        break;
                }
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    public void showPopWindow(int imgType) {
        this.imgType = imgType;
        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 提交退货图片
     * @param image
     */
    public void subBackImg(TImage image, final RequestListener requestListener) {
        Bitmap bitmap = BitmapUtil.getSmallBitmap(image.getCompressPath());// 压缩图片
        String imgBase64 = BitmapUtil.imgToBase64(image.getCompressPath(), bitmap);// 将图片转为base64

        AjaxParams params = new AjaxParams();
        params.put("base64Str", imgBase64);
        new RestClient(Config.getSubimgUrl()).getApiService().SubmitShopImage(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopImgPath>>() {
            @Override
            public void onResponse(ApiResponse<ShopImgPath> result, int code, String msg) {
                if (null != requestListener) {
                    requestListener.handleRequestResponse(result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopImgPath>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(UploadPictureActivity.this, t.getMessage());
            }
        });
    }
}
