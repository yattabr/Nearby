package br.com.eduardo.nearby.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import br.com.eduardo.nearby.AsyncTask.FotoAsyncTask;
import br.com.eduardo.nearby.Entity.Fotos;
import br.com.eduardo.nearby.Foursquare.FoursquareSession;

/**
 * 
 * @author eduardo.jesus
 * 
 * Classe Cotroller da View main
 *
 */
public class MainController {

	private Context context;
	private Fotos fotos;

	private Bitmap image;

	public MainController(Fotos foto, Context context) {
		this.context = context;
		this.fotos = foto;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	/*
	 * Metodo usado para executar a thread que faz download da imagem do usuário
	 */
	public void downloadImagens(final String urlImg) {
		FoursquareSession session = new FoursquareSession(context);
		new FotoAsyncTask().execute(session.getPhoto(), fotos);

	}
}
