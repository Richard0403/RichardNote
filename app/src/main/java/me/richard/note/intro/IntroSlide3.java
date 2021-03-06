package me.richard.note.intro;

import android.os.Bundle;

import me.richard.note.R;


public class IntroSlide3 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundResource(R.color.intro_color_3);
		title.setText(R.string.intro_3_title);
		image.setImageResource(R.drawable.slide2_release);
		description.setText(R.string.intro_3_description);
	}
}