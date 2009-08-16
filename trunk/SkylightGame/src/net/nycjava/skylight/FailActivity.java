package net.nycjava.skylight;

import net.nycjava.skylight.dependencyinjection.Dependency;
import net.nycjava.skylight.dependencyinjection.DependencyInjectingObjectFactory;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * reporting unsteady hand; report acknowledged; reporting slow hand; report acknowledged; go to welcome
 */
public class FailActivity extends SkylightActivity {

	@Dependency
	private LinearLayout view;

	@Override
	protected void addDependencies(DependencyInjectingObjectFactory dependencyInjectingObjectFactory) {

		dependencyInjectingObjectFactory.registerImplementationObject(LinearLayout.class,
				(LinearLayout) getLayoutInflater().inflate(R.layout.failmsg, null));
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);

		MediaPlayer.create(getBaseContext(), R.raw.glassbreaking).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Intent intent = new Intent();
			intent.setClass(FailActivity.this, WelcomeActivity.class);
			finish();
			startActivity(intent);
		}
		return true;
	}

}
