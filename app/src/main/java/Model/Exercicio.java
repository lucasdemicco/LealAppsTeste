package Model;

public class Exercicio {

    private String nome;
    private int imagem;
    private String observacoes;

    public Exercicio() {
    }

    public Exercicio(String nome, int imagem, String observacoes) {
        this.nome = nome;
        this.imagem = imagem;
        this.observacoes = observacoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
