package contabancaria;

import cliente.Cliente;
import transacoes.Transacao;

public abstract class ContaBancaria {

    private final Cliente titular;
    private String nome;
    private String celular;
    private final String conta;
    private final String agencia;

    private double saldo;

    protected ContaBancaria(Cliente titular, String conta, String agencia) {
        this.titular = titular;
        this.nome = titular.getNome();
        this.celular = titular.getCelular();
        this.conta = conta;
        this.agencia = agencia;
    }

    public abstract String getTipoConta();

    protected void configurarConta() {
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

    public double getTaxaChequeEspecialDevida() {
        return 0;
    }

    public double getLimiteChequeEspecialDisponivel() {
        return getChequeEspecial() - getValorChequeEspecialUsado();
    }

    public void abrir() {
        configurarConta();
    }

    public void depositar(double valor) {
        validarValor(valor);
        creditar(valor);
    }

    public boolean sacar(double valor) {
        validarValor(valor);
        if (valor > saldo) {
            return false;
        }
        debitar(valor);
        return true;
    }

    public boolean pagarBoleto(double valor) {
        return sacar(valor);
    }

    public double aplicarRendimento() {
        double rendimento = calcularRendimento();
        creditar(rendimento);
        return rendimento;
    }

    public String executarTransacao(Transacao transacao) {
        return transacao.executar(this);
    }

    protected void creditar(double valor) {
        this.saldo += valor;
    }

    protected void debitar(double valor) {
        this.saldo -= valor;
    }

    protected static void validarValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }

    public Cliente getTitular() {
        return titular;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        if (!Cliente.celularValido(celular)) {
            throw new IllegalArgumentException("Celular inválido.");
        }
        this.celular = Cliente.normalizarCelular(celular);
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
}
