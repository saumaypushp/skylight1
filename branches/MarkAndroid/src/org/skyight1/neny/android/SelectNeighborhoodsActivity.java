package org.skyight1.neny.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SelectNeighborhoodsActivity extends Activity {
	public class ImageAdapter extends BaseAdapter {

		private final Context context;

		private final List<Integer> listOfActiveResourceIds;

		private List<Integer> listOfInactiveResourceIds;

		private final List<Boolean> listOfSelectedNeighborhoods;

		public ImageAdapter(final Context aContext, final List<Integer> aListOfActiveResourceIds, final List<Boolean> aListOfSelectedNeighborhoods,
				List<Integer> aListOfInactiveResiourceIds) {
			context = aContext;
			listOfActiveResourceIds = aListOfActiveResourceIds;
			listOfInactiveResourceIds = aListOfInactiveResiourceIds;
			listOfSelectedNeighborhoods = aListOfSelectedNeighborhoods;
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
					final boolean newState = !listOfSelectedNeighborhoods.get(position);
					listOfSelectedNeighborhoods.set(position, newState);
					final Editor edit = preferences.edit();
					edit.putBoolean(String.valueOf(position), newState);
					edit.commit();
					imageView.setImageResource(listOfSelectedNeighborhoods.get(position) ? listOfActiveResourceIds.get(position) : listOfInactiveResourceIds
							.get(position));
				}
			});
			imageView.setImageResource(listOfSelectedNeighborhoods.get(position) ? listOfActiveResourceIds.get(position) : listOfInactiveResourceIds
					.get(position));
			return imageView;
		}
	}

	private List<Boolean> listOfSelectedNeighborhoods = new ArrayList<Boolean>();

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle aSavedInstanceState) {
		super.onCreate(aSavedInstanceState);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.neighborhoods_view);

		final GridView grid = (GridView) findViewById(R.id.neighbourhoodGrid);
		final List<Integer> neighborhoodActiveImageResources =
				Arrays.asList(R.drawable.n_inwood_active, R.drawable.n_harlem_active, R.drawable.n_east_harlem_active, R.drawable.n_uws_active, R.drawable.n_ues_active, R.drawable.n_chelsea_active, R.drawable.n_gramercy_active, R.drawable.n_greenwich_soho_active, R.drawable.n_les_active, R.drawable.n_east_village_active, R.drawable.n_wall_st_active);
		final List<Integer> neighborhoodInactiveImageResources =
				Arrays.asList(R.drawable.n_inwood_inactive, R.drawable.n_harlem_inactive, R.drawable.n_east_harlem_inactive, R.drawable.n_uws_inactive, R.drawable.n_ues_inactive, R.drawable.n_chelsea_inactive, R.drawable.n_gramercy_inactive, R.drawable.n_greenwich_soho_inactive, R.drawable.n_les_inactive, R.drawable.n_east_village_inactive, R.drawable.n_wall_st_inactive);
		for (final int dummy : neighborhoodActiveImageResources) {
			listOfSelectedNeighborhoods.add(false);
		}

		// load preferences
		listOfSelectedNeighborhoods = new ArrayList<Boolean>();
		for (int i = 0; i < neighborhoodActiveImageResources.size(); i++) {
			listOfSelectedNeighborhoods.add(preferences.getBoolean(String.valueOf(i), true));
		}

		grid.setAdapter(new ImageAdapter(this, neighborhoodActiveImageResources, listOfSelectedNeighborhoods, neighborhoodInactiveImageResources));
	}
}