package contabancaria;

import cliente.Cliente;

public class ContaPoupanca extends ContaBancaria {

    private double taxaRendimento = 0.5;

    public ContaPoupanca(Cliente titular, String conta, String agencia) {
        super(titular, conta, agencia);
    }

    public ContaPoupanca(Cliente titular, String conta, String agencia, double taxaRendimento) {
        super(titular, conta, agencia);
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public double calcularRendimento() {
        return getSaldo() * (taxaRendimento / 100);
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}
