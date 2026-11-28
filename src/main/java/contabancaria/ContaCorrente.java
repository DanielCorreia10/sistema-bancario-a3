package contabancaria;

import cliente.Cliente;

public class ContaCorrente extends ContaBancaria {

    private double chequeEspecial;
    private double valorChequeEspecialUsado;

    public ContaCorrente(Cliente titular, String conta, String agencia) {
        super(titular, conta, agencia);
    }

    @Override
    protected void configurarConta(double primeiroDeposito) {
        if (primeiroDeposito <= 500) {
            this.chequeEspecial = 50;
        } else {
            this.chequeEspecial = primeiroDeposito / 2.0;
        }
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
    public void setValorChequeEspecialUsado(double valorChequeEspecialUsado) {
        this.valorChequeEspecialUsado = valorChequeEspecialUsado;
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}
