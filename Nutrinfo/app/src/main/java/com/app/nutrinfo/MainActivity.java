package com.app.nutrinfo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //View
    private TextView nome;
    private TextView email;
    private TextView id;

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

        //View
        nome = (TextView) findViewById(R.id.nome);
        email = (TextView) findViewById(R.id.email);
        id = (TextView) findViewById(R.id.id);

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
    }
    //fim do onCreate()

    //Cria um user e instancia com os dados do usuário atual para exibir na tela
    private void setUserData(FirebaseUser user){
        nome.setText(user.getDisplayName());
        email.setText(user.getEmail());
        id.setText(user.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAListen);
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