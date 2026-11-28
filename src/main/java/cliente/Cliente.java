package cliente;

public class Cliente {

    private String nome;
    private final Cpf cpf;
    private final Senha senha;

    public Cliente(String nome, Cpf cpf, Senha senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }

    public boolean autenticar(String senhaInformada) {
        return senha.confere(senhaInformada);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cpf getCpf() {
        return cpf;
    }
}
