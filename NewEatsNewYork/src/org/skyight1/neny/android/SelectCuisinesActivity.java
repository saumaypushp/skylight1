package org.skyight1.neny.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SelectCuisinesActivity extends Activity {
	public class ImageAdapter extends BaseAdapter {

		private final Context context;

		private final List<Integer> listOfActiveResourceIds;

		private List<Integer> listOfInactiveResourceIds;

		private final List<Boolean> listOfSelectedCuisines;

		public ImageAdapter(final Context aContext, final List<Integer> aListOfActiveResourceIds, final List<Boolean> aListOfSelectedCuisines,
				List<Integer> aListOfInactiveResiourceIds) {
			context = aContext;
			listOfActiveResourceIds = aListOfActiveResourceIds;
			listOfInactiveResourceIds = aListOfInactiveResiourceIds;
			listOfSelectedCuisines = aListOfSelectedCuisines;
		}

		@Override
		public int getCount() {
			return listOfActiveResourceIds.size();
		}

		@Override
		public Object getItem(int position) {
			return listOfActiveResourceIds.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);
				imageView.setAdjustViewBounds(true);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View aV) {
					final boolean newState = !listOfSelectedCuisines.get(position);
					listOfSelectedCuisines.set(position, newState);
					final Editor edit = preferences.edit();
					edit.putBoolean(String.valueOf(position), newState);
					edit.commit();
					imageView.setImageResource(listOfSelectedCuisines.get(position) ? listOfActiveResourceIds.get(position) : listOfInactiveResourceIds
							.get(position));
				}
			});
			imageView.setImageResource(listOfSelectedCuisines.get(position) ? listOfActiveResourceIds.get(position) : listOfInactiveResourceIds.get(position));
			return imageView;
		}
	}

	private List<Boolean> listOfSelectedCuisines = new ArrayList<Boolean>();

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle aSavedInstanceState) {
		super.onCreate(aSavedInstanceState);

		preferences = getSharedPreferences("cuisines", MODE_PRIVATE);

		setContentView(R.layout.cuisines_view);

		final GridView grid = (GridView) findViewById(R.id.cuisinesGrid);
		final List<Integer> cuisinesActiveImageResources =
				Arrays.asList(R.drawable.china_active, R.drawable.africa_active, R.drawable.italian_active, R.drawable.mayan_active, R.drawable.comfort_active, R.drawable.eclectic_active, R.drawable.eu_active, R.drawable.indian_active, R.drawable.middle_eastern_active, R.drawable.north_america_active, R.drawable.pacifica_active, R.drawable.vege_active

				);
		final List<Integer> cuisinesInactiveImageResources =
				Arrays.asList(R.drawable.china_inactive, R.drawable.africa_inactive, R.drawable.italian_inactive, R.drawable.mayan_inactive, R.drawable.comfort_inactive, R.drawable.eclectic_inactive, R.drawable.eu_inactive, R.drawable.indian_inactive, R.drawable.middle_eastern_inactive, R.drawable.north_america_inactive, R.drawable.pacifica_inactive, R.drawable.vege_inactive);
		for (final int dummy : cuisinesActiveImageResources) {
			listOfSelectedCuisines.add(false);
		}

		// load preferences
		listOfSelectedCuisines = new ArrayList<Boolean>();
		for (int i = 0; i < cuisinesActiveImageResources.size(); i++) {
			listOfSelectedCuisines.add(preferences.getBoolean(String.valueOf(i), true));
		}

		grid.setAdapter(new ImageAdapter(this, cuisinesActiveImageResources, listOfSelectedCuisines, cuisinesInactiveImageResources));
	}
}
