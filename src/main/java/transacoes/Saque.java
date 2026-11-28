package transacoes;

import contabancaria.ContaBancaria;

public class Saque extends Transacao {

    public Saque(double valor) {
        super(valor);
    }

    @Override
    public void executar(ContaBancaria conta) {
        if (valor <= 0) {
            System.out.println("O saque deve ser maior que zero.");
            return;
        }

        if (valor > conta.getSaldo()) {
            double restanteParaSacar = valor - conta.getSaldo();

            if (restanteParaSacar <= conta.getLimiteChequeEspecialDisponivel()) {
                conta.setSaldo(0);
                conta.setValorChequeEspecialUsado(conta.getValorChequeEspecialUsado() + restanteParaSacar);

                System.out.println("Saque realizado com sucesso.");
                System.out.println("Valor utilizado do cheque especial: R$"
                        + formatar(conta.getValorChequeEspecialUsado()));
            } else {
                System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            }
        } else {
            conta.setSaldo(conta.getSaldo() - valor);
            System.out.println("Saque realizado com sucesso!");
        }

        exibirSaldo(conta);
    }
}
