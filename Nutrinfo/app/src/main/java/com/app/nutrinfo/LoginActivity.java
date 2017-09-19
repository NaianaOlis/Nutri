package com.app.nutrinfo;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etemail, etsenha;
    private Button bentrar;
    private TextView tvcadastro;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etemail = (EditText)findViewById(R.id.etEmail);
        etsenha = (EditText)findViewById(R.id.etsenha);
        bentrar = (Button)findViewById(R.id.bentrar);
        tvcadastro = (TextView)findViewById(R.id.tvcadastro);

        progressDialog = new ProgressDialog(this);

        bentrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                login();
            }
        });
        tvcadastro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                progressDialog.setMessage("Aguarde...");
                progressDialog.show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        String email = etemail.getText().toString().trim();
        String senha  = etsenha.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Informe um email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(senha)){
            Toast.makeText(this,"Informe sua senha",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Aguarde...");
        progressDialog.show();

        //login

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            Toast.makeText(LoginActivity.this,"Successfully registered" + user.getEmail(),Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }
                        progressDialog.dismiss();

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
        //atualiza pagina
    }
}
