package me.richard.note.intro;

import android.os.Bundle;
import android.view.View;

import me.richard.note.R;


/**
 * Created by Wang Shouheng on 2017/12/6. */
public class IntroSlide1 extends IntroFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        background.setBackgroundResource(R.color.intro_color_1);
        title.setText(String.format(getString(R.string.intro_1_title), getString(R.string.app_name)));
        image.setVisibility(View.GONE);
        imageSmall.setImageResource(R.drawable.mark_note);
        imageSmall.setVisibility(View.VISIBLE);
        description.setText(String.format(getString(R.string.intro_1_description), getString(R.string.app_name)));
    }
}
