package com.app.nutrinfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class IngredienteList extends ArrayAdapter<Ingrediente> {
    private Activity context;
    List<Ingrediente> ingredientes;

    public IngredienteList(Activity context, List<Ingrediente> ingredientes) {
        super(context, R.layout.layout_ingrediente_list, ingredientes);
        this.context = context;
        this.ingredientes = ingredientes;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_ingrediente_list, null, true);

        TextView textViewNome = (TextView) listViewItem.findViewById(R.id.textViewNome);
        TextView textViewQtd = (TextView) listViewItem.findViewById(R.id.textViewQtd);
        TextView textViewUnidade = (TextView) listViewItem.findViewById(R.id.textViewUnidade);;

        Ingrediente ingrediente = ingredientes.get(position);
        textViewNome.setText(ingrediente.getNome()+" ");
        textViewQtd.setText(ingrediente.getQuantidade()+" ");
        textViewUnidade.setText(ingrediente.getMedida()+" ");


        return listViewItem;
    }
}