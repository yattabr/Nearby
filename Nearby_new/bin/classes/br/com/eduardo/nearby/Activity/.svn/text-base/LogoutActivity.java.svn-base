package br.com.eduardo.nearby.Activity;

import br.com.eduardo.nearby.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LogoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ProgressDialog dialog = new ProgressDialog(this);

		dialog.setMessage(getString(R.string.logout));
		dialog.setCancelable(false);
		dialog.show();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent it = new Intent(LogoutActivity.this, MainActivity.class);
				startActivity(it);
				finish();
			}
		}, 2500);
	}

}
