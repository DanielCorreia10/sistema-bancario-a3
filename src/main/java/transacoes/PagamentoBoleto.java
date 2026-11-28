package transacoes;

import contabancaria.ContaBancaria;

public class PagamentoBoleto extends Transacao {

    public PagamentoBoleto(double valor) {
        super(valor);
    }

    @Override
    public void executar(ContaBancaria conta) {
        if (valor <= 0) {
            System.out.println("O valor do boleto deve ser maior que zero.");
            return;
        }

        if (valor <= conta.getSaldo()) {
            conta.setSaldo(conta.getSaldo() - valor);
            System.out.println("Boleto pago com sucesso!");
        } else {
            System.out.println("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial");

            double valorParaPagarComCheque = valor - conta.getSaldo();

            if (valorParaPagarComCheque <= conta.getLimiteChequeEspecialDisponivel()) {
                conta.setValorChequeEspecialUsado(
                        conta.getValorChequeEspecialUsado() + valorParaPagarComCheque);
                conta.setSaldo(0);

                System.out.println("Transação concluída com sucesso.");
                System.out.println("Sua conta agora está utilizando o cheque especial.");
            } else {
                System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            }
        }

        exibirSaldo(conta);
    }
}
