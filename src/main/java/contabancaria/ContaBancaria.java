package contabancaria;

import transacoes.Transacao;

public abstract class ContaBancaria {

    private final String nome;
    private final String conta;
    private final String agencia;

    private double saldo;
    private double chequeEspecial;
    private double valorChequeEspecialUsado;

    protected ContaBancaria(String nome, String conta, String agencia) {
        this.nome = nome;
        this.conta = conta;
        this.agencia = agencia;
    }

    public abstract double calcularChequeEspecial(double primeiroDeposito);

    public abstract String getTipoConta();

    public void abrir(double primeiroDeposito) {
        this.saldo = primeiroDeposito;
        this.chequeEspecial = calcularChequeEspecial(primeiroDeposito);
    }

    public boolean autenticar(String nomeInformado, String contaInformada, String agenciaInformada) {
        return nomeInformado.equalsIgnoreCase(this.nome)
                && contaInformada.equals(this.conta)
                && agenciaInformada.equals(this.agencia);
    }

    public void executarTransacao(Transacao transacao) {
        transacao.executar(this);
    }

    public String getNome() {
        return nome;
    }

    public String getConta() {
        return conta;
    }

    public String getAgencia() {
        return agencia;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getChequeEspecial() {
        return chequeEspecial;
    }

    public double getValorChequeEspecialUsado() {
        return valorChequeEspecialUsado;
    }

    public void setValorChequeEspecialUsado(double valorChequeEspecialUsado) {
        this.valorChequeEspecialUsado = valorChequeEspecialUsado;
    }

    public double getLimiteChequeEspecialDisponivel() {
        return chequeEspecial - valorChequeEspecialUsado;
    }
}
