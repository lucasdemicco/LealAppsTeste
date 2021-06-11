package Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import Controller.Base64Custom;
import Controller.ConfigFirebase;

public class Treino {

    private String data;
    private String nome;
    private String descricao;

    public Treino() {

    }

    public void salvarTreino(String dataEscolhida){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateUtil.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference firerbase = ConfigFirebase.getFirebaseDatabase();
        firerbase.child("Treinos")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
