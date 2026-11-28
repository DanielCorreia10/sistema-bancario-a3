package contabancaria;

import cliente.Cliente;
import transacoes.Transacao;

public abstract class ContaBancaria {

    private final Cliente titular;
    private final String conta;
    private final String agencia;

    private double saldo;

    protected ContaBancaria(Cliente titular, String conta, String agencia) {
        this.titular = titular;
        this.conta = conta;
        this.agencia = agencia;
    }

    public abstract String getTipoConta();

    protected void configurarConta(double primeiroDeposito) {
    }

    public double calcularRendimento() {
        return 0;
    }

    public double getChequeEspecial() {
        return 0;
    }

    public double getValorChequeEspecialUsado() {
        return 0;
    }

    public void setValorChequeEspecialUsado(double valorChequeEspecialUsado) {
        throw new UnsupportedOperationException(getTipoConta() + " não possui cheque especial");
    }

    public double getLimiteChequeEspecialDisponivel() {
        return getChequeEspecial() - getValorChequeEspecialUsado();
    }

    public void abrir(double primeiroDeposito) {
        this.saldo = primeiroDeposito;
        configurarConta(primeiroDeposito);
    }

    public double aplicarRendimento() {
        double rendimento = calcularRendimento();
        this.saldo += rendimento;
        return rendimento;
    }

    public void executarTransacao(Transacao transacao) {
        transacao.executar(this);
    }

    public Cliente getTitular() {
        return titular;
    }

    public String getNome() {
        return titular.getNome();
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
}
