package me.richard.note.activity.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.umeng.analytics.MobclickAgent;
import me.richard.note.util.ActivityUtils;

/**
 * Created by richard on 2017/12/21.*/
@SuppressLint("Registered")
public abstract class CommonActivity<T extends ViewDataBinding> extends ThemedActivity implements
        ColorChooserDialog.ColorCallback {

    private T binding;

    protected abstract int getLayoutResId();

    protected abstract void doCreateView(Bundle savedInstanceState);

    protected void beforeSetContentView(){}

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.addActivity(this);

//        Fabric.with(this, new Crashlytics());

        if (getLayoutResId() <= 0 ) {
            throw new AssertionError("Subclass must provide a valid layout resource id");
        }

        binding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutResId(), null, false);

        beforeSetContentView();

        setContentView(binding.getRoot());

        doCreateView(savedInstanceState);
    }

    protected final T getBinding() {
        return binding;
    }

    protected <M extends Activity> void startActivity(Class<M> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    protected <M extends Activity> void startActivityForResult(Class<M> activityClass, int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }

    protected Fragment getCurrentFragment(@IdRes int resId) {
        return getSupportFragmentManager().findFragmentById(resId);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {}

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {}

    public void superOnBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
