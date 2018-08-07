package me.richard.note.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import me.richard.note.R;
import me.richard.note.activity.base.CommonActivity;
import me.richard.note.config.Constants;
import me.richard.note.databinding.ActivityLoginBinding;
import me.richard.note.dialog.RegisterDialog;
import me.richard.note.intro.IntroActivity;
import me.richard.note.net.HttpRequest;
import me.richard.note.net.api.HomeService;
import me.richard.note.net.entity.BaseEntity;
import me.richard.note.net.entity.SignInEntity;
import me.richard.note.util.ToastUtils;
import me.richard.note.util.preferences.UserPreferences;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends CommonActivity<ActivityLoginBinding> {

    private final static int RC_SIGN_IN = 0x01;


    public static void startForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), LoginActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_CODE, requestCode);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        getBinding().ivQq.setOnClickListener(view -> umengAuth());
    }

    private void umengAuth(){
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String uid = data.get("uid");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            int sex = "男".equals(gender)? 1:0;

            signIn(name, uid, "", sex, iconurl);
        }
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void signIn(String name, String qqOpenId, String wxUnionId, int sex, String header){
        HttpRequest httpRequest = new HttpRequest<SignInEntity>() {
            @Override
            public String createJson() {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("qqOpenId", qqOpenId);
                map.put("wxUnionId",wxUnionId);
                map.put("sex",sex);
                map.put("header", header);
                return new Gson().toJson(map);
            }

            @Override
            protected void onSuccess(SignInEntity result) {
                super.onSuccess(result);
                UserPreferences.getInstance().setUserEntity(result.getData());
                ToastUtils.makeToast(result.getMsg());
                startActivity(MainActivity.class);
            }
        };
        httpRequest.start(HomeService.class, "signIn");
    }
}
