package Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import Controller.Base64Custom;
import Controller.ConfigFirebase;

public class Treino {

    private String idTreino;
    private String data;
    private String nomeTreino;
    private String descricao;
    private List<String> fotos;

    public Treino() {
        DatabaseReference treinoRef = ConfigFirebase.getFirebaseDatabase()
                .child("Treinos");
        setIdTreino(treinoRef.push().getKey());
    }

    public void salvarTreino(String dataEscolhida) {
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateUtil.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference treinoRef = ConfigFirebase.getFirebaseDatabase()
                .child("Treinos");

        treinoRef.child(idUsuario)
                .child(mesAno)
                .child(getIdTreino())
                .setValue(this);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nomeTreino;
    }

    public void setNome(String nome) {
        this.nomeTreino = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdTreino() {
        return idTreino;
    }

    public String getNomeTreino() {
        return nomeTreino;
    }

    public void setNomeTreino(String nomeTreino) {
        this.nomeTreino = nomeTreino;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public void setIdTreino(String idTreino) {
        this.idTreino = idTreino;
    }
}
