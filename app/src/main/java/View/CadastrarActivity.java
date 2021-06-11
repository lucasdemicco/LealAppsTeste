package View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lucas.lealappsteste.R;

import org.jetbrains.annotations.NotNull;

import Controller.ConfigFirebase;
import Model.Usuario;

public class CadastrarActivity extends AppCompatActivity {

    private EditText txtNome, txtEmail, txtSenha;
    private Button btnCadastro;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campoNome = txtNome.getText().toString();
                String campoEmail = txtEmail.getText().toString();
                String campoSenha = txtSenha.getText().toString();

                //Validação
                if (!campoNome.isEmpty())
                    if (!campoEmail.isEmpty())
                        if (!campoSenha.isEmpty()) {

                            usuario = new Usuario();
                            usuario.setNome(campoNome);
                            usuario.setEmail(campoEmail);
                            usuario.setSenha(campoSenha);
                            cadastrarUsuario();

                        } else Toast.makeText(CadastrarActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(CadastrarActivity.this,
                                "Preencha o email!",
                                Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(CadastrarActivity.this,
                            "Preencha o nome!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario() {
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastrarActivity.this,
                            "Cadastrado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta já foi cadastrada!";
                    } catch (Exception e) {
                        excecao = "Digite uma senha mais forte!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastrarActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
