package contabancaria;

public class ContaPoupanca extends ContaBancaria {

    public ContaPoupanca(String nome, String conta, String agencia) {
        super(nome, conta, agencia);
    }

    @Override
    public double calcularChequeEspecial(double primeiroDeposito) {
        return 0;
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}
