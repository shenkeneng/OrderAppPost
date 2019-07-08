package com.frxs.order;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.model.SaleBackCart;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.InactivityTimer;
import zxing.view.ViewfinderView;


/**
 * 扫一扫 By Tiepier
 */
public class CaptureActivity extends FrxsActivity implements Callback {

    private CaptureActivityHandler handler;

    private ViewfinderView viewfinderView;

    private boolean hasSurface;

    private String strSearch;// 搜索商品编号

    private TextView tvResult;

    private Vector<BarcodeFormat> decodeFormats;

    private String characterSet;

    private InactivityTimer inactivityTimer;

    private MediaPlayer mediaPlayer;

    private boolean playBeep;

    private static final float BEEP_VOLUME = 0.10f;

    private boolean vibrate;

    private TextView tvTitle;

    private Button btnConfirm;

    private String from;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initViews() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvTitle = (TextView) findViewById(R.id.title_tv);
        btnConfirm = (Button) findViewById(R.id.capture_restart_scan);
        tvTitle.setText("扫一扫");
        from = getIntent().getStringExtra("FROM");
        backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
    }

    @Override
    protected void initEvent() {
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.capture_restart_scan:
//                if (!TextUtils.isEmpty(strSearch)) {
//                    CameraManager.get().startPreview();
//                    Message msg = handler.obtainMessage();
//                    msg.what = R.id.restart_preview;
//                    handler.sendMessage(msg);
//                    tvResult.setText("");
//                    Intent intent = new Intent(CaptureActivity.this, ProductListActivity.class);
//                    intent.putExtra("SEARCH", strSearch);
//                    startActivity(intent);
//                    strSearch = "";
//                    finish();
//                } else {
//                    ToastUtils.show(CaptureActivity.this, "请扫描商品");
//                }
//                break;
            case R.id.tv_title_left:
                finish();
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        tvResult.setText("扫描结果：" + result.getText());
        strSearch = result.getText();
        if (strSearch.equals("")) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            CameraManager.get().stopPreview();
        }
        //有扫描结果直接进入商品列表搜索
        if (!TextUtils.isEmpty(strSearch)) {
            CameraManager.get().startPreview();
            Message msg = handler.obtainMessage();
            msg.what = R.id.restart_preview;
            handler.sendMessage(msg);
            tvResult.setText("");
            Intent intent = new Intent();
            if (TextUtils.isEmpty(from)) {
                intent.setClass(CaptureActivity.this, ProductListActivity.class);
            } else {
                if (from.equals("sales_return")){
                    intent.setClass(CaptureActivity.this, ProductListActivity.class);
                    intent.putExtra("FROM", "sales_return");
                } else {
                    intent.setClass(CaptureActivity.this, BackScanListActivity.class);
                    intent.putExtra("FROM", "BACK");
                    intent.putExtra("GOODS", backMap);
                }
            }
            intent.putExtra("SEARCH", strSearch);
            startActivity(intent);
            strSearch = "";
            finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.init(this.getApplicationContext());
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            final MaterialDialog materialDialog = new MaterialDialog(this);
            materialDialog.setMessage("无法获取摄像头权限，请检查是否已经打开摄像头权限。");
            materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();

            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {

        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CaptureActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
