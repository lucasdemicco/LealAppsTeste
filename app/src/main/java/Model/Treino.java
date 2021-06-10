package Model;

public class Treino {

    private String nome;
    private String descricao;
    private int data;

    public Treino() {
    }

    public Treino(String nome, String descricao, int data) {
        this.nome = nome;
        this.descricao = descricao;
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

    public int getData(String s) {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
