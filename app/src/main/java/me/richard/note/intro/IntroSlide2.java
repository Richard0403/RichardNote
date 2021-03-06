package me.richard.note.intro;

import android.os.Bundle;

import me.richard.note.R;


public class IntroSlide2 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundResource(R.color.intro_color_2);
		title.setText(R.string.intro_2_title);
		image.setImageResource(R.drawable.slide1_release);
		description.setText(R.string.intro_2_description);
	}
}