package transacoes;

import contabancaria.ContaBancaria;

public class Deposito extends Transacao {

    private static final double TAXA_CHEQUE_ESPECIAL = 0.20;

    public Deposito(double valor) {
        super(valor);
    }

    @Override
    public void executar(ContaBancaria conta) {
        if (valor <= 0) {
            System.out.println("O depósito deve ser maior que zero.");
            return;
        }

        double usado = conta.getValorChequeEspecialUsado();

        if (usado > 0) {
            double taxa = usado * TAXA_CHEQUE_ESPECIAL;
            double valorParaCobrir = usado + taxa;

            System.out.println("Você utilizou R$" + formatar(usado) + " do seu cheque especial.");
            System.out.println("Será cobrada uma taxa de 20%: R$" + formatar(taxa));

            if (valor >= valorParaCobrir) {
                conta.setSaldo(conta.getSaldo() + valor - valorParaCobrir);
                conta.setValorChequeEspecialUsado(0);
                System.out.println("Cheque especial e taxa pagos com sucesso!");
            } else {
                double valorRestante = valorParaCobrir - valor;
                conta.setValorChequeEspecialUsado(valorRestante);
                System.out.println("Depósito utilizado para abater a dívida do cheque especial.");
                System.out.println("Valor restante da dívida: R$" + formatar(valorRestante));
            }
        } else {
            conta.setSaldo(conta.getSaldo() + valor);
            System.out.println("Depósito realizado com sucesso!");
        }

        exibirSaldo(conta);
    }
}
