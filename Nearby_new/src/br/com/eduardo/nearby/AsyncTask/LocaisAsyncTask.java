package br.com.eduardo.nearby.AsyncTask;

import java.util.ArrayList;

import android.os.AsyncTask;
import br.com.eduardo.nearby.Entity.Locais;
import br.com.eduardo.nearby.Foursquare.FoursquareApp;
import br.com.eduardo.nearby.Util.FlagsObservers;
/**
 * 
 * @author eduardo.jesus
 * 
 * Thread usada para fazer a busca de locais proximos
 *
 */
public class LocaisAsyncTask extends AsyncTask<Object, Void, ArrayList<Locais>> {

	private FoursquareApp mFsqApp;
	private double latitude;
	private double longitude;
	private ArrayList<Locais> mNearbyList;
	private Locais locais;

	@Override
	protected ArrayList<Locais> doInBackground(Object... params) {
		mFsqApp = (FoursquareApp) params[0];
		latitude = (Double) params[1];
		longitude = (Double) params[2];
		locais = (Locais) params[3];

		mNearbyList = new ArrayList<Locais>();

		try {
			mNearbyList = mFsqApp.getNearby(latitude, longitude);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mNearbyList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Locais> result) {
		locais.setListLocais(result);
		locais.setFlag(FlagsObservers.LIST_LOCAIS);
		locais.notifyObservers();
		super.onPostExecute(result);
	}

}
