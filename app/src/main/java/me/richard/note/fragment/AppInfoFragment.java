package me.richard.note.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import de.psdev.licensesdialog.LicensesDialog;
import me.richard.note.BuildConfig;
import me.richard.note.R;
import me.richard.note.constants.AppConst;
import me.richard.note.databinding.FragmentAppInfoBinding;
import me.richard.note.fragment.base.BaseFragment;
import me.richard.note.listener.OnFragmentDestroyListener;
import me.richard.note.util.IntentUtils;

/**
 * Created by Richard on 2017/12/3.*/
public class AppInfoFragment extends BaseFragment<FragmentAppInfoBinding> {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_app_info;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        configToolbar();

        configViews();
    }

    private void configToolbar() {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.app_info);
            }
        }
    }

    private void configViews() {
        String verName =  "V" + BuildConfig.VERSION_NAME;
        getBinding().tvVersionName.setText(verName);

//        getBinding().ctvThanks.setOnCardTitleClickListener(() -> IntentUtils.openGithubProject(getActivity()));
        getBinding().ctvThanks.setSubTitle(String.format(getString(R.string.thanks_person), getString(R.string.app_name)));

        getBinding().ctvRating.setOnCardTitleClickListener(() -> IntentUtils.openInMarket(getActivity()));
        getBinding().ctvRating.setSubTitle(String.format(getString(R.string.give_good_rating_if_you_like), getString(R.string.app_name)));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof OnFragmentDestroyListener) {
            ((OnFragmentDestroyListener) getActivity()).onFragmentDestroy();
        }
    }
}
