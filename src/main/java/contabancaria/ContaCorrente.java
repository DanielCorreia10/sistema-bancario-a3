package contabancaria;

import cliente.Cliente;

public class ContaCorrente extends ContaBancaria {

    private static final double LIMITE_CHEQUE_ESPECIAL = 50;
    private static final double TAXA_CHEQUE_ESPECIAL = 0.20;

    private double chequeEspecial;
    private double valorChequeEspecialUsado;

    public ContaCorrente(Cliente titular, String conta, String agencia) {
        super(titular, conta, agencia);
    }

    @Override
    protected void configurarConta() {
        this.chequeEspecial = LIMITE_CHEQUE_ESPECIAL;
    }

    @Override
    public void depositar(double valor) {
        validarValor(valor);

        if (valorChequeEspecialUsado <= 0) {
            creditar(valor);
            return;
        }

        double valorParaCobrir = valorChequeEspecialUsado + getTaxaChequeEspecialDevida();

        if (valor >= valorParaCobrir) {
            creditar(valor - valorParaCobrir);
            valorChequeEspecialUsado = 0;
        } else {
            valorChequeEspecialUsado = valorParaCobrir - valor;
        }
    }

    @Override
    public boolean sacar(double valor) {
        validarValor(valor);

        if (valor <= getSaldo()) {
            return super.sacar(valor);
        }

        double restante = valor - getSaldo();
        if (restante > getLimiteChequeEspecialDisponivel()) {
            return false;
        }

        debitar(getSaldo());
        valorChequeEspecialUsado += restante;
        return true;
    }

    @Override
    public double getChequeEspecial() {
        return chequeEspecial;
    }

    @Override
    public double getValorChequeEspecialUsado() {
        return valorChequeEspecialUsado;
    }

    @Override
    public double getTaxaChequeEspecialDevida() {
        return valorChequeEspecialUsado * TAXA_CHEQUE_ESPECIAL;
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}
