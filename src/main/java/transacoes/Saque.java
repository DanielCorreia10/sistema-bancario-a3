package transacoes;

import contabancaria.ContaBancaria;

public class Saque extends Transacao {

    public Saque(double valor) {
        super(valor);
    }

    @Override
    public String executar(ContaBancaria conta) {
        if (valor <= 0) {
            return "O saque deve ser maior que zero.";
        }

        double dividaAntes = conta.getValorChequeEspecialUsado();

        if (!conta.sacar(valor)) {
            return "Saldo + limite insuficiente, não podemos concluir a transação!\n" + linhaSaldo(conta);
        }

        StringBuilder msg = new StringBuilder();
        if (conta.getValorChequeEspecialUsado() > dividaAntes) {
            msg.append("Saque realizado com sucesso.\n");
            msg.append("Valor utilizado do cheque especial: R$")
                    .append(formatar(conta.getValorChequeEspecialUsado())).append("\n");
        } else {
            msg.append("Saque realizado com sucesso!\n");
        }

        return msg.append(linhaSaldo(conta)).toString();
    }
}
