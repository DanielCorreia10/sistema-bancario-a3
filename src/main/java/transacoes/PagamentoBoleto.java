package transacoes;

import contabancaria.ContaBancaria;

public class PagamentoBoleto extends Transacao {

    public PagamentoBoleto(double valor) {
        super(valor);
    }

    @Override
    public String executar(ContaBancaria conta) {
        if (valor <= 0) {
            return "O valor do boleto deve ser maior que zero.";
        }

        StringBuilder msg = new StringBuilder();
        boolean precisaDoLimite = valor > conta.getSaldo();
        if (precisaDoLimite) {
            msg.append("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial\n");
        }

        if (!conta.pagarBoleto(valor)) {
            return msg.append("Saldo + limite insuficiente, não podemos concluir a transação!\n")
                    .append(linhaSaldo(conta)).toString();
        }

        if (precisaDoLimite) {
            msg.append("Transação concluída com sucesso.\n");
            msg.append("Sua conta agora está utilizando o cheque especial.\n");
        } else {
            msg.append("Boleto pago com sucesso!\n");
        }

        return msg.append(linhaSaldo(conta)).toString();
    }
}
