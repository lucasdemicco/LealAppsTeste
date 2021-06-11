package View;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lucas.lealappsteste.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Controller.Permissoes;

public class TreinosActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgTreino;

    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);

        imgTreino = findViewById(R.id.imgTreino);
        imgTreino.setOnClickListener(this);

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgTreino:
                escolherImagem(1);
                break;
        }
    }

    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Recuperar Imagem
        if(resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configurar imagem no IMageView
            if(requestCode == 1){
                imgTreino.setImageURI(imagemSelecionada);
                listaFotosRecuperadas.add(caminhoImagem);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == getPackageManager().PERMISSION_DENIED){
                alertaPermissao();
            }
        }
    }

    public void alertaPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void salvarTreino(View view) {
    }
}