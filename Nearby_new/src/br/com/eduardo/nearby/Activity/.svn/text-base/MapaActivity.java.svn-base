package br.com.eduardo.nearby.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import br.com.eduardo.nearby.R;
import br.com.eduardo.nearby.Util.Global;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends FragmentActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		LatLng latlng = null;
		String nome = " ";
		String endereco = " ";

		// Intent que recebe informações para manipular o mapa
		Intent it = getIntent();
		if (it != null) {
			String lat = it.getStringExtra(Global.INTENT_LATITUDE);
			String lng = it.getStringExtra(Global.INTENT_LONGITUDE);
			nome = it.getStringExtra(Global.INTENT_NOME);
			endereco = it.getStringExtra(Global.INTENT_ENDERECO);

			latlng = new LatLng(Double.parseDouble(lat),
					Double.parseDouble(lng));

		}

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		map.setMyLocationEnabled(true);

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

		Marker meuPonto = map.addMarker(new MarkerOptions()
				.position(latlng)
				.title(nome)
				.snippet(endereco)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_nearby)));

	}
}
