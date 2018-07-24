package me.richard.note.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Folder;
import com.onedrive.sdk.extensions.Item;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.activity.base.CommonActivity;
import me.richard.note.async.onedrive.ClearBackupStateTask;
import me.richard.note.databinding.ActivityDirectoryBinding;
import me.richard.note.dialog.SimpleEditDialog;
import me.richard.note.fragment.DirectoriesFragment;
import me.richard.note.manager.onedrive.OneDriveManager;
import me.richard.note.model.Directory;
import me.richard.note.util.FragmentHelper;
import me.richard.note.util.ToastUtils;
import me.richard.note.util.preferences.SyncPreferences;

public class DirectoryActivity extends CommonActivity<ActivityDirectoryBinding> implements
        DirectoriesFragment.OnFragmentInteractionListener {

    private String oldOneDriveBackupItemId;
    private String oldOneDriveFilesBackupItemId;
    private String oldOneDriveDatabaseItemId;
    private String oldOneDrivePreferencesItemId;

    private SyncPreferences syncPreferences;

    public static void startExplore(android.app.Fragment fragment, int req) {
        Intent intent = new Intent(fragment.getActivity(), DirectoryActivity.class);
        fragment.startActivityForResult(intent, req);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_directory;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        syncPreferences = SyncPreferences.getInstance();

        configToolbar();

        Directory directory = new Directory();
        directory.setId("root");
        directory.setPath("root");
        FragmentHelper.replace(this, DirectoriesFragment.newInstance(directory), R.id.fragment_container);

        getBinding().fabCreate.setColorNormal(accentColor());
        getBinding().fabCreate.setColorPressed(accentColor());
        getBinding().fabCreate.setOnClickListener(view -> createFolder());
        getBinding().fabCreate.setImageResource(R.drawable.fab_add);

        oldOneDriveBackupItemId = syncPreferences.getOneDriveLastBackupItemId();
        oldOneDriveFilesBackupItemId = syncPreferences.getOneDriveFilesBackupItemId();
        oldOneDriveDatabaseItemId = syncPreferences.getOneDriveDatabaseItemId();
        oldOneDrivePreferencesItemId = syncPreferences.getOneDrivePreferencesItemId();
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (!isDarkTheme()) toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        if (ab != null) {
            ab.setTitle(R.string.text_folder_explore);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createFolder() {
        new SimpleEditDialog("", content -> {
            if (TextUtils.isEmpty(content)) {
                ToastUtils.makeToast(R.string.title_required);
                return;
            }
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle(R.string.text_please_wait);
            pd.setCancelable(false);
            pd.show();

            final Item newItem = new Item();
            newItem.name = content;
            newItem.folder = new Folder();
            OneDriveManager.getInstance().create(getCurrentFragment().getDirectory().getId(), newItem, new ICallback<Item>() {
                @Override
                public void success(Item item) {
                    pd.dismiss();
                    getCurrentFragment().addCategory(OneDriveManager.getDirectory(item));
                }

                @Override
                public void failure(ClientException ex) {
                    pd.dismiss();
                    ToastUtils.makeToast(String.format(
                            PalmApp.getStringCompact(R.string.error_when_try_to_backup), ex.getMessage()));
                }
            });
        }).setMaxLength(100).show(getSupportFragmentManager(), "EDIT FOLDER NAME");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDirectoryClicked(Directory item) {
        FragmentHelper.replaceWithCallback(this, DirectoriesFragment.newInstance(item), R.id.fragment_container);
    }

    private DirectoriesFragment getCurrentFragment() {
        return (DirectoriesFragment) getCurrentFragment(R.id.fragment_container);
    }

    @Override
    public void onDirectoryPicked(Directory directory) {
        String newBackupDir = syncPreferences.getOneDriveBackupItemId();
        if (!TextUtils.isEmpty(oldOneDriveBackupItemId) && !oldOneDriveBackupItemId.equals(newBackupDir)) {
            new MaterialDialog.Builder(this)
                    .title(R.string.text_warning)
                    .content(R.string.synchronize_path_changed_message)
                    .positiveText(R.string.confirm)
                    .onPositive((dialog, which) -> {
                        clearLastSyncState();
                        finishWithOK();
                    })
                    .negativeText(R.string.text_undone)
                    .onNegative((dialog, which) -> {
                        syncPreferences.setOneDriveBackupItemId(oldOneDriveBackupItemId);
                        syncPreferences.setOneDriveLastBackupItemId(oldOneDriveBackupItemId);
                        syncPreferences.setOneDriveFilesBackupItemId(oldOneDriveFilesBackupItemId);
                        syncPreferences.setOneDriveDatabaseItemId(oldOneDriveDatabaseItemId);
                        syncPreferences.setOneDrivePreferencesItemId(oldOneDrivePreferencesItemId);
                        finish();
                    })
                    .dismissListener(dialogInterface -> {
                        clearLastSyncState();
                        finishWithOK();
                    })
                    .show();
        } else {
            finishWithOK();
        }
    }

    private void clearLastSyncState() {
        new ClearBackupStateTask().execute();
    }

    private void finishWithOK() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
