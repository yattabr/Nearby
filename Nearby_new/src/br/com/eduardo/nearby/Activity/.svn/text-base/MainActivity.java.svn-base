package br.com.eduardo.nearby.Activity;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.eduardo.nearby.R;
import br.com.eduardo.nearby.Controller.MainController;
import br.com.eduardo.nearby.Entity.Fotos;
import br.com.eduardo.nearby.Foursquare.FoursquareApp;
import br.com.eduardo.nearby.Foursquare.FoursquareApp.FsqAuthListener;
import br.com.eduardo.nearby.Foursquare.FoursquareSession;
import br.com.eduardo.nearby.Util.Global;

public class MainActivity extends Activity implements Observer, OnClickListener {

	private Button bt_buscar;
	private Button bt_conectar;
	private ImageButton bt_logout;
	private TextView txt_nome;
	private ImageView photo;
	private ProgressDialog dialog;

	private SharedPreferences sharedPref;
	private FoursquareSession session;

	private MainController controller;

	private FoursquareApp mFsqApp;
	private Fotos fotos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.main_carregando_imagens));
		dialog.setCancelable(false);

		fotos = new Fotos();
		fotos.addObserver(this);
		controller = new MainController(fotos, this);

		txt_nome = (TextView) findViewById(R.id.tv_title);
		bt_conectar = (Button) findViewById(R.id.bt_conectar);
		bt_conectar.setOnClickListener(this);
		bt_buscar = (Button) findViewById(R.id.bt_buscar);
		bt_buscar.setOnClickListener(this);
		bt_logout = (ImageButton) findViewById(R.id.bt_logout);
		bt_logout.setOnClickListener(this);
		photo = (ImageView) findViewById(R.id.img_photo_user);

		mFsqApp = new FoursquareApp(this, Global.CLIENT_ID,
				Global.CLIENT_SECRET);

		resultadoConexao();

		sharedPref = this.getSharedPreferences(Global.SHARED,
				Context.MODE_PRIVATE);
		if (sharedPref.getString(Global.FSQ_ACCESS_TOKEN, null) != null) {
			bt_conectar.setVisibility(View.GONE);
		}

		if (mFsqApp.hasAccessToken()) {
			dialog.show();
			txt_nome.setText(mFsqApp.getUserName());
			controller.downloadImagens(mFsqApp.getPhoto());
		}

	}

	/*
	 * Metodo que imprime o resultado da conexão com o servidor do Foursquare
	 */
	public void resultadoConexao() {

		FsqAuthListener listener = new FsqAuthListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(
						MainActivity.this,
						getString(R.string.main_conectado_como)
								+ mFsqApp.getUserName(), Toast.LENGTH_SHORT)
						.show();
				txt_nome.setText(mFsqApp.getUserName());
				controller.downloadImagens(mFsqApp.getPhoto());
				photo.setImageBitmap(fotos.getPhotos());
				bt_conectar.setVisibility(View.GONE);
			}

			@Override
			public void onFail(String error) {
				error = getString(R.string.main_nao_conectado);
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT)
						.show();
			}
		};
		mFsqApp.setListener(listener);
	}

	@Override
	public void onClick(View v) {
		if (v == bt_conectar) {
			mFsqApp.authorize();
		} else if (v == bt_buscar) {
			Intent it = new Intent(this, LocaisActivity.class);
			startActivity(it);
		} else if (v == bt_logout) {
			mFsqApp.logout();

			final AlertDialog alertDialog = new AlertDialog.Builder(
					MainActivity.this).create();
			alertDialog.setMessage(getString(R.string.main_deseja_logout));
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
					getString(R.string.main_sim),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent it = new Intent(MainActivity.this,
									LogoutActivity.class);
							startActivity(it);
							finish();
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
					getString(R.string.main_nao),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.dismiss();
						}
					});
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * @Override da interface import java.util.Observer;
	 * 
	 * Metodo update chamado para atualizar a view no momento em que a Entity
	 * recebe algum valor.
	 */
	@Override
	public void update(Observable observable, Object data) {
		photo.setImageBitmap(fotos.getPhotos());
		dialog.dismiss();
	}
}