package contabancaria;

public class ContaCorrente extends ContaBancaria {

    public ContaCorrente(String nome, String conta, String agencia) {
        super(nome, conta, agencia);
    }

    @Override
    public double calcularChequeEspecial(double primeiroDeposito) {
        if (primeiroDeposito <= 500) {
            return 50;
        }
        return primeiroDeposito / 2.0;
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}
