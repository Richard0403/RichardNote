package me.richard.note.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.annotation.Nullable;

import me.richard.note.R;


public class IntroFragment extends Fragment {

	protected View background;
	protected TextView title;
	protected ImageView image;
	protected ImageView imageSmall;
	protected TextView description;

	@Override
	public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (inflater == null) {
			throw new IllegalArgumentException("The inflater is null exception.");
		}
		View root = inflater.inflate(R.layout.fragment_intro_slide, container, false);
		background = root.findViewById(R.id.intro_background);
		title = root.findViewById(R.id.intro_title);
		image = root.findViewById(R.id.intro_image);
		imageSmall = root.findViewById(R.id.intro_image_small);
		description = root.findViewById(R.id.intro_description);
		return root;
	}
}