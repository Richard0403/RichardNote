package me.richard.note.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import me.richard.note.R;
import me.richard.note.databinding.ActivityListBaseWithDrawerBinding;
import me.richard.note.fragment.CategoriesFragment;
import me.richard.note.fragment.NotesFragment;
import me.richard.note.model.data.Status;
import me.richard.note.util.FragmentHelper;


/**
 * Created by Richard on 2017/10/10.*/
public abstract class BaseListActivity extends CommonActivity<ActivityListBaseWithDrawerBinding> implements
        NotesFragment.OnNotesInteractListener,
        CategoriesFragment.OnCategoriesInteractListener {

    private boolean isListChanged;

    protected abstract CharSequence getActionbarTitle();

    protected abstract Fragment getNotesFragment();

    protected abstract Fragment getCategoryFragment();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_list_base_with_drawer;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        configToolbar();

        configDrawer();
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (!isDarkTheme()) toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        if (actionBar != null) {
            actionBar.setTitle(getActionbarTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }
    }

    private void configDrawer() {
        getBinding().drawerLayout.openDrawer(GravityCompat.START);
        View header = getBinding().navView.inflateHeaderView(R.layout.layout_archive_header);
        Toolbar drawerToolbar = header.findViewById(R.id.toolbar);
        drawerToolbar.setTitle(R.string.items);
        drawerToolbar.setBackgroundColor(primaryColor());

        getBinding().navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_back) {
                if (isListChanged) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                }
                super.onBackPressed();
                return true;
            }
            getBinding().drawerLayout.closeDrawers();
            execute(menuItem.getItemId());
            return true;
        });
        getBinding().navView.getMenu().findItem(R.id.nav_notes).setChecked(true);

        /*
         * Don't try to delay the execution due to the runtime exception.*/
        FragmentHelper.replace(this, getNotesFragment(), R.id.fragment_container);
    }

    private void execute(@IdRes final int id) {
        new Handler().postDelayed(() -> {
            switch (id) {
                case R.id.nav_notes:
                    FragmentHelper.replace(this, getNotesFragment(), R.id.fragment_container);
                    break;
                case R.id.nav_labels:
                    FragmentHelper.replace(this, getCategoryFragment(), R.id.fragment_container);
                    break;
            }
        }, 350);
    }

    public void setDrawerLayoutLocked(boolean lock){
        getBinding().drawerLayout.setDrawerLockMode(
                lock ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    protected Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Fragment fragment = getCurrentFragment();
                if (!fragment.onOptionsItemSelected(item)) {
                    getBinding().drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getBinding().drawerLayout.isDrawerOpen(GravityCompat.START)){
            getBinding().drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isListChanged) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onNoteDataChanged() {
        isListChanged = true;
    }

    @Override
    public void onResumeToCategory() {
        setDrawerLayoutLocked(false);
    }

    @Override
    public void onActivityAttached(boolean isTopStack) {
        setDrawerLayoutLocked(!isTopStack);
    }

    @Override
    public void onCategoryLoadStateChanged(Status status) {
        onLoadStateChanged(status);
    }

    @Override
    public void onNoteLoadStateChanged(Status status) {
        onLoadStateChanged(status);
    }

    private void onLoadStateChanged(Status status) {
        switch (status) {
            case SUCCESS:
            case FAILED:
                getBinding().sl.setVisibility(View.GONE);
                break;
            case LOADING:
                getBinding().sl.setVisibility(View.VISIBLE);
                break;
        }
    }
}
