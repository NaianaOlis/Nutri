package com.app.nutrinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.app.nutrinfo.R.layout.activity_cadastro_receita;

public class CadastroReceita extends AppCompatActivity {

    String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private DatabaseReference raiz;

    AutoCompleteTextView textTitulo;
    AutoCompleteTextView textNomeIng;
    EditText textQtdIng;
    EditText textModoPrep;
    Button btnAddIngr;
    Button buttonSalvarReceita;
    ListView listViewIngrs;
    int count = 0;

    List<Ingrediente> ingredientes;

    private Spinner categ;
    private String categoria;
    private Spinner unidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_cadastro_receita);


        // [START initialize_database_ref]
        raiz = FirebaseDatabase.getInstance().getReference("receita");

        // [END initialize_database_ref]

        //selecionando os views
        textTitulo = (AutoCompleteTextView) findViewById(R.id.tituloReceita);
        textNomeIng = (AutoCompleteTextView) findViewById(R.id.nomeIngrediente);
        textQtdIng = (EditText) findViewById(R.id.qtdIngrediente);
        textModoPrep = (EditText) findViewById(R.id.modoPreparo);
        categ = (Spinner) findViewById(R.id.Categoria_receita);
        unidade = (Spinner) findViewById(R.id.unidade);

        btnAddIngr = (Button) findViewById(R.id.addIngr);



        buttonSalvarReceita = (Button) findViewById(R.id.salvar);
        //fim selecionando views

        ingredientes = new ArrayList<>();

// Cria um ArraAdapter usando um array de string e um layout padrão de spinner
        ArrayAdapter cat_adapter = ArrayAdapter.createFromResource(this, R.array.categorias_array, android.R.layout.simple_spinner_item);
        ArrayAdapter med_adapter = ArrayAdapter.createFromResource(this, R.array.medidas_array, android.R.layout.simple_spinner_item);
//alterar a fonte de dados(adapter) do Spinner
        categ.setAdapter(cat_adapter);
        unidade.setAdapter(med_adapter);


        //Método do Spinner para capturar o item selecionado
        categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                categoria = parent.getItemAtPosition(posicao).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngrediente();
            }
        });

        buttonSalvarReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReceita();
            }
        });



//AutoComplete Ingredientes
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        database.child("produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot produtoSnapshot : dataSnapshot.getChildren()){
                    String produto = produtoSnapshot.child("produto").getValue(String.class);
                    autoComplete.add(produto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV= (AutoCompleteTextView)findViewById(R.id.nomeIngrediente);
        ACTV.setAdapter(autoComplete);
//Fim auto Complete

    }

//Codigo de Barras

    public void scanBar(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(CadastroReceita.this, "Sem Leitor de Código de Barras", "Deseja fazer o download do leitor de código de barras ?", "Sim", "Não").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

//Fim Codigo de Barras

    private void addIngrediente() {
        String nomeIngr = textNomeIng.getText().toString().trim();
        String qtdIngr = textQtdIng.getText().toString().trim();
        String medIngr = unidade.getSelectedItem().toString();

        String ingrId = String.valueOf(count);

        if (!TextUtils.isEmpty(nomeIngr)) {

            Ingrediente ingrediente = new Ingrediente(ingrId, medIngr, nomeIngr, qtdIngr);

            ingredientes.add(ingrediente);

            textNomeIng.setText("");

            final IngredienteList adapter = new IngredienteList(CadastroReceita.this, ingredientes);
            listViewIngrs = (ListView)findViewById(com.app.nutrinfo.R.id.listaIngr);
            listViewIngrs.setAdapter(adapter);

            count++;

//Modificar ou deletar ingrediente
            listViewIngrs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final Ingrediente ingrediente = ingredientes.get(i);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CadastroReceita.this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.layout_edit_ingrediente, null);
                    dialogBuilder.setView(dialogView);

                    final EditText editTextNome = (EditText) dialogView.findViewById(R.id.editTextNome);
                    final EditText editTextQtd = (EditText) dialogView.findViewById(R.id.editTextQtd);
                    final Spinner spinnerUnidade = (Spinner) dialogView.findViewById(R.id.spinnerUnidade);
                    final Button btnEditar = (Button) dialogView.findViewById(R.id.btnModificar);
                    final Button btnDeletar = (Button) dialogView.findViewById(R.id.btnDelete);

                    editTextNome.setText(ingrediente.getNome());
                    editTextQtd.setText(ingrediente.getQuantidade());

                    dialogBuilder.setTitle("Editar Ingrediente");
                    final AlertDialog b = dialogBuilder.create();
                    b.show();


                    btnEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nome = editTextNome.getText().toString().trim();
                            String qtd = editTextQtd.getText().toString().trim();
                            String unidade = spinnerUnidade.getSelectedItem().toString();
                            if (!TextUtils.isEmpty(nome)) {
                                ingrediente.setNome(nome);
                                ingrediente.setQuantidade(qtd);
                                ingrediente.setMedida(unidade);

                                ingredientes.set(i, ingrediente);

                                listViewIngrs.setAdapter(adapter);

                                b.dismiss();
                            }
                        }
                    });


                    btnDeletar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ingredientes.remove(i);
                            adapter.notifyDataSetChanged();
                            listViewIngrs.setAdapter(adapter);

                            b.dismiss();
                        }
                    });
                    return true;
                }
            });

//Fim modificar ou deletar ingrediente

            Toast.makeText(this, "Ingrediente adicionado ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Favor adicionar um ingrediente", Toast.LENGTH_LONG).show();
        }
    }

    private void buscaIngrediente() {

    }


    private void addReceita() {
        String nomeRec = textTitulo.getText().toString().trim();
        String modoPrep = textModoPrep.getText().toString().trim();


        if (!TextUtils.isEmpty(nomeRec)) {

            String idReceita = raiz.push().getKey();

            Receita receita = new Receita(idReceita, nomeRec , modoPrep, categoria, ingredientes);

            raiz.child(idReceita).setValue(receita);

            textNomeIng.setText("");

            Toast.makeText(this, "Receita adicionada ", Toast.LENGTH_LONG).show();

            Intent it = new Intent(CadastroReceita.this, MainActivity.class);
            startActivity(it);
        } else {
            Toast.makeText(this, "Favor preencher todos os campos", Toast.LENGTH_LONG).show();
        }

    }


}