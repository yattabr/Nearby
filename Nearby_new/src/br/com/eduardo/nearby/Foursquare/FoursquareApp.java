package br.com.eduardo.nearby.Foursquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import br.com.eduardo.nearby.R;
import br.com.eduardo.nearby.Entity.Locais;
import br.com.eduardo.nearby.Foursquare.FoursquareDialog.FsqDialogListener;

/**
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 */
public class FoursquareApp {
	private FoursquareSession mSession;
	private FoursquareDialog mDialog;
	private FsqAuthListener mListener;
	private ProgressDialog mProgress;
	private String mTokenUrl;
	private String mAccessToken;
	private Context context_app;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.foursquare.com/)
	 */
	public static final String CALLBACK_URL = "http://www.mobigap.com.br";
	private static final String AUTH_URL = "https://foursquare.com/oauth2/authenticate?response_type=code";
	private static final String TOKEN_URL = "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";
	private static final String API_URL = "https://api.foursquare.com/v2";

	private static final String TAG = "FoursquareApi";

	public FoursquareApp(Context context, String clientId, String clientSecret) {
		mSession = new FoursquareSession(context);
		this.context_app = context;
		mAccessToken = mSession.getAccessToken();

		mTokenUrl = TOKEN_URL + "&client_id=" + clientId + "&client_secret="
				+ clientSecret + "&redirect_uri=" + CALLBACK_URL;

		String url = AUTH_URL + "&client_id=" + clientId + "&redirect_uri="
				+ CALLBACK_URL;

		FsqDialogListener listener = new FsqDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				mListener.onFail(context_app
						.getString(R.string.fsqApp_falha_autorizacao));
			}
		};

		mDialog = new FoursquareDialog(context_app, url, listener);
		mProgress = new ProgressDialog(context_app);

		mProgress.setCancelable(false);
	}

	/*
	 * Metodo que garante acesso ao servidor do Foursquare
	 */
	private void getAccessToken(final String code) {
		mProgress.setMessage(context_app
				.getString(R.string.fsqApp_acessando_autorizacao));
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Buscando access token");

				int what = 0;

				try {
					URL url = new URL(mTokenUrl + "&code=" + code);

					Log.i(TAG, "Abrindo URL " + url.toString());

					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);

					urlConnection.connect();

					JSONObject jsonObj = (JSONObject) new JSONTokener(
							streamToString(urlConnection.getInputStream()))
							.nextValue();
					mAccessToken = jsonObj.getString("access_token");

					Log.i(TAG, "access token recebido: " + mAccessToken);
				} catch (Exception ex) {
					what = 1;

					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}

	/*
	 * Metodo que ler as informações do User logado. Informações buscadas: Nome
	 * e Foto Acesso é salvo em sessão pela classe FoursquareSession.java
	 */
	private void fetchUserName() {
		mProgress.setMessage(context_app
				.getString(R.string.fsqApp_finalizando_acesso));

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Buscadndo nome do usuario");
				int what = 0;

				try {
					String v = timeMilisToString(System.currentTimeMillis());
					URL url = new URL(API_URL + "/users/self?oauth_token="
							+ mAccessToken + "&v=" + v);

					Log.d(TAG, "Abrindo URL " + url.toString());

					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.connect();

					String response = streamToString(urlConnection
							.getInputStream());
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					JSONObject resp = (JSONObject) jsonObj.get("response");
					JSONObject user = (JSONObject) resp.get("user");

					String firstName = user.getString("firstName");
					String lastName = user.getString("lastName");

					JSONObject photo = (JSONObject) user.get("photo");
					String prefix = photo.getString("prefix");
					String sufix = photo.getString("suffix");
					String url_photo = prefix + "110x110" + sufix;

					Log.i(TAG, "Nome do usuario recebido: " + firstName + " "
							+ lastName);

					// Salvando informações de acesso, nome, sobrenome e url da
					// photo do perfil
					mSession.storeAccessToken(mAccessToken, firstName + " "
							+ lastName, url_photo);
				} catch (Exception ex) {
					what = 1;

					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				if (msg.what == 0) {
					fetchUserName();
				} else {
					mProgress.dismiss();

					mListener
							.onFail(context_app
									.getString(R.string.fsqApp_acessando_autorizacao_falha));
				}
			} else {
				mProgress.dismiss();

				mListener.onSuccess();
			}
		}
	};

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void setListener(FsqAuthListener listener) {
		mListener = listener;
	}

	public String getUserName() {
		return mSession.getUsername();
	}

	public String getPhoto() {
		return mSession.getPhoto();
	}

	public void authorize() {
		mDialog.show();
	}

	public void logout() {
		mSession.resetAccessToken();
	}

	/**
	 * 
	 * @author eduardo.jesus
	 * 
	 *         Método que ler o JSON enviado pelo Foursquare e alimenta a Entity
	 *         Locais e retorna um Arraylist<Loicais> para a view LociasActivity
	 * 
	 */
	public ArrayList<Locais> getNearby(double latitude, double longitude)
			throws Exception {
		ArrayList<Locais> venueList = new ArrayList<Locais>();

		try {
			String v = timeMilisToString(System.currentTimeMillis());
			String ll = String.valueOf(latitude) + ","
					+ String.valueOf(longitude);
			URL url = new URL(API_URL + "/venues/search?ll=" + ll
					+ "&oauth_token=" + mAccessToken + "&v=" + v);

			Log.d(TAG, "Abrindo URL " + url.toString());

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);

			urlConnection.connect();

			String response = streamToString(urlConnection.getInputStream());
			JSONObject jsonObj = (JSONObject) new JSONTokener(response)
					.nextValue();

			JSONArray groups = (JSONArray) jsonObj.getJSONObject("response")
					.getJSONArray("venues");

			int length = groups.length();
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					Locais venue = new Locais();
					JSONObject group = (JSONObject) groups.get(i);

					if (group.has("name")) {
						venue.setName(group.getString("name"));
					} else {
						venue.setName(context_app
								.getString(R.string.fsqApp_nome_nao_cadastrado));
					}

					if (group.has("url")) {
						venue.setUrl(group.getString("url"));
					} else {
						venue.setUrl(context_app
								.getString(R.string.fsqApp_url_nao_cadastrado));
					}

					JSONObject local = group.getJSONObject("location");
					if (local.has("address")) {
						venue.setAddress(local.getString("address"));
					} else {
						venue.setAddress(context_app
								.getString(R.string.fsqApp_endereco_nao_cadastrado));
					}

					if (local.has("lng")) {
						venue.setLongitude(local.getString("lng"));
					}

					if (local.has("lat")) {
						venue.setLatitude(local.getString("lat"));
					}

					JSONObject stats = group.getJSONObject("stats");
					if (stats.has("checkinsCount")) {
						venue.setChekins_count(stats.getString("checkinsCount"));
					} else {
						venue.setChekins_count(context_app
								.getString(R.string.fsqApp_checkin_nao_cadastrado));
					}

					venueList.add(venue);
				}

			}
		} catch (Exception ex) {
			throw ex;
		}

		return venueList;
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	private String timeMilisToString(long milis) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(milis);

		return sd.format(calendar.getTime());
	}

	public interface FsqAuthListener {
		public abstract void onSuccess();

		public abstract void onFail(String error);
	}
}