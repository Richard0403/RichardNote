package me.richard.note.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import me.richard.note.BuildConfig;
import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.net.entity.VersionEntity;
import me.richard.note.util.DownloadUtil;
import me.richard.note.util.ScreenUtils;
import me.richard.note.util.StringUtils;
import me.richard.note.util.ToastUtils;
import org.polaric.colorful.BaseActivity;
import org.polaric.colorful.PermissionUtils;

import java.io.File;

/**
 * 更新提示
 */
public class UpdateTipDialog extends Dialog implements View.OnClickListener{
    private static final int DOWNLOAD_SUCCESS = 0x01;
    private static final int DOWNLOAD_GOING = 0x02;
    private static final String DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS";
    public static final int INSTALL_PACKAGES_REQUESTCODE = 0x01;
    private VersionEntity.DataBean version;

    private TextView tv_confirm;
    private String mFileUrl;
    private ProgressBar pb_download;
    private BaseActivity mActivity;
    private TextView tv_version_name;
    private TextView tv_version_size;
    private TextView tv_version_content;
    private TextView tv_percent;
    public static String savePath = PalmApp.getContext().getFilesDir().getAbsolutePath() +"/update.apk";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_SUCCESS:
                    installApk();
                    dismiss();
                    break;
                case DOWNLOAD_GOING:
                    int progress = msg.arg1;
                    pb_download.setIndeterminate(false);
                    pb_download.setMax(100);
                    pb_download.setProgress(progress);
                    tv_percent.setText("已下载 "+progress+"%");
                    break;
                default:
                    break;
            }
        }
    };


    private void checkIsAndroid() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = mActivity.getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();//安装应用的逻辑(写自己的就可以)
            } else {
                //请求安装未知应用来源的权限
                PermissionUtils.checkIntallPermission(mActivity, new PermissionUtils.OnGetPermissionCallback() {
                    @Override
                    public void onGetPermission() {
                        installApk();
                    }
                });
//                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk();
        }
    }


    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(savePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            PalmApp.getContext().startActivity(intent);
        }else {
            intent.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_tip);
        initView();
        setSize();
    }
    public UpdateTipDialog(BaseActivity activity, VersionEntity.DataBean version) {
        super(activity, R.style.CommentDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.version = version;
        this.mActivity = activity;
        this.mFileUrl = version.getUrl();
    }

    private void initView(){
        tv_confirm=(TextView) findViewById(R.id.tv_confirm);
        pb_download=(ProgressBar) findViewById(R.id.pb_download);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_version_size = (TextView) findViewById(R.id.tv_version_size);
        tv_version_content = (TextView) findViewById(R.id.tv_version_content);
        tv_percent = (TextView) findViewById(R.id.tv_percent);

        String content = version.getUpdateContent().replace("{n}","\n");
        tv_version_name.setText(version.getVersionName());
        tv_version_size.setText(version.getSize()+"M");
        tv_version_content.setText(content);
        tv_confirm.setOnClickListener(this);
    }

    private void setSize(){

        int winW = ScreenUtils.getScreenWidth();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = winW / 10 * 8;
//        p.height = (int) (winW / 10 * 8 * 1.8);
        getWindow().setAttributes(p);
//        getWindow().setGravity(Gravity.CENTER);
        //0不提示，1提示更新，2强制",
        setCancelable(version.isForce() != 2);
        setCanceledOnTouchOutside(version.isForce() != 2);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                //TODO
                setOtherInvisible();
                startDownload(mFileUrl);
                pb_download.setProgress(0);
                tv_percent.setText("");
                break;
            default:
                break;
        }
    }
    private void setOtherInvisible() {
        tv_confirm.setVisibility(View.INVISIBLE);
        tv_percent.setVisibility(View.VISIBLE);
        pb_download.setVisibility(View.VISIBLE);
    }


    public void startDownload(String fileUrl) {
        if(StringUtils.isEmpty(fileUrl)){
            ToastUtils.makeToast("apk url can't be null");
            return;
        }
        DownloadUtil.get().download(fileUrl, savePath, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
            }
            @Override
            public void onDownloading(int progress) {
                Message message=new Message();
                message.arg1=progress;
                message.what = DOWNLOAD_GOING;
                handler.sendMessage(message);
            }
            @Override
            public void onDownloadFailed() {
                Toast.makeText(mActivity,"Download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
