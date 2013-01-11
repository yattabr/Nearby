package br.com.eduardo.nearby.AsyncTask;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import br.com.eduardo.nearby.Entity.Fotos;

/**
 * 
 * @author eduardo.jesus
 * 
 * Thread que faz download da imagem do usuário
 *
 */
public class FotoAsyncTask extends AsyncTask<Object, Void, Bitmap> {

	Context context;
	Fotos fotos;

	@Override
	protected Bitmap doInBackground(Object... params) {

		String urlImg = (String) params[0];
		fotos = (Fotos) params[1];

		URL url;
		try {
			url = new URL(urlImg);
			InputStream is = url.openStream();
			final Bitmap imagem = BitmapFactory.decodeStream(is);
			return imagem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		fotos.setPhotos(result);
		fotos.notifyObservers();
	}
}
