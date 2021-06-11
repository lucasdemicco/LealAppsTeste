package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lucas.lealappsteste.R;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*FloatingActionButton treino = findViewById(R.id.fabTreino);
        treino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /* FloatingActionButton anotacoes = findViewById(R.id.fabAnotacoes);
        anotacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "",Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

    }


    public void adicionarTreino(View v) {
        startActivity(new Intent(this, TreinosActivity.class));
    }
}
