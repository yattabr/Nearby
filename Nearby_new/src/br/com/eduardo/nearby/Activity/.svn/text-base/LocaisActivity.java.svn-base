package br.com.eduardo.nearby.Activity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import br.com.eduardo.nearby.R;
import br.com.eduardo.nearby.Adapter.LocaisAdapter;
import br.com.eduardo.nearby.Controller.LocaisController;
import br.com.eduardo.nearby.Entity.Locais;
import br.com.eduardo.nearby.Foursquare.FoursquareApp;
import br.com.eduardo.nearby.Util.FlagsObservers;
import br.com.eduardo.nearby.Util.Global;

import com.aphidmobile.flip.FlipViewController;

public class LocaisActivity extends Activity implements Observer {

	private LocaisController controller;
	private static Locais locais;

	private FoursquareApp mFsqApp;
	private ArrayList<Locais> mNearbyList;
	private LocaisAdapter mAdapter;
	private ProgressDialog mProgress;
	private FlipViewController flipcontroller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		flipcontroller = new FlipViewController(this);

		mFsqApp = new FoursquareApp(this, Global.CLIENT_ID,
				Global.CLIENT_SECRET);
		mNearbyList = new ArrayList<Locais>();
		mAdapter = new LocaisAdapter(this);

		locais = new Locais();
		locais.addObserver(this);

		mProgress = new ProgressDialog(this);
		mProgress.setMessage(getString(R.string.locais_buscando_locais));
		mProgress.show();

		controller = new LocaisController(locais, this);
		controller.pegarLocalizacao();
	}

	protected void onResume() {
		super.onResume();
		flipcontroller.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipcontroller.onPause();
	}

	
	/*
	 * Metodo que atualiza a view todas vez que a Entity Locais recebe algum valor
	 *   
	 */
	@Override
	public void update(Observable observer, Object data) {
		switch (locais.getFlag()) {
		case FlagsObservers.LOCALIZACAO:
			double lat = Double.valueOf(locais.getLatitude());
			double lon = Double.valueOf(locais.getLongitude());
			controller.loadNearbyPlaces(lat, lon, mFsqApp);
			break;
		case FlagsObservers.LIST_LOCAIS:
			mProgress.dismiss();
			mNearbyList = locais.getListLocais();
			if (!mNearbyList.isEmpty()) {
				mAdapter.setData(mNearbyList);
				flipcontroller.setAdapter(mAdapter);
				setContentView(flipcontroller);
			}
			break;

		default:
			break;
		}

	}

}
