package com.app.nutrinfo;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //View
    private TextView nome;
    private Button btnSair;
    private Button btnAddReceita;
    ListView listViewReceitas;

    List<Receita> receitas;

    DatabaseReference databaseReceitas;

    //Google
    private GoogleApiClient googleApiClient;

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAListen;

    //Início do onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReceitas = FirebaseDatabase.getInstance().getReference("receita");

        receitas = new ArrayList<>();

        //View
        nome = (TextView) findViewById(R.id.nome);
        btnSair = (Button)findViewById(R.id.logoutBtn);
        btnAddReceita = (Button) findViewById(R.id.btnAddReceita);
        listViewReceitas = (ListView) findViewById(R.id.listViewReceita);

        //Configuração API Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Inicializa o FirebaseAuth (Obrigatório)
        fAuth = FirebaseAuth.getInstance();

        //Firebase Listen - Escuta se há usuário logado
        fAListen = new FirebaseAuth.AuthStateListener() {
            //Sempre que há uma mudança
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Cria um user Firebase e instancia com o user atual
                FirebaseUser fUser = firebaseAuth.getCurrentUser();

                //Se existe usuário logado atualmente - chama método para instanciar user e exibir dados
                if (fUser != null){
                    setUserData(fUser);
                }else {
                    //Se não existe usuário logado - volta para a tela de login
                    goLoginScreen();
                }
            }
        };

        btnAddReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CadastroReceita.class);
                startActivity(it);
            }
        });
    }
    //fim do onCreate()

    //Cria um user e instancia com os dados do usuário atual para exibir na tela
    private void setUserData(FirebaseUser user){
        nome.setText(user.getDisplayName());

    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAListen);

        databaseReceitas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                receitas.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Receita receita = postSnapshot.getValue(Receita.class);
                    //adding artist to the list
                    receitas.add(receita);
                }

                //creating adapter
                ReceitaList artistAdapter = new ReceitaList(MainActivity.this, receitas);
                //attaching adapter to the listview
                listViewReceitas.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view){
        fAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    goLoginScreen();
                }else {
                    Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Falha na conexão com a internet", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (fAListen != null){
            fAuth.removeAuthStateListener(fAListen);
        }
    }

}