package br.com.eduardo.nearby.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.com.eduardo.nearby.R;
import br.com.eduardo.nearby.Activity.MapaActivity;
import br.com.eduardo.nearby.Entity.Locais;
import br.com.eduardo.nearby.Util.Global;
/**
 * 
 * @author eduardo.jesus
 *
 * Adapter usado para criar a view de locais
 */
public class LocaisAdapter extends BaseAdapter {
	private ArrayList<Locais> mVenueList;
	private LayoutInflater mInflater;
	private Context context;
	private ViewHolder holder;

	public LocaisAdapter(Context c) {
		mInflater = LayoutInflater.from(c);
		this.context = c;
	}

	public void setData(ArrayList<Locais> poolList) {
		mVenueList = poolList;
	}

	public int getCount() {
		return mVenueList.size();
	}

	public Object getItem(int position) {
		return mVenueList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final Locais venue = mVenueList.get(position);

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.nearby_list, null);

			holder = new ViewHolder();

			holder.txt_nome = (TextView) convertView
					.findViewById(R.id.txt_nome_locais);
			holder.txt_endereco = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.txt_checkin_count = (TextView) convertView
					.findViewById(R.id.txt_checkin_count);
			holder.txt_contagem = (TextView) convertView
					.findViewById(R.id.txt_contagem);
			holder.txt_locais_restantes = (TextView) convertView
					.findViewById(R.id.txt_locais_restantes);
			holder.txt_url = (TextView) convertView.findViewById(R.id.tv_site);
			holder.bt_mapa = (Button) convertView.findViewById(R.id.bt_mapa);
			holder.bt_mapa.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Intent que passa valores para a MapaActivity
					Intent it = new Intent(context, MapaActivity.class);
					it.putExtra(Global.INTENT_LATITUDE, venue.getLatitude());
					it.putExtra(Global.INTENT_LONGITUDE, venue.getLongitude());
					it.putExtra(Global.INTENT_NOME, venue.getName());
					it.putExtra(Global.INTENT_ENDERECO, venue.getAddress());
					context.startActivity(it);
				}
			});

			holder.txt_nome.setText(venue.getName());
			holder.txt_endereco.setText(venue.getAddress());
			holder.txt_checkin_count.setText(venue.getChekins_count());
			holder.txt_url.setText(venue.getUrl());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		List<String> listContagem = new ArrayList<String>();

		int i = mVenueList.size() - 1;
		while (i >= 0) {
			listContagem.add(String.valueOf(i));
			i--;
		}
		venue.setContagem(listContagem);

		holder.txt_contagem.setText(venue.getContagem().get(position));
		int count = Integer.parseInt(venue.getContagem().get(position));
		if (count == 1) {
			holder.txt_locais_restantes.setText(context.getString(R.string.locais_ultimo_local));
		}

		return convertView;
	}

	static class ViewHolder {
		TextView txt_nome;
		TextView txt_endereco;
		TextView txt_checkin_count;
		TextView txt_contagem;
		TextView txt_url;
		TextView txt_locais_restantes;
		Button bt_mapa;
	}

}