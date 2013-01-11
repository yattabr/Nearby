package br.com.eduardo.nearby.Controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import br.com.eduardo.nearby.AsyncTask.LocaisAsyncTask;
import br.com.eduardo.nearby.Entity.Locais;
import br.com.eduardo.nearby.Foursquare.FoursquareApp;
import br.com.eduardo.nearby.Util.FlagsObservers;

public class LocaisController {

	private Context context;
	private MeuLocalListener local;
	private LocationManager manager;

	private Locais locais;

	private int lock = 0;

	public LocaisController(Locais locais, Context context) {
		this.context = context;
		this.locais = locais;
	}

	
	/*
	 * Metodo que busca minha localização
	 */
	public void pegarLocalizacao() {
		local = new MeuLocalListener();
		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0.0f, local);
	}

	/*
	 * Metodo que executa a thread locaisAsyncTask 
	 * para buscar locais proximos de mim
	 */
	public void loadNearbyPlaces(final double latitude, final double longitude,
			FoursquareApp mFsqApp) {

		try {
			LocaisAsyncTask at = new LocaisAsyncTask();
			at.execute(mFsqApp, latitude, longitude, locais);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * LocationListener que buscar minha localização
	 */
	public class MeuLocalListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (lock == 0) {
				locais.setLatitude(String.valueOf(location.getLatitude()));
				locais.setLongitude(String.valueOf(location.getLongitude()));
				locais.setFlag(FlagsObservers.LOCALIZACAO);
				locais.notifyObservers();
				lock++;
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}