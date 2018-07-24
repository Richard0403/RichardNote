package me.richard.note.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.richard.note.R;
import me.richard.note.activity.base.CommonActivity;
import me.richard.note.databinding.ActivityUserInfoBinding;
import me.richard.note.fragment.StatisticsFragment;
import me.richard.note.fragment.TimeLineFragment;
import me.richard.note.fragment.setting.UserInfoFragment;
import me.richard.note.util.FragmentHelper;

public class UserInfoActivity extends CommonActivity<ActivityUserInfoBinding>
        implements UserInfoFragment.OnItemSelectedListener {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        configToolbar();

        configFragment();
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.user_info);
        }
        if (!isDarkTheme()) toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
    }

    private void configFragment() {
        FragmentHelper.replace(this, new UserInfoFragment(), R.id.fragment_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineSelected() {
        FragmentHelper.replaceWithCallback(this, new TimeLineFragment(), R.id.fragment_container);
    }

    @Override
    public void onChatHeaderSelected() {
        FragmentHelper.replaceWithCallback(this, new StatisticsFragment(), R.id.fragment_container);
    }
}