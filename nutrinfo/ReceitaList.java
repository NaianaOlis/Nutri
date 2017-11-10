package com.app.nutrinfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReceitaList extends ArrayAdapter<Receita> {
    private Activity context;
    List<Receita> receitas;

    public ReceitaList(Activity context, List<Receita> receitas) {
        super(context, R.layout.layout_receita_list, receitas);
        this.context = context;
        this.receitas = receitas;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_receita_list, null, true);

        TextView textViewNome = (TextView) listViewItem.findViewById(R.id.textViewNome);

        Receita receita = receitas.get(position);
        textViewNome.setText(receita.getTitulo());

        return listViewItem;
    }
}