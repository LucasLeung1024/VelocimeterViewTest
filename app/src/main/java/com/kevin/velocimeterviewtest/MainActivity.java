package com.kevin.velocimeterviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.kevin.velocimeterviewtest.VelocimeterView.VelocimeterView;

public class MainActivity extends AppCompatActivity {

	private SeekBar			mSeek;
	private VelocimeterView	mVelocimeterView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mVelocimeterView = (VelocimeterView) findViewById(R.id.velocimeter);
		mSeek = (SeekBar) findViewById(R.id.seek);
		mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mVelocimeterView.setValue(progress, true);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}
}
