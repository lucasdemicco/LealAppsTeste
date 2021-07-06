package View;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucas.lealappsteste.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Controller.ConfigFirebase;
import Controller.Permissoes;
import Model.DateUtil;
import Model.Treino;

public class TreinosActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtData, txttxtNomeTreino, txtDescricao;
    private ImageView imgTreino;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaUrlFotos = new ArrayList<>();


    private Treino treino;

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);

        iniciarComponentes();

        storage = ConfigFirebase.getFirebaseStorage();

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);
    }

    public void iniciarComponentes() {
        txtData = findViewById(R.id.txtData);
        txtData.setText(DateUtil.dataAtual());

        txtDescricao = findViewById(R.id.txtDescricao);
        txttxtNomeTreino = findViewById(R.id.txtNomeTreino);

        imgTreino = findViewById(R.id.imgTreino);
        imgTreino.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgTreino:
                escolherImagem(1);
                break;
        }
    }

    public void alertaPermissao() {
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

    public void escolherImagem(int requestCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Recuperar Imagem
        if (resultCode == Activity.RESULT_OK) {
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configurar imagem no IMageView
            if (requestCode == 1) {
                imgTreino.setImageURI(imagemSelecionada);
            }

            FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
            usuario.getCurrentUser().getEmail();
            listaFotosRecuperadas.add(caminhoImagem);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == getPackageManager().PERMISSION_DENIED) {
                alertaPermissao();
            }
        }
    }

    public void salvarTreino(View view) {

        if (validarCamposTreino()) {
            treino = new Treino();
            String data = txtData.getText().toString();
            treino.setNome(txttxtNomeTreino.getText().toString());
            treino.setDescricao(txtDescricao.getText().toString());
            treino.setData(data);
            treino.salvarTreino(data);
        }

            //Salvar imagem Storage
            for (int i = 0; i < listaFotosRecuperadas.size(); i++) {
                String urlImagem = listaFotosRecuperadas.get(i);
                int tamanhoLista = listaFotosRecuperadas.size();
                salvarFotoStorage(urlImagem, tamanhoLista, i);
            }
            Toast.makeText(this,
                    "Salvo com sucesso!",
                    Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, PrincipalActivity.class));
    }



    public void salvarFotoStorage(String urlFoto, int totalFotos, int contador) {
        //Criar nó Storage
        final StorageReference imagemAnuncio = storage.child("imagens")
                .child("treinos")
                .child(treino.getIdTreino())
                .child("Imagem" + contador);

        //Fazer upload do arquivo
        UploadTask uploadTask = imagemAnuncio.putFile(Uri.parse(urlFoto));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemAnuncio.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        Uri url = task.getResult();
                        assert url != null;
                        String urlConvertida = url.toString();

                        listaUrlFotos.add(urlConvertida);

                        if (totalFotos == listaUrlFotos.size()) {
                            treino.setFotos(listaUrlFotos);
                            treino.salvarTreino(treino.getData());
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(TreinosActivity.this,
                                "Falha ao fazer upload",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public Boolean validarCamposTreino() {
        String txtNome = txttxtNomeTreino.getText().toString();
        String data = txtData.getText().toString();
        String descricao = txtDescricao.getText().toString();

        if (listaFotosRecuperadas.size() != 0) {
            if (!txtNome.isEmpty()) {
                if (!data.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        return true;

                    } else {
                        Toast.makeText(TreinosActivity.this,
                                "Preencha uma descrição",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(TreinosActivity.this,
                            "Preencha uma data",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(TreinosActivity.this,
                        "Preencha o nome do Treino",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(TreinosActivity.this,
                    "Foto não selecionada",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}


