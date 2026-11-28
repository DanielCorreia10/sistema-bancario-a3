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

        double dividaAntes = conta.getValorChequeEspecialUsado();

        if (!conta.sacar(valor)) {
            System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            exibirSaldo(conta);
            return;
        }

        if (conta.getValorChequeEspecialUsado() > dividaAntes) {
            System.out.println("Saque realizado com sucesso.");
            System.out.println("Valor utilizado do cheque especial: R$"
                    + formatar(conta.getValorChequeEspecialUsado()));
        } else {
            System.out.println("Saque realizado com sucesso!");
        }

        exibirSaldo(conta);
    }
}
