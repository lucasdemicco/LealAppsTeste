package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.lucas.lealappsteste.R;

import Controller.ConfigFirebase;

public class BemVindo extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bem_vindo)

        setButtonBackVisible(false);
        setButtonNextVisible(false);


        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.black)
                .fragment(R.layout.intros)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intros2)
                .canGoForward(false)
                .build()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void irParaAuthActivity(View view) {
       startActivity(new Intent(this, LoginActivity.class));
       finish();
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();

        }
    }

    public void deslogarUsuario(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(BemVindo.this, PrincipalActivity.class));
        finish();
    }
}
