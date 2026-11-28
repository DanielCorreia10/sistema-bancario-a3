package contabancaria;

import cliente.Cliente;

public class ContaPoupanca extends ContaBancaria {

    /** Taxa definida pelo sistema, em percentual por aplicação. Não pode ser alterada. */
    private static final double TAXA_RENDIMENTO = 0.5;

    public ContaPoupanca(Cliente titular, String conta, String agencia) {
        super(titular, conta, agencia);
    }

    @Override
    public double getTaxaRendimento() {
        return TAXA_RENDIMENTO;
    }

    @Override
    public double calcularRendimento() {
        return getSaldo() * (TAXA_RENDIMENTO / 100);
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}
