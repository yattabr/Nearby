package br.com.eduardo.nearby.Foursquare;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import br.com.eduardo.nearby.Util.Global;

/**
 * Manage access token and user name. Uses shared preferences to store access
 * token and user name.
 * 
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
public class FoursquareSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	public FoursquareSession(Context context) {
		sharedPref = context.getSharedPreferences(Global.SHARED,
				Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	/**
	 * Save access token and user name
	 * 
	 * @param accessToken
	 *            Access token
	 * @param username
	 *            User name
	 */
	public void storeAccessToken(String accessToken, String username, String photo) {
		editor.putString(Global.FSQ_ACCESS_TOKEN, accessToken);
		editor.putString(Global.FSQ_USERNAME, username);
		editor.putString(Global.FSQ_PHOTO, photo);

		editor.commit();
	}

	/**
	 * Reset access token and user name
	 */
	public void resetAccessToken() {
		editor.putString(Global.FSQ_ACCESS_TOKEN, null);
		editor.putString(Global.FSQ_USERNAME, null);

		editor.commit();
	}

	/**
	 * Get user name
	 * 
	 * @return User name
	 */
	public String getUsername() {
		return sharedPref.getString(Global.FSQ_USERNAME, null);
	}
	
	public String getPhoto(){
		return sharedPref.getString(Global.FSQ_PHOTO, null);
	}

	/**
	 * Get access token
	 * 
	 * @return Access token
	 */
	public String getAccessToken() {
		return sharedPref.getString(Global.FSQ_ACCESS_TOKEN, null);
	}
}