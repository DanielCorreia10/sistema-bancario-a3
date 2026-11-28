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

        boolean precisaDoLimite = valor > conta.getSaldo();
        if (precisaDoLimite) {
            System.out.println("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial");
        }

        if (!conta.pagarBoleto(valor)) {
            System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            exibirSaldo(conta);
            return;
        }

        if (precisaDoLimite) {
            System.out.println("Transação concluída com sucesso.");
            System.out.println("Sua conta agora está utilizando o cheque especial.");
        } else {
            System.out.println("Boleto pago com sucesso!");
        }

        exibirSaldo(conta);
    }
}
